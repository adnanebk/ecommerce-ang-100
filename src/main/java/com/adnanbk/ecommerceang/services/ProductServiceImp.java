package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.ExcelUtils.ExcelHelperI;
import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    private  ProductRepository productRepo;
    private ProductCategoryRepository categoryRepo;
    private ExcelHelperI<Product> excelHelper;


    public ProductServiceImp(ProductRepository productRepo, ProductCategoryRepository categoryRepo, ExcelHelperI<Product> excelHelper) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.excelHelper = excelHelper;
    }

    @Override
    @CachePut(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},
          key = "#product.id")
    public Optional<Product> updateProduct(Product product, String baseUrl) {
        var updatedProduct =  productRepo.findById(product.getId()).map(prod -> {
            mapProduct(product, baseUrl, prod);
            productRepo.save(prod);
            return prod;
        });
          return updatedProduct;
    }

    @Override
    @CachePut(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},
            key = "#product.id")
    public Product addProduct(Product product, String baseUrl) {
        System.out.println("prod id ****"+product.getId());
            var cat = fetchCategoryByName(product.getCategoryName());
            product.setCategory(cat);
        if(!product.getImageUrl().isEmpty() && !product.getImageUrl().startsWith("http")
                && !product.getImageUrl().startsWith("assets"))
            product.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
        return productRepo.save(product);
    }

    @Override
    @CachePut(value = {"byCategory","byId","byCategoryAndName","byName","allPro"})
    public List<Product> updateProducts(List<Product> products, String baseUrl) {

        var updatedProducts =  productRepo.findAllById(products.stream()
                .map(Product::getId).collect(Collectors.toList()))
                .stream().map(prod -> {
          var product = products.stream().filter(p->p.getId()==prod.getId()).findFirst();
           mapProduct(product.get(),baseUrl,prod);
            return prod;
        });
        return productRepo.saveAll(updatedProducts.collect(Collectors.toList()));
    }

    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},key = "{#productsIds}")
    public void removeProducts( List<Long> productsIds) {
        productRepo.deleteInBatch(productRepo.findAllById(productsIds));
    }

    private ProductCategory fetchCategoryByName(String categoryName){
        if(categoryName==null || categoryName.isEmpty())
        {
            throw new ValidationException("Category is required");

        }
        var cat = categoryRepo.findByCategoryName(categoryName);
            if(cat==null)
            throw new ValidationException("Category not found");
        return cat;
    }

    @CachePut(value = {"byCategory","byId","byCategoryAndName","byName","allPro"})
    public List<Product> saveAllFromExcel(MultipartFile multipartFile){
        try {
            List<Product> products = excelHelper.excelToList(multipartFile.getInputStream());
           if(!products.isEmpty())
            return productRepo.saveAll(products);
           else
               throw new ValidationException("We can't process the file");

        } catch (IOException e) {
            throw new ValidationException("We can't process the file");
        }
    }
    public ByteArrayInputStream loadToExcel(List<Long> Ids) {
        if(Ids!=null && !Ids.isEmpty())
            return excelHelper.listToExcel(productRepo.findAllById(Ids));
        return excelHelper.listToExcel(productRepo.findAll());
    }

    private void mapProduct(Product productSrc, String baseUrl, Product productDest) {
        if (!productDest.getCategoryName().equalsIgnoreCase(productSrc.getCategoryName()))
        {
            var cat = fetchCategoryByName(productSrc.getCategoryName());
            productDest.setCategory(cat);
        }
        productDest.setFromProduct(productSrc);
        if(!productSrc.getImageUrl().startsWith("http") && !productSrc.getImageUrl().startsWith("assets"))
            productDest.setImageUrl(baseUrl +"/uploadingDir/"+ productSrc.getImageUrl());
    }
}
