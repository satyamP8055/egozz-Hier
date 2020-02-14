package com.hss.egoz.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hss.egoz.model.Trip;

public interface TripDao extends JpaRepository<Trip, Integer>{

	@Query("select t from Trip t where t.startDate between :startDate and :endDate or t.endDate between  :startDate and :endDate")
	public List<Trip> filterByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
}
