package com.hss.egoz.service.trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.model.Trip;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.repository.TripDao;

/*
 * @author Satyam Pandey
 * Service to perform trip operations
 * */
@Service
public class TripServiceImpl implements TripService {

	@Autowired
	private TripDao tripDao;

	// List all trips..
	@Override
	public List<Trip> listAll() {
		return tripDao.findAll();
	}

	// Filter trips for specific Date
	@Override
	public List<Trip> filterForDate(Date startDate, Date endDate) {
		return tripDao.filterByDate(startDate, endDate);
	}

	// Filter vehicles engaged in trips at specific date
	@Override
	public List<Vehicle> filterVehiclesForDate(Date startDate, Date endDate, Integer classId) {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		tripDao.filterByDate(startDate, endDate).forEach(trip -> {
			if (trip.getVehicle().getVehicleClass().getVehicleClassId().intValue() == classId
					&& trip.getTripStatus() > -2)
				vehicles.add(trip.getVehicle());
		});
		return vehicles;
	}

	// Insert a trip to DB
	@Override
	public Integer insertTrip(Trip trip) {
		return tripDao.save(trip).getTripId();
	}

	// get a trip by ID
	@Override
	public Trip getTrip(Integer tripId) {
		return tripDao.findById(tripId).get();
	}

	// Update trip
	@Override
	public void updateTrip(Trip t) {
		Trip trip = getTrip(t.getTripId());
		trip.setAc(t.getAc());
		trip.setDistance(t.getDistance());
		trip.setDropAddress(t.getDropAddress());
		trip.setEgozzTrip(t.getEgozzTrip());
		trip.setEndDate(t.getEndDate());
		trip.setPickUpAddress(t.getPickUpAddress());
		trip.setStartDate(t.getStartDate());
		trip.setTripCharge(t.getTripCharge());
		trip.setTripStatus(t.getTripStatus());
		trip.setUser(t.getUser());
		trip.setUserReviewMessage(t.getUserReviewMessage());
		trip.setUserRole(t.getUserRole());
		trip.setVehicle(t.getVehicle());
		trip.setVehicleClass(t.getVehicleClass());
		tripDao.save(trip);
	}

}
