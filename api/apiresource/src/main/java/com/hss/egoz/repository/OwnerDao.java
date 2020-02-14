package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.Owner;

public interface OwnerDao extends JpaRepository<Owner, Integer>{

	public Owner findByUserName(String userName);
	
}
