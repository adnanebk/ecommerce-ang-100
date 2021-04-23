package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.Utils.ExcelHelperI;
import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import com.adnanbk.ecommerceang.services.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    private ProductRepository productRepo;
    private ProductCategoryRepository categoryRepo;
    private ExcelHelperI<Product> excelHelper;

    public ProductServiceImp(ProductRepository productRepo, ProductCategoryRepository categoryRepo, ExcelHelperI<Product> excelHelper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.excelHelper = excelHelper;
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public  Product updateProduct(Product product, String baseUrl) {
        return productRepo.findById(product.getId()).map(prod -> {
            mapProduct(product, prod,baseUrl);
          return   productRepo.save(prod);
        }).get();
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public Product addProduct(Product product, String baseUrl) {
        var cat = fetchCategoryByName(product.getCategoryName());
        product.setCategory(cat);
        if (!product.getImage().isEmpty() && !product.getImage().startsWith("http")
                && !product.getImage().startsWith("assets"))
            product.setImage(baseUrl + "/api/products/images/" + product.getImage());
        return productRepo.save(product);
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public List<Product> updateProducts(List<Product> products, String baseUrl) {
        var updatedProducts = findAndMapProducts(products, baseUrl);
        return productRepo.saveAll(updatedProducts);
    }


    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public void removeProducts(List<Long> productsIds) {
        productRepo.deleteInBatch(productRepo.findAllById(productsIds));
    }

    private ProductCategory fetchCategoryByName(String categoryName) {
        var cat = categoryRepo.findByName(categoryName);
        if (cat == null)
            throw new ValidationException("Category not found");
        return cat;
    }

    @CacheEvict(value =  "allPro",allEntries = true)
    public List<Product> saveAllFromExcel(MultipartFile multipartFile, String baseUrl) {
        try {
            List<Product> products = excelHelper.excelToList(multipartFile.getInputStream())
                                    .stream().map(product -> mapProduct(product,baseUrl)).collect(Collectors.toList());
                 if(products.size()>0)
                return productRepo.saveAll(products);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
            throw new ValidationException("We can't process the file");
    }

    @Override
    public List<Product> searchProducts(String query) {
        if(query==null|| query.isEmpty() || query.trim().isEmpty())
            return productRepo.findAll();
        return productRepo.findAll().stream().filter(prod -> prod.getName().toLowerCase().contains(query)
                || prod.getDescription().toLowerCase().contains(query)).collect(Collectors.toList());
    }




    public ByteArrayInputStream loadToExcel(List<Long> Ids) {
        if (Ids != null && !Ids.isEmpty())
            return excelHelper.listToExcel(productRepo.findAllById(Ids));
        return excelHelper.listToExcel(productRepo.findAll());
    }

    private void mapProduct(Product productSrc, Product productDest,String baseUrl) {
        if (productSrc.getCategoryName() != null &&
                !productDest.getCategoryName().equalsIgnoreCase(productSrc.getCategoryName())) {
            var cat = fetchCategoryByName(productSrc.getCategoryName());
            productDest.setCategory(cat);
        }
        productDest.setFromProduct(productSrc);
        if (!productSrc.getImage().startsWith("http") && !productSrc.getImage().startsWith("assets"))
            productDest.setImage(baseUrl + "/api/products/images/" + productSrc.getImage());
    }
    private Product mapProduct(Product productSrc,String baseUrl) {
        if (productSrc.getCategoryName() != null){
            var cat = fetchCategoryByName(productSrc.getCategoryName());
            productSrc.setCategory(cat);
        }
        if (!productSrc.getImage().startsWith("http") && !productSrc.getImage().startsWith("assets"))
            productSrc.setImage(baseUrl + "/api/products/images/" + productSrc.getImage());
        return productSrc;
    }
    private List<Product> findAndMapProducts(List<Product> products, String baseUrl) {
        return productRepo.findAllById(products.stream()
                .map(Product::getId).collect(Collectors.toList()))
                .stream().peek(prod -> {
                    var product = products.stream().filter(p -> p.getId().equals(prod.getId())).findFirst();
                    product.ifPresent(value -> mapProduct(value, prod,baseUrl));
                }).collect(Collectors.toList());
    }
}
