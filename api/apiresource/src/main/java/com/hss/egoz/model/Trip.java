package com.hss.egoz.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Trip {

	@Id
	@SequenceGenerator(name="trip_seq", allocationSize = 1, initialValue = 1, sequenceName = "TRIP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_seq")
	private Integer tripId;
	
	@ManyToOne
	@JoinColumn(name="vehicle")
	private Vehicle vehicle;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	private String userRole;
	
	private String pickUpAddress;
	
	private String dropAddress;
	
	private Double distance;
	
	private Date startDate;
	
	private Date endDate;
	
	private Boolean kmsBased;
	
	public Boolean getKmsBased() {
		return kmsBased;
	}

	public void setKmsBased(Boolean kmsBased) {
		this.kmsBased = kmsBased;
	}

	@ManyToOne
	@JoinColumn(name="vehicleClassId")
	private VehicleClass vehicleClass;
	
	private Boolean ac;
	
	private Double userReviewStars;
	
	private String userReviewMessage;
	
	private Integer tripStatus;
	
	private Double tripCharge;
	
	private Boolean egozzTrip;

	public Integer getTripId() {
		return tripId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getPickUpAddress() {
		return pickUpAddress;
	}

	public void setPickUpAddress(String pickUpAddress) {
		this.pickUpAddress = pickUpAddress;
	}

	public String getDropAddress() {
		return dropAddress;
	}

	public void setDropAddress(String dropAddress) {
		this.dropAddress = dropAddress;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public VehicleClass getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(VehicleClass vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public Boolean getAc() {
		return ac;
	}

	public void setAc(Boolean ac) {
		this.ac = ac;
	}

	public Double getUserReviewStars() {
		return userReviewStars;
	}

	public void setUserReviewStars(Double userReviewStars) {
		this.userReviewStars = userReviewStars;
	}

	public String getUserReviewMessage() {
		return userReviewMessage;
	}

	public void setUserReviewMessage(String userReviewMessage) {
		this.userReviewMessage = userReviewMessage;
	}

	public Integer getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(Integer tripStatus) {
		this.tripStatus = tripStatus;
	}

	public Double getTripCharge() {
		return tripCharge;
	}

	public void setTripCharge(Double tripCharge) {
		this.tripCharge = tripCharge;
	}

	public Boolean getEgozzTrip() {
		return egozzTrip;
	}

	public void setEgozzTrip(Boolean egozzTrip) {
		this.egozzTrip = egozzTrip;
	}
	
}
