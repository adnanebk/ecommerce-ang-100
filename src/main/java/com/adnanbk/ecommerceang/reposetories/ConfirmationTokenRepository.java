package com.adnanbk.ecommerceang.reposetories;

import com.adnanbk.ecommerceang.models.ConfirmationToken;
import com.adnanbk.ecommerceang.models.CreditCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

}
