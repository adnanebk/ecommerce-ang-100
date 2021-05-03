package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import com.adnanbk.ecommerceang.services.UserOderService;
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



    private final UserOderService userOderService;


    public OrderController(UserOderService userOderService) {

        this.userOderService = userOderService;
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
        return ResponseEntity.ok(userOderService.findAllByUserName(userName));
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder( @RequestBody @Valid  UserOrder userOrder, Principal principal){
         UserOrder SavedUserOrder =userOderService.saveOrder(userOrder,principal.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(SavedUserOrder.getId()).toUri();
        return ResponseEntity.created(location).body(SavedUserOrder);
    }


}
