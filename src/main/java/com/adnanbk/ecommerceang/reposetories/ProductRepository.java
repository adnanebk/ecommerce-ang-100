package com.adnanbk.ecommerceang.reposetories;


import com.adnanbk.ecommerceang.models.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@CrossOrigin
public interface ProductRepository extends JpaRepository<Product, Long> {

    @RestResource(path="byCategory")
    @Query("select prod from Product as prod where prod.category.Id = ?1")
    @Cacheable("byCategory")
    Page<Product> findByCategoryId(Long id, Pageable pageable);

    @RestResource(path="byId")
    @Cacheable("byId")
    Optional<Product> findById(Long id);

    @RestResource(path="byCategoryAndName")
    @Query("select prod from Product as prod where prod.category.Id = ?1 and lower(prod.name) like lower(concat('%', ?2,'%')) ")
    @Cacheable("byCategoryAndName")
    Page<Product> findByCategoryIdAndByNameIgnoreCase(Long id,String name, Pageable pageable);

    @RestResource(path="byName")
    @Cacheable("byName")
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);


    @Cacheable("allPro")
    Page<Product> findAll(Pageable pageable);
    @RestResource(path="byDate")
    Page<Product> findAllByDateCreated(Date date, Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Product> S save(S s);


    @Override
    @RestResource(exported = false)
    void delete(Product product);

}
