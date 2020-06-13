package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo  extends JpaRepository<OrderItem, Integer> {
}
