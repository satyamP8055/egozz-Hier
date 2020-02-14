package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.User;

public interface UserDao extends JpaRepository<User, Integer>{

	public User findByUserName(String userName);
	
}
