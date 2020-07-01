package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<AppUser,Integer> {
    boolean existsByUserName(String userName);
    AppUser findByUserName(String userName);

}
