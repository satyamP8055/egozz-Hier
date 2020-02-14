package com.hss.egoz.authentication.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * @author Satyam Pandey
 * Service to perform token based operations...
 * */
@Service
public class TokenService {

	private String secret;

	private String member;

	@Autowired
	private TokenRepository tokenRepository;

	@PostConstruct
	public void initializeSecret() {
		secret = new BCryptPasswordEncoder().encode("egoz");
	}

	public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;

	/*
	 * Generate a new token by a given String
	 * 
	 * @Param : 1. string member for which token is to be generated..
	 * 
	 * @Return : string encoded token containing the passed member as a key
	 */
	public String generateToken(String member) {
		Map<String, Object> claims = new HashMap<String, Object>();
		String token = Jwts.builder().setClaims(claims).setSubject(member).setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
		Token t = new Token();
		Date generatedAt = new Date();
		t.setGeneratedAt(generatedAt);
		t.setExpiryDate(new Date(generatedAt.getTime() + 2 * JWT_TOKEN_VALIDITY));
		t.setTokenName(token);
		tokenRepository.save(t);
		return token;
	}

	/*
	 * Generate a new token by a given Integer
	 * 
	 * @Param : 1. Integer member for which token is to be generated..
	 * 
	 * @Return : string encoded token containing the passed member as a key
	 */
	public String generateToken(Integer member) {
		return this.generateToken(String.valueOf(member));
	}

	/*
	 * Get back the member as String from the token
	 * 
	 * @Param : Token String containing the member
	 * 
	 * @Return : Passed member as a String
	 */
	public String getMember(String tokenKey) throws JwtException {
		if (validateToken(tokenKey))
			return member;
		else
			throw new JwtException("Invalid Token");
	}

	/*
	 * Get back the member as String from the token
	 * 
	 * @Param : Token String containing the member
	 * 
	 * @Return : Passed member as a Integer
	 */
	public Integer getMemberId(String tokenKey) {
		try {
			return Integer.parseInt(this.getMember(tokenKey));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/*
	 * Validate if the token passed is still valid & is having a valid key..
	 */
	public Boolean validateToken(String tokenKey) {
		try {
			final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(tokenKey).getBody();
			Function<Claims, String> resolver = Claims::getSubject;
			member = resolver.apply(claims);
			if (member == null)
				return false;
			else {
				Token token = tokenRepository.findToken(tokenKey);
				if (token == null || new Date().compareTo(token.getExpiryDate()) > 0) {
					if (token != null)
						tokenRepository.delete(token);
					return false;
				} else
					return true;
			}
		} catch (Exception exception) {
			return false;
		}
	}

	/*
	 * Invalidate the token... (Remove from Database) ...
	 */
	public void invalidate(String tokenKey) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(tokenKey).getBody();
		Function<Claims, String> resolver = Claims::getSubject;
		member = resolver.apply(claims);
		if (member != null) {
			Token token = tokenRepository.findToken(tokenKey);
			if (token != null)
				tokenRepository.delete(token);
		}
	}

}
