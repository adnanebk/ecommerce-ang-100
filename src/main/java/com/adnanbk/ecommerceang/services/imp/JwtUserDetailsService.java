package com.adnanbk.ecommerceang.services.imp;

import com.adnanbk.ecommerceang.exceptions.UserNotEnabledException;
import com.adnanbk.ecommerceang.models.AppUser;
import com.adnanbk.ecommerceang.reposetories.UserRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {

	private final UserRepo userRepo;

	private AppUser user;

	public JwtUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,UserNotEnabledException {
		 user = userRepo.findByUserName(username);
		 if(user==null)
			return null;
		 return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE-USER")));
	}

	public AppUser GetCurrentUser(){
	    return user;
    }
	public AppUser GetCurrentUserByUsername(String username){
		//loadUserByUsername(username);

		return  userRepo.findByUserName(username);
	}
	
/*	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userDao.save(newUser);
	}*/
}
