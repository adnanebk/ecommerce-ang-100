package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepo;
    private ProductCategoryRepository categoryRepo;


    public ProductService(ProductRepository productRepo, ProductCategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;

    }
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Optional<Product> updateProduct(Product product,String baseUrl) {
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
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Product saveProduct(Product product,String baseUrl) {
        if(!product.getCategoryName().isEmpty())
        {
            var cat = categoryRepo.findByCategoryName(product.getCategoryName());
            product.setCategory(cat);
        }
        if(!product.getImageUrl().isEmpty())
        product.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
        return productRepo.save(product);
    }
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public boolean removeProduct(int id) {

        if(!productRepo.existsById((long) id))
            return false;
        else
            productRepo.deleteById((long) id);
        return true;
    }
}
