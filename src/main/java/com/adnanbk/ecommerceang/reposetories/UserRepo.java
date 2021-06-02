package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.AppUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@ApiImplicitParams(@ApiImplicitParam(name = "authorization",
        value = "Bearer jwt-token",paramType = "header"))
public interface UserRepo extends CrudRepository<AppUser,Long> {

    boolean existsByUserName(String userName);

    @RestResource(path="byUserName")
    AppUser findByUserName(String userName);

    Optional<AppUser> findByEmail(String email);
}
