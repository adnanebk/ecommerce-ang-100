package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.*;
import com.adnanbk.ecommerceang.services.ImageService;
import com.adnanbk.ecommerceang.services.ProductService;
import com.adnanbk.ecommerceang.validations.ProductValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.*;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;




@CrossOrigin
@RepositoryRestController
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private ProductValidator productValidator;
    private String baseUrl="";

    public ProductController(ImageService imageService, ProductService productService,ProductValidator productValidator) {
        this.imageService = imageService;
        this.productService = productService;
        this.productValidator = productValidator;
    }


        @InitBinder("product") // add this parameter to apply this binder only to request parameters with this name
    protected void bidValidator(WebDataBinder binder) {
            binder.addValidators(productValidator);
    }


    @PostMapping(value = "/products/images")
    @ApiOperation(value = "Create product image",notes = "this endpoint return image url",response = String.class)
    public Callable<ResponseEntity<String>> UploadProductImage(@RequestParam("image") MultipartFile file){

      return   ()->{
             String url;
            try {
                url = getBaseUrl()+"/uploadingDir/"+this.imageService.CreateImage(file);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok(url);
        };

    }
    @PostMapping("/products/v2")
    @ApiOperation(value = "Add new product",notes = "This endpoint bind a category to created product based on category name ," +
            "and it  also create image url based on the file name",response = Product.class)
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product){
        System.out.printf("add prod");
        Product prod = productService.addProduct(product,getBaseUrl());
        return ResponseEntity.created(URI.create("/api/products/"+product.getId())).body(prod);
    }
    @PutMapping("/products/v2")
    @ApiOperation(value = "update product",notes = "This endpoint  bind a category to updated product based on category name ," +
            "and it  also create image url based on the file name",response = Product.class)
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product){
        System.out.printf("update prod");
        Optional<Product> updatedProduct =productService.updateProduct(product,getBaseUrl());
      if(updatedProduct.isEmpty())
          return ResponseEntity.badRequest().body("Product not found");

        return ResponseEntity.ok(updatedProduct.get());
    }

public String getBaseUrl(){
  if(baseUrl.isEmpty())
    baseUrl =ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    return baseUrl;

}

}
