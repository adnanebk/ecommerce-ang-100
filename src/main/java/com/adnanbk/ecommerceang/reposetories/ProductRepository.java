package com.adnanbk.ecommerceang.reposetories;


import com.adnanbk.ecommerceang.models.Product;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    @Query("select count(p)>0 from Product p where p.id != ?1 and  p.name = ?2")
    boolean existsByIdNotAndName(long id,String name);

    boolean existsByName(String name);

    @Query("select count(p)>0 from Product p where p.id != ?1 and  p.sku = ?2")
    boolean existsByIdNotAndSkuIs(long id,String sku);


    @RestResource(path="byCategory")
    //@Query("select prod from Product as prod where prod.category.Id = ?1")
    Page<Product> findByCategoryId(Long id, Pageable pageable);

    @RestResource(path="byId")
    @Cacheable("byProId")
    Optional<Product> findById(Long id);



    @RestResource(path="byCategoryAndName")
    //@Query("select prod from Product as prod where prod.category.Id = ?1 and lower(prod.name) like lower(concat('%', ?2,'%')) ")
    Page<Product>  findByCategoryIdAndNameIgnoreCase(Long id,String name, Pageable pageable);

    @RestResource(path="ByNameOrDescription")
    Page<Product> findByNameIgnoreCaseContainsOrDescriptionIgnoreCaseContains(String name,String description, Pageable pageable);

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
    @CacheEvict(value = {"allPro"},allEntries = true)
    void delete(Product product);

    @Override
    @CacheEvict(value = {"allPro"},allEntries = true)
    void deleteById(Long id);
}
