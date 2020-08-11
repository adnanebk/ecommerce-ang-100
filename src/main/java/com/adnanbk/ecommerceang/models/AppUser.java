package com.adnanbk.ecommerceang.models;

import com.adnanbk.ecommerceang.validations.ConfirmPassword;
import com.adnanbk.ecommerceang.validations.UniqueUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@JsonIgnoreProperties(value = {"password","id"}, allowSetters = true)
@ConfirmPassword
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	@UniqueUser
	@NotEmpty
	private String userName;



	@Column
	@Email
	private String email;

	@Column
	@NotEmpty
	private String firstName;

	@Column
	@NotEmpty
	private String lastName;

	@Column
	private String adress;

	private String city;
	private String country;

	@Column
	@NotEmpty
	@Length(min = 4,message = "{error.min}")
	private String password;
	@Length(min = 4,message = "{error.min}")
	@Transient
	private String confirmPassword;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser",orphanRemoval = true)
	private Set<UserOrder> userOrders;

	public void addOrder(UserOrder order){
		userOrders.add(order);
		order.setAppUser(this);
	}






}