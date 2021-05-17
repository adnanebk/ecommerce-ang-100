package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.Utils.ExcelHelperI;
import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import com.adnanbk.ecommerceang.services.ProductService;
import org.springframework.beans.factory.annotation.Value;
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
    private ExcelHelperI<Product> excelHelper;

    @Value("${api.url}")
    private String baseUrl;
    public ProductServiceImp(ProductRepository productRepo, ExcelHelperI<Product> excelHelper) {
        this.productRepo = productRepo;
        this.excelHelper = excelHelper;
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public Product addProduct(Product product) {

           mapProductImage(product);
        return productRepo.save(product);
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public  Product updateProduct(Product product) {
        Product prod= productRepo.getOne(product.getId());
        mapProduct(product, prod);
        return   productRepo.save(prod);
    }

    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public List<Product> updateProducts(List<Product> products) {
        var updatedProducts = mapProducts(products);
        return productRepo.saveAll(updatedProducts);
    }


    @Override
    @CacheEvict(value =  "allPro",allEntries = true)
    public void removeProducts(List<Long> productsIds) {
        productRepo.deleteInBatch(productRepo.findAllById(productsIds));
    }

    @CacheEvict(value =  "allPro",allEntries = true)
    public List<Product> saveAllFromExcel(MultipartFile multipartFile, String baseUrl) {
        try {
            List<Product> products = excelHelper.excelToList(multipartFile.getInputStream())
                                    .stream().map(this::mapProductImage).toList();
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

    private void mapProduct(Product productSrc, Product productDest) {
        productDest.setFromProduct(productSrc);
            mapProductImage(productDest);
    }
    private Product mapProductImage(Product productSrc) {
        if (!productSrc.getImage().startsWith("http") && !productSrc.getImage().startsWith("assets"))
            productSrc.setImage(baseUrl + "/products/images/" + productSrc.getImage());
        return  productSrc;
    }
    private List<Product> mapProducts(List<Product> products) {
        return productRepo.findAllById(products.stream()
                .map(Product::getId).collect(Collectors.toList()))
                .stream().peek(prod -> {
                    var product = products.stream().filter(p -> p.getId().equals(prod.getId())).findFirst();
                    product.ifPresent(value -> mapProduct(value, prod));
                }).collect(Collectors.toList());
    }
}
