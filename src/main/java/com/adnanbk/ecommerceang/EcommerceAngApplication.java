package com.adnanbk.ecommerceang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableCaching
@EnableAsync
public class EcommerceAngApplication {

    public EcommerceAngApplication() {

    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("byId","byCategory","byCategoryAndName","byName","allPro");
    }

    public static void main(String[] args) {

        SpringApplication.run(EcommerceAngApplication.class, args);
     /*   Product p =new Product();
        p.setCategory(productCategoryRepository.findById((long) 1).get());*/

        //orderRepository.save(new Order());
        System.out.println("");
    }


}
