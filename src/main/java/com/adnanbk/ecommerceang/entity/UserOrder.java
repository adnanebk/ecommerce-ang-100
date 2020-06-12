package com.adnanbk.ecommerceang.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Length(min = 2,message = "should be greater or equal 2")
    private String firstName;

    @NotEmpty
    @Length(min = 2,message = "should be greater or equal 2")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String country;

    @NotEmpty
    @Length(min = 4,message = "should be greater or equal 4")
    private String street;

    @NotEmpty
    private String city;

    private int quantity;
    private double totalPrice;

    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true
           // ,mappedBy = "userOrder"
    )
    private List<OrderItem> orderItems;

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


/*    private String country;

    private String city;


    private String street;*/


 /*   @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Product> products;*/


}
