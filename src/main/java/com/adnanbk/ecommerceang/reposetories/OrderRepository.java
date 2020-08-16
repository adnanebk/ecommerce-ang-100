package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.UserOrder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@ApiImplicitParams(@ApiImplicitParam(name = "authorization",
        value = "Bearer jwt-token",paramType = "header"))
public interface OrderRepository extends CrudRepository<UserOrder, Integer> {


    @RestResource(path="byUserName")
   List<UserOrder> findByAppUserUserName(String userName);


}
