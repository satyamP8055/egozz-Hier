package com.hss.egoz.bean;

import org.springframework.transaction.annotation.Transactional;

/*
 * @author Satyam Pandey
 * Transactional bean to communicate in request for Statistics
 * */
@Transactional
public class VehicleIncome {

	private String vehicleName;
	
	public double income;

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}
	
}
