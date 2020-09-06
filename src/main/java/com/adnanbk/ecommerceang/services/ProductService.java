package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> updateProduct(Product product, String baseUrl);

    Product addProduct(Product product, String baseUrl);

    List<Product> updateProducts(List<Product> products, String baseUrl);

    void removeProducts(List<Long> productsIds);
    ByteArrayInputStream loadToExcel(List<Long> productsIds);
   List<Product> saveAllFromExcel(MultipartFile multipartFile);
}
