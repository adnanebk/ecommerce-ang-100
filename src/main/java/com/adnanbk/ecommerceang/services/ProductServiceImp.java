package com.adnanbk.ecommerceang.services;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.reposetories.ProductCategoryRepository;
import com.adnanbk.ecommerceang.reposetories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    private  ProductRepository productRepo;
    private ProductCategoryRepository categoryRepo;


    public ProductServiceImp(ProductRepository productRepo, ProductCategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Optional<Product> updateProduct(Product product, String baseUrl) {
        var updatedProduct =  productRepo.findById(product.getId()).map(prod -> {
            mapProduct(product, baseUrl, prod);
            productRepo.save(prod);
            return prod;
        });
          return updatedProduct;
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

    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
    public Product addProduct(Product product, String baseUrl) {
        System.out.println("prod id ****"+product.getId());
            var cat = fetchCategoryByName(product.getCategoryName());
            product.setCategory(cat);
        if(!product.getImageUrl().isEmpty() && !product.getImageUrl().startsWith("http")
                && !product.getImageUrl().startsWith("assets"))
            product.setImageUrl(baseUrl+"/uploadingDir/"+product.getImageUrl());
        System.out.printf("save prodd add ");
        return productRepo.save(product);
    }

    @Override
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
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
    @CacheEvict(value = {"byCategory","byId","byCategoryAndName","byName","allPro"},allEntries = true)
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
}
