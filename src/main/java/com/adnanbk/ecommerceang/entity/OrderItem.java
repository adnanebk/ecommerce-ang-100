package com.adnanbk.ecommerceang.entity;

import lombok.Data;
import org.apache.catalina.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class OrderItem {


    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long Id;

    @Column(name = "name")
    private String name;

    private Long productId;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

  /* @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private UserOrder userOrder;*/

    private int quantity;

}
