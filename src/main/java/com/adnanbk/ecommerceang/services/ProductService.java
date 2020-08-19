package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Optional;

public interface ProductService {

    Optional<Product> updateProduct(Product product, String baseUrl);

    Product addProduct(Product product, String baseUrl);
}
