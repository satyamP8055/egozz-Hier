package com.hss.egoz.authentication.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * @author Satyam Pandey
 * Repository for CRUD of JWT tokens
 * */
public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query("select t from Token t where t.tokenName=:tokenName")
	public Token findToken(@Param("tokenName") String tokenName);

}
