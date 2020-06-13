package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.entity.ProductCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {
}
