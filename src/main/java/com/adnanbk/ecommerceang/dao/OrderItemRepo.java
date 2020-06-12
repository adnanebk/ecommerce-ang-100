package com.adnanbk.ecommerceang.dao;

import com.adnanbk.ecommerceang.entity.OrderItem;
import com.adnanbk.ecommerceang.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepo  extends JpaRepository<OrderItem, Integer> {
}
