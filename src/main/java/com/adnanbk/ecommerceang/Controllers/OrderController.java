package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.CreditCard;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.services.UserOderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class OrderController {



    private final UserOderService userOderService;


    public OrderController(UserOderService userOderService) {

        this.userOderService = userOderService;
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder( @RequestBody @Valid  UserOrder userOrder, Principal principal){
        if(principal==null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"you have no access");
         UserOrder SavedUserOrder =userOderService.saveOrder(userOrder,principal.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(SavedUserOrder.getId()).toUri();
        return ResponseEntity.created(location).body(SavedUserOrder);
    }


}
