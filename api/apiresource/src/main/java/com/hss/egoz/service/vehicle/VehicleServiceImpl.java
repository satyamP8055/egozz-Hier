package com.hss.egoz.service.vehicle;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hss.egoz.config.Initiator;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.model.VehicleClass;
import com.hss.egoz.repository.VehicleClassDao;
import com.hss.egoz.repository.VehicleDao;

/*
 * @author Satyam Pandey
 * Service for vehicle related operations
 * */
@Service
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private VehicleDao vehicleDao;

	@Autowired
	private VehicleClassDao vehicleClassDao;

	@PostConstruct
	public void init() {
		if (vehicleClassDao.findAll().size() <= 0) {

			// Get Fundamental Classes & traverse through it
			Initiator.getFundamentalClasses().forEach(vehicleClass -> {

				// Add class to DB
				vehicleClassDao.save(vehicleClass);
			});
		}
	}

	// Add Vehicle Service
	@Override
	public Integer addVehicle(Vehicle vehicle) {
		Integer vehicleId = -1;
		vehicleId = vehicleDao.save(vehicle).getVehicleId();
		return vehicleId;
	}

	// Service to update vehicle
	@Override
	public void updateVehicle(Vehicle vehicle) {
		Vehicle v = vehicleDao.findById(vehicle.getVehicleId()).get();
		v.copyVehicle(vehicle);
		vehicleDao.save(v);
	}

	// Service to get classlist
	@Override
	public List<VehicleClass> getClassList() {
		return vehicleClassDao.findAll();
	}

	// Service to fetch vehicle class by vehicle class id
	@Override
	public VehicleClass getVehicleClass(Integer id) {
		return vehicleClassDao.findById(id).get();
	}

	// Service to get vehicle list
	@Override
	public List<Vehicle> getVehicleList() {
		return vehicleDao.findAll();
	}

	// Service to get vehicle by vehicle id
	@Override
	public Vehicle get(Integer id) {
		return vehicleDao.findById(id).get();
	}

	// Service to delete vehicle
	@Override
	public void deleteVehicle(Vehicle vehicle) {
		try {
			vehicleDao.delete(vehicle);
		} catch (DataIntegrityViolationException ex) {

			/*
			 * DataIntegrityViolationException occurs means vehicle has been engaged to some
			 * trips and can't be removed from DB Just update the status to DeActived for
			 * the case
			 */
			Vehicle v = vehicleDao.findById(vehicle.getVehicleId()).get();
			v.setVehicleStatus("DeActivated");
			vehicleDao.save(v);
		}
	}
}
