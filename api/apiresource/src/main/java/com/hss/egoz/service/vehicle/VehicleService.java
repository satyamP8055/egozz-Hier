package com.hss.egoz.service.vehicle;

import java.util.List;

import com.hss.egoz.model.Vehicle;
import com.hss.egoz.model.VehicleClass;

public interface VehicleService {

	public Integer addVehicle(Vehicle vehicle);

	List<VehicleClass> getClassList();

	VehicleClass getVehicleClass(Integer id);
	
	List<Vehicle> getVehicleList();
	
	public Vehicle get(Integer id);
	
	public void deleteVehicle(Vehicle vehicle);

	void updateVehicle(Vehicle vehicle);
	
}
