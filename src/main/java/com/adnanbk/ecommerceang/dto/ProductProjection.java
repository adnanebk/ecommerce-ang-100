package com.adnanbk.ecommerceang.dto;

import com.adnanbk.ecommerceang.models.Product;
import com.adnanbk.ecommerceang.models.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Projection(name = "customProduct", types =  Product.class)
public interface ProductProjection {

    public Long getId();

    public ProductCategory getCategory();

    public String getSku();

    public String getName();

    public String getDescription();

    public BigDecimal getUnitPrice();

    public String getImage();

    public boolean isActive();

    public Integer getUnitsInStock();

    public Date getDateCreated();

    public Date getLastUpdated();
}
