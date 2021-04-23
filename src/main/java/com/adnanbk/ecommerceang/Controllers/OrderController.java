package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.security.Principal;


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
    @ApiOperation(value = "get orders by username",notes = "this endpoint returns all orders of the specified username including the order items ")
    public ResponseEntity<Iterable<UserOrder>> getOrders(@PathVariable String userName){
        var ls=orderRepository.findAll();
        var userOrders = orderRepository.findByAppUser_UserName(userName);
        return ResponseEntity.ok(userOrders);
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder(@Valid @RequestBody UserOrder userOrder, Principal principal){
        AppUser appUser =userRepo.findByUserName(principal.getName());
        userOrder.setAppUser(appUser);
        userOrder.setUserOrderItems(orderItemRepo.saveAll(userOrder.getOrderItems()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userOrder.getId()).toUri();

        return ResponseEntity.created(location).body(orderRepository.save(userOrder));
    }


}
