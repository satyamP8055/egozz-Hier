package com.hss.egoz.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@Entity
public class Vehicle {

	@Id
	@SequenceGenerator(name = "vehicle_seq", allocationSize = 1, initialValue = 1, sequenceName = "VEHICLE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_seq")
	private Integer vehicleId;

	private String vehicleName;

	@Column(unique = true)
	private String vehicleNumber;

	private String vehicleColor;

	@ManyToOne
	@JoinColumn(name = "vehicleClassId")
	private VehicleClass vehicleClass;

	private Double kmsSwitch;

	private Double acDayCharge;

	private Double acKmsCharge;

	private Double acMileage;

	private Double defaultDayCharge;

	private Double defaultKmsCharge;

	private Double defaultMileage;
	
	private Double latitude;
	
	private Double longitude;
	
	private String address;
	
	@ManyToOne
	@JoinColumn(name="ownerId")
	private Owner owner;
	
	@ManyToOne
	@JoinColumn(name="adminId")
	private Admin admin;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="vehicleId")
	private List<DataFile> images;
	
	private String vehicleRemarks;
	
	private String vehicleStatus;

	@Transient
	private Double charge;
	
	public Double getCharge() {
		return charge;
	}

	public void setCharge(Double charge) {
		this.charge = charge;
		
	}

	public String toString() {
		return vehicleId+", "+vehicleName+", "+vehicleNumber;
	}
	
	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleColor() {
		return vehicleColor;
	}

	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	public VehicleClass getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(VehicleClass vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public Double getKmsSwitch() {
		return kmsSwitch;
	}

	public void setKmsSwitch(Double kmsSwitch) {
		this.kmsSwitch = kmsSwitch;
	}

	public Double getAcDayCharge() {
		return acDayCharge;
	}

	public void setAcDayCharge(Double acDayCharge) {
		this.acDayCharge = acDayCharge;
	}

	public Double getAcKmsCharge() {
		return acKmsCharge;
	}

	public void setAcKmsCharge(Double acKmsCharge) {
		this.acKmsCharge = acKmsCharge;
	}

	public Double getAcMileage() {
		return acMileage;
	}

	public void setAcMileage(Double acMileage) {
		this.acMileage = acMileage;
	}

	public Double getDefaultDayCharge() {
		return defaultDayCharge;
	}

	public void setDefaultDayCharge(Double defaultDayCharge) {
		this.defaultDayCharge = defaultDayCharge;
	}

	public Double getDefaultKmsCharge() {
		return defaultKmsCharge;
	}

	public void setDefaultKmsCharge(Double defaultKmsCharge) {
		this.defaultKmsCharge = defaultKmsCharge;
	}

	public Double getDefaultMileage() {
		return defaultMileage;
	}

	public void setDefaultMileage(Double defaultMileage) {
		this.defaultMileage = defaultMileage;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<DataFile> getImages() {
		return images;
	}

	public void setImages(List<DataFile> images) {
		this.images = images;
	}

	public String getVehicleRemarks() {
		return vehicleRemarks;
	}

	public void setVehicleRemarks(String vehicleRemarks) {
		this.vehicleRemarks = vehicleRemarks;
	}

	public String getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}
	
	public void copyVehicle(Vehicle vehicle) {
		this.acDayCharge=vehicle.acDayCharge;
		this.acKmsCharge=vehicle.acKmsCharge;
		this.acMileage=vehicle.acMileage;
		this.defaultDayCharge=vehicle.defaultDayCharge;
		this.defaultKmsCharge=vehicle.defaultKmsCharge;
		this.defaultMileage=vehicle.defaultMileage;
		this.kmsSwitch=vehicle.kmsSwitch;
		this.vehicleColor=vehicle.vehicleColor;
		this.vehicleName=vehicle.vehicleName;
		this.vehicleNumber=vehicle.vehicleNumber;
		this.vehicleRemarks=vehicle.vehicleRemarks;
		this.vehicleStatus=vehicle.vehicleStatus;
		this.images=vehicle.images;
		this.latitude=vehicle.latitude;
		this.longitude=vehicle.longitude;
		this.address=vehicle.address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
