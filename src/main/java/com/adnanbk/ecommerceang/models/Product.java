package com.adnanbk.ecommerceang.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="product")
@Data
@JsonIgnoreProperties(value = {"dateCreated","lastUpdated"}, allowGetters = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private ProductCategory category;

    @Transient
    private String categoryName;


    @Column(name = "sku",unique = true)
    @NotEmpty
    private String sku;

    @Column(name = "name",unique = true)
    @NotEmpty
    private String name;

    @Column(name = "description")
    @Length(min = 10,message = "{error.min}")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    @NotEmpty
    private String imageUrl;

    @Column(name = "active")
    private boolean active;

    @Column(name = "units_in_stock")
    private int unitsInStock;

    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Id.equals(product.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    public String getCategoryName() {
        if ((getCategory()!=null))
            this.categoryName = category.getCategoryName();
        return categoryName;
    }



    public void setFromProduct(Product product) {
        this.categoryName=product.categoryName;
        this.sku = product.sku;
        this.name = product.name;
        this.description = product.description;
        this.unitPrice = product.unitPrice;
        this.active = product.active;
        this.unitsInStock = product.unitsInStock;
        this.dateCreated = product.dateCreated;
        this.lastUpdated = product.lastUpdated;
    }
}
