package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

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

          prod.setCategory(categoryRepo.findByCategoryName(product.getCategoryName()));
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
        if(!product.getCategoryName().isEmpty())
        {
            var cat = categoryRepo.findByCategoryName(product.getCategoryName());
            product.setCategory(cat);
        }
        if(!product.getImageUrl().isEmpty())
        product.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
        return productRepo.save(product);
    }

}
