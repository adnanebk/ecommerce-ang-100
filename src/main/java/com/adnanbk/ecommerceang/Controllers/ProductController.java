package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.*;
import com.adnanbk.ecommerceang.security.ImageService;
import com.adnanbk.ecommerceang.services.ProductService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.PersistenceException;
import javax.validation.*;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RepositoryRestController
@CrossOrigin
public class ProductController {

    private ImageService imageService;
    private ProductService productService;

    public ProductController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @PostMapping(value = "/products/images",consumes = { "multipart/form-data" },produces = {"application/json"})
    public ResponseEntity UploadProductImage(@RequestParam("image") MultipartFile file){
        String url="";
        String burl =ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
             url = burl+"/uploadingDir/"+this.imageService.CreateImage(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
      return ResponseEntity.ok(url);
    }

    @PutMapping("/products")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product){
        Optional<Product> updatedProduct =productService.updateProduct(product,ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
      if(updatedProduct.isEmpty())
          return ResponseEntity.badRequest().body("Product not found");

        return ResponseEntity.ok(updatedProduct.get());
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable int id){

      boolean isRemoved =this.productService.removeProduct(id);
       if(!isRemoved)
           return ResponseEntity.badRequest().body("Product not found");
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product){
    Product prod = productService.saveProduct(product,ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());

        return ResponseEntity.created(URI.create("/api/products/"+product.getId())).body(prod);
    }



}
