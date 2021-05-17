package com.adnanbk.ecommerceang.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="product_category")
@Data
public class ProductCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",unique = true)
    @Length(min = 2,message = "{error.min}")
    private String name;

/*    @OneToMany( mappedBy = "category",orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Product> products;*/





}
