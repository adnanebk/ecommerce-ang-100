package com.adnanbk.ecommerceang.reposetories;
import com.adnanbk.ecommerceang.models.CreditCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CreditCardRepo extends CrudRepository<CreditCard, Long> {
    Optional<CreditCard> findByCardNumber(String cardNumber);
}
