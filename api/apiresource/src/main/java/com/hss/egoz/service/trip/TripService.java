package com.hss.egoz.service.trip;

import java.util.Date;
import java.util.List;

import com.hss.egoz.model.Trip;
import com.hss.egoz.model.Vehicle;

public interface TripService {

	List<Trip> listAll();

	List<Trip> filterForDate(Date startDate, Date endDate);

	Integer insertTrip(Trip trip);

	List<Vehicle> filterVehiclesForDate(Date startDate, Date endDate, Integer classId);

	Trip getTrip(Integer tripId);

	void updateTrip(Trip t);

}
