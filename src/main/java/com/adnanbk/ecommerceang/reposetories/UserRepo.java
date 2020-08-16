package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.AppUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.repository.CrudRepository;
@ApiImplicitParams(@ApiImplicitParam(name = "authorization",
        value = "Bearer jwt-token",paramType = "header"))
public interface UserRepo extends CrudRepository<AppUser,Integer> {
    boolean existsByUserName(String userName);
    AppUser findByUserName(String userName);

}
