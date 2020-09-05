package com.adnanbk.ecommerceang.models;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="product_category")
// @Data -- known bug
@Getter
@Setter
public class ProductCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "category_name",unique = true)
    @Length(min = 2,message = "{error.min}")
    private String categoryName;

    @OneToMany( mappedBy = "category",orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<Product> products;


public void addProduct(Product product){
    products.add(product);
  product.setCategory(this);
}

    public void removeProduct(Product product){
        products.remove(product);
        product.setCategory(null);
    }


}







