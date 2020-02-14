package com.hss.egoz.bean;

import org.springframework.transaction.annotation.Transactional;

/*
 * @author Satyam Pandey
 * Transactional bean to communicate in request for Statistics
 * */
@Transactional
public class VehicleTripData {

	private String vehicleName;
	
	private int trips;

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public int getTrips() {
		return trips;
	}

	public void setTrips(int trips) {
		this.trips = trips;
	}
	
}
