package com.adnanbk.ecommerceang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select() // builder object
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build() // build the object
                .apiInfo(apiInfo());
    }
    @Bean
    public ApiInfo apiInfo(){
         return new ApiInfo("ecommerce app api",
                 "sample Api for testing the application functionalities",
                 "1.0","fee",new Contact("Adnane Benkouider","http://adnanbk.herokuapp.com",
                 "benkouider.adnane@gmail.com"),"Api license","", Collections.emptyList());
    }
}
