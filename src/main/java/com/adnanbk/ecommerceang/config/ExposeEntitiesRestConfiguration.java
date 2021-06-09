package com.adnanbk.ecommerceang.config;

import com.adnanbk.ecommerceang.dto.ProductProjection;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.mediatype.MessageResolver;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.mvc.RepresentationModelProcessorInvoker;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class ExposeEntitiesRestConfiguration implements RepositoryRestConfigurer, WebMvcConfigurer {


    @Value("${front.url}")
    private String origin;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        cors.addMapping("/api/**").allowedOrigins(origin);
        config.exposeIdsFor(ProductCategory.class,Product.class, AppUser.class);
    }

    @Override
    public void addCorsMappings(CorsRegistry cors) {
        cors.addMapping("/api/**").allowedOrigins(origin);
    }
}

