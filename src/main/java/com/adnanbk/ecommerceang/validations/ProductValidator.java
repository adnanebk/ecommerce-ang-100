package com.adnanbk.ecommerceang.validations;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import com.adnanbk.ecommerceang.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {


    private ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
       Product product = (Product) o;
       if(product.getName().isEmpty() || product.getSku().isEmpty())
           return;
        if(product.getId()!=0) // when the product updated
        {
            boolean isNameExist =productRepository.existsByIdNotAndName(product.getId(),product.getName());
            boolean isSkuExist =productRepository.existsByIdNotAndSkuIs(product.getId(),product.getSku());

            if(isNameExist)
                errors.reject("Name already exists");

            if(isSkuExist)
                errors.reject( "Sku already exists");
        }
        else
        {
            boolean isNameExist = productRepository.existsByName(product.getName());
            boolean isSkuExist =  productRepository.existsBySku(product.getSku());

            if(isNameExist)
                errors.reject("Name already exists");

            if(isSkuExist)
                errors.reject( "Sku already exists");
        }



    }

}
