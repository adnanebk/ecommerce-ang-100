package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.dto.ApiError;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.dto.ResponseError;
import com.adnanbk.ecommerceang.models.OrderItem;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.security.Principal;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class OrderController {


    private final OrderRepository orderRepository;
    private final OrderItemRepo orderItemRepo;
    private final UserRepo userRepo;


    public OrderController(OrderRepository orderRepository, OrderItemRepo orderItemRepo, UserRepo userRepo) {
        this.orderRepository = orderRepository;
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
    }


/*   @PostConstruct
    public void postControll(){
       UserOrder o=new UserOrder();
       o.setEmail("aa@aa");
       o.setFirstName("a");
       o.setLastName("aa@aa");
       orderRepository.save(o);

       System.out.println("ok");
    }*/

    @GetMapping("/userOrders/byUserName/{userName}")
    @ApiOperation(value = "get orders by username",notes = "this endpoint returns all orders of the specified user name including the order items ")
    public ResponseEntity<Iterable<UserOrder>> getOrders(@PathVariable String userName){
    var ll = orderRepository.findByAppUserUserName(userName);
        return ResponseEntity.ok(ll);
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder(@Valid @RequestBody UserOrder userOrder, Principal principal){
        AppUser appUser =userRepo.findByUserName(principal.getName());
        userOrder.setAppUser(appUser);
       userOrder.setUserOrderItems(orderItemRepo.saveAll(userOrder.getOrderItems()));
        return new ResponseEntity(orderRepository.save(userOrder),HttpStatus.CREATED);
    }


}
