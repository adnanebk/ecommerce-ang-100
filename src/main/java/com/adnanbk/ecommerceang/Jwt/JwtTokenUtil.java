package com.adnanbk.ecommerceang.Jwt;


import com.adnanbk.ecommerceang.services.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = -2550185165626007488L;

	@Value("${jwt.expiration-time}")
	private int validityTime;

	@Value("${jwt.secret}")
	private String secret;
	private JwtUserDetailsService userDetailsService;


	public JwtTokenUtil(JwtUserDetailsService userDetailsService) {

		this.userDetailsService = userDetailsService;
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}


	//generate token for user
	public String generateToken(String username)  {
		//final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		//final UserDetails userDetails = use(username);
		Map<String, Object> claims = new HashMap<>();
		//claims.put("user",this.userDetailsService.GetCurrentUser());
		//claims.put("email",userDetailsService.GetCurrentUser().getEmail());
		//claims.put("photo",fileSystemStorageService.loadAsResource(userDetailsService.GetCurrentuser().getPhoto()).getURL());
		//String url=fileSystemStorageService.loadFromCloudinary(userDetailsService.GetCurrentuser().getPhoto());
		//claims.put("photoUrl",url);
		return doGenerateToken(claims, username);
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + validityTime * 86400000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

//	//validate token
//	public Boolean validateToken(String token, UserDetails userDetails) {
//		final String username = getUsernameFromToken(token);
//		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//	}
	//validate token
	public UserDetails validateToken(String token, String username) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
		if(userDetails==null)
			return null;
		final String usernameInToken = getUsernameFromToken(token);
		if (usernameInToken.equals(userDetails.getUsername()) && !isTokenExpired(token))
			return userDetails;
		return null;
	}
	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public int getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(int validityTime) {
		this.validityTime = validityTime;
	}
}