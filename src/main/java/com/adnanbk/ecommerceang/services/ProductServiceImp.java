package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private static ProductRepository productRepo;
    private ProductCategoryRepository categoryRepo;



    public ProductServiceImp(ProductRepository productRepo, ProductCategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;

    }

    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Optional<Product> updateProduct(Product product, String baseUrl) {
      var updatedProduct =  productRepo.findById(product.getId()).map(prod -> {
          if (!prod.getCategoryName().equalsIgnoreCase(product.getCategoryName()))
          {
              var cat = fetchCategoryByName(product.getCategoryName());
              prod.setCategory(cat);
          }
              if(!product.getImageUrl().equals(prod.getImageUrl()))
              prod.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
              prod.setFromProduct(product);
              productRepo.save(prod);
          return prod;
        });
      return updatedProduct;
    }
    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Product addProduct(Product product, String baseUrl) {
        System.out.println("prod id ****"+product.getId());
            var cat = fetchCategoryByName(product.getCategoryName());
            product.setCategory(cat);
        if(!product.getImageUrl().isEmpty())
        product.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
        System.out.printf("save prodd add ");
        return productRepo.save(product);
    }

    public ProductCategory fetchCategoryByName(String categoryName){
        var cat = categoryRepo.findByCategoryName(categoryName);
        if(cat==null)
            throw new ValidationException("Category not found");
        return cat;
    }
}
