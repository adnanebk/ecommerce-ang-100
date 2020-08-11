package com.adnanbk.ecommerceang.Controllers;

import com.adnanbk.ecommerceang.dto.ApiError;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.dto.ResponseError;
import com.adnanbk.ecommerceang.models.UserOrder;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
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


@RepositoryRestController
@CrossOrigin
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
    public ResponseEntity<Iterable<UserOrder>> getOrders(@PathVariable(required = false) String userName){

        return ResponseEntity.ok(orderRepository.findByAppUserUserName(userName));
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder(@Valid @RequestBody UserOrder userOrder, Principal principal){
        AppUser appUser =userRepo.findByUserName(principal.getName());
        userOrder.setAppUser(appUser);
       userOrder.setOrderItems(orderItemRepo.saveAll(userOrder.getOrderItems()));
       // userOrder.se(productRepository.findAllById(userOrder.getProducts().stream().map(p->p.getId()).collect(Collectors.toList())));
        return new ResponseEntity(orderRepository.save(userOrder),HttpStatus.CREATED);
    }



    @ExceptionHandler({ PersistenceException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(
            PersistenceException ex) {
        Set<ResponseError> errors = new HashSet<>();
        if(ex.getCause() instanceof ConstraintViolationException )
        {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            for (ConstraintViolation<?> violation : cause.getConstraintViolations()) {
                errors.add(new ResponseError(violation.getPropertyPath().toString(),violation.getMessage()));
            }
        }


        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new  ResponseEntity(apiError, apiError.getStatus());
    }

}
