package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.services.UserOderService;
import com.adnanbk.ecommerceang.validations.OrderValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.security.Principal;


@RestController
@RequestMapping("/api")
public class OrderController {


       @InitBinder("userOrder") // add this parameter to apply this binder only to request parameters with this name
    protected void bidValidator(WebDataBinder binder) {
        binder.addValidators(orderValidator);
    }

    private final UserOderService userOderService;
    private final OrderValidator orderValidator;

    public OrderController(UserOderService userOderService, OrderValidator orderValidator) {

        this.userOderService = userOderService;
        this.orderValidator = orderValidator;
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
