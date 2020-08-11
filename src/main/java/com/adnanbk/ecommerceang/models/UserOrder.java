package com.adnanbk.ecommerceang.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty
    @Length(min = 2,message = "{error.min}")
    private String firstName;

    @NotEmpty
    @Length(min = 2,message = "{error.min}")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String country;

    @NotEmpty
    @Length(min = 4,message = "{error.min}")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private AppUser appUser;

/*    private String country;

    private String city;


    private String street;*/


 /*   @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Product> products;*/


}
