package com.hss.egoz.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class VehicleClass {

	@Id
	@SequenceGenerator(name = "vehicleClass_seq", allocationSize = 1, initialValue = 1, sequenceName = "VEHICLE_CLASS_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicleClass_seq")
	private Integer vehicleClassId;

	@Column(unique = true)
	private String className;

	private Boolean acSwitch;

	private Double minDefaultKmsCharge;

	private Double maxDefaultKmsCharge;

	private Double minDefaultDayCharge;

	private Double maxDefaultDayCharge;

	private Double minDefaultMileage;

	private Double maxDefaultMileage;

	private Double minAcKmsCharge;

	private Double maxAcKmsCharge;

	private Double minAcDayCharge;

	private Double maxAcDayCharge;

	private Double minAcMileage;

	private Double maxAcMileage;

	private Double travelDayIncentive;

	private Double appDayIncentive;

	private Double travelKmsIncentive;

	private Double appKmsIncentive;

	private Integer seat;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "dataFileId")
	private DataFile image;

	public DataFile getImage() {
		return image;
	}

	public void setImage(DataFile image) {
		this.image = image;
	}

	public Integer getVehicleClassId() {
		return vehicleClassId;
	}

	public void setVehicleClassId(Integer vehicleClassId) {
		this.vehicleClassId = vehicleClassId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getAcSwitch() {
		return acSwitch;
	}

	public void setAcSwitch(Boolean acSwitch) {
		this.acSwitch = acSwitch;
	}

	public Double getMinDefaultKmsCharge() {
		return minDefaultKmsCharge;
	}

	public void setMinDefaultKmsCharge(Double minDefaultKmsCharge) {
		this.minDefaultKmsCharge = minDefaultKmsCharge;
	}

	public Double getMaxDefaultKmsCharge() {
		return maxDefaultKmsCharge;
	}

	public void setMaxDefaultKmsCharge(Double maxDefaultKmsCharge) {
		this.maxDefaultKmsCharge = maxDefaultKmsCharge;
	}

	public Double getMinDefaultDayCharge() {
		return minDefaultDayCharge;
	}

	public void setMinDefaultDayCharge(Double minDefaultDayCharge) {
		this.minDefaultDayCharge = minDefaultDayCharge;
	}

	public Double getMaxDefaultDayCharge() {
		return maxDefaultDayCharge;
	}

	public void setMaxDefaultDayCharge(Double maxDefaultDayCharge) {
		this.maxDefaultDayCharge = maxDefaultDayCharge;
	}

	public Double getMinDefaultMileage() {
		return minDefaultMileage;
	}

	public void setMinDefaultMileage(Double minDefaultMileage) {
		this.minDefaultMileage = minDefaultMileage;
	}

	public Double getMaxDefaultMileage() {
		return maxDefaultMileage;
	}

	public void setMaxDefaultMileage(Double maxDefaultMileage) {
		this.maxDefaultMileage = maxDefaultMileage;
	}

	public Double getMinAcKmsCharge() {
		return minAcKmsCharge;
	}

	public void setMinAcKmsCharge(Double minAcKmsCharge) {
		this.minAcKmsCharge = minAcKmsCharge;
	}

	public Double getMaxAcKmsCharge() {
		return maxAcKmsCharge;
	}

	public void setMaxAcKmsCharge(Double maxAcKmsCharge) {
		this.maxAcKmsCharge = maxAcKmsCharge;
	}

	public Double getMinAcDayCharge() {
		return minAcDayCharge;
	}

	public void setMinAcDayCharge(Double minAcDayCharge) {
		this.minAcDayCharge = minAcDayCharge;
	}

	public Double getMaxAcDayCharge() {
		return maxAcDayCharge;
	}

	public void setMaxAcDayCharge(Double maxAcDayCharge) {
		this.maxAcDayCharge = maxAcDayCharge;
	}

	public Double getMinAcMileage() {
		return minAcMileage;
	}

	public void setMinAcMileage(Double minAcMileage) {
		this.minAcMileage = minAcMileage;
	}

	public Double getMaxAcMileage() {
		return maxAcMileage;
	}

	public void setMaxAcMileage(Double maxAcMileage) {
		this.maxAcMileage = maxAcMileage;
	}

	public Double getTravelDayIncentive() {
		return travelDayIncentive;
	}

	public void setTravelDayIncentive(Double travelDayIncentive) {
		this.travelDayIncentive = travelDayIncentive;
	}

	public Double getAppDayIncentive() {
		return appDayIncentive;
	}

	public void setAppDayIncentive(Double appDayIncentive) {
		this.appDayIncentive = appDayIncentive;
	}

	public Double getTravelKmsIncentive() {
		return travelKmsIncentive;
	}

	public void setTravelKmsIncentive(Double travelKmsIncentive) {
		this.travelKmsIncentive = travelKmsIncentive;
	}

	public Double getAppKmsIncentive() {
		return appKmsIncentive;
	}

	public void setAppKmsIncentive(Double appKmsIncentive) {
		this.appKmsIncentive = appKmsIncentive;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

}
