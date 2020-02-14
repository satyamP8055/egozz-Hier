package com.hss.egoz.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class DataFile {

	@Id
	@SequenceGenerator(name="file_seq", allocationSize = 1, initialValue = 1, sequenceName = "FILE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
	private Integer fileId;
		
	private String fileLocation;

	@JsonIgnore
	private Integer vehicleId;

	@JsonIgnore
	private Integer vehicleClassId;

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Integer getVehicleClassId() {
		return vehicleClassId;
	}

	public void setVehicleClassId(Integer vehicleClassId) {
		this.vehicleClassId = vehicleClassId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
}
