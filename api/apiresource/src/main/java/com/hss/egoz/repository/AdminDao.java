package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.Admin;

public interface AdminDao extends JpaRepository<Admin, Integer>{

	public Admin findByUserName(String userName);
	
}
