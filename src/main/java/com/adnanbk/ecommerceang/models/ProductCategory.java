package com.adnanbk.ecommerceang.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="product_category")
// @Data -- known bug
@Getter
@Setter
public class ProductCategory {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long Id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category",orphanRemoval = true)
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







