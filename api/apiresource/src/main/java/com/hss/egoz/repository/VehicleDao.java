package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.Vehicle;

public interface VehicleDao extends JpaRepository<Vehicle, Integer> {

}
