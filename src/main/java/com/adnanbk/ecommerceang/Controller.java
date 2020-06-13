package com.adnanbk.ecommerceang;

import com.adnanbk.ecommerceang.entity.ApiError;
import com.adnanbk.ecommerceang.entity.ResponseError;
import com.adnanbk.ecommerceang.entity.UserOrder;
import com.adnanbk.ecommerceang.reposetories.OrderItemRepo;
import com.adnanbk.ecommerceang.reposetories.OrderRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.*;


@RepositoryRestController
@CrossOrigin
public class Controller  {


    private OrderRepository orderRepository;
    private OrderItemRepo orderItemRepo;


    public Controller(OrderRepository orderRepository, OrderItemRepo orderItemRepo) {
        this.orderRepository = orderRepository;
        this.orderItemRepo = orderItemRepo;
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

    @GetMapping("/userOrders")
    public ResponseEntity<Iterable<UserOrder>> getOrders(){
        return ResponseEntity.ok(orderRepository.findAll());
    }


    @PostMapping("/userOrders")
    public ResponseEntity<UserOrder> saveOrder( @Valid @RequestBody UserOrder userOrder){
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
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

}
