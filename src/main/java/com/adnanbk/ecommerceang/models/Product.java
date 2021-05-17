package com.adnanbk.ecommerceang.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "{error.choose}")
    private ProductCategory category;

    @Column(name = "sku",unique = true)
    @NotEmpty
    private String sku;

    @Column(name = "name",unique = true)
    @NotEmpty
    private String name;

    @Column(name = "description",length = 500)
    @Length(min = 10,message = "{error.min}")
    private String description;

    @Column(name = "unit_price")
    @DecimalMin("0.0")
    private BigDecimal unitPrice;

    @Column(name = "image")
    @NotEmpty(message = "{error.upload}")
    private String image;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    @Min(value = 0)
    private Integer unitsInStock;

    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;



    public void setFromProduct(Product product) {
        this.setCategory(product.category);
        this.setImage(product.image);
        this.setSku(product.sku);
        this.setName(product.name);
        this.setDescription(product.description);
        this.setUnitPrice(product.unitPrice);
        this.setActive(product.active);
        this.setUnitsInStock(product.unitsInStock);
    }
}
