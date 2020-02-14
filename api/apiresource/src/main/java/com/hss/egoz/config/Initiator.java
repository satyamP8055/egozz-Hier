package com.hss.egoz.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hss.egoz.model.Access;
import com.hss.egoz.model.Admin;
import com.hss.egoz.model.DataFile;
import com.hss.egoz.model.Role;
import com.hss.egoz.model.VehicleClass;

/*
 * @author Satyam Pandey
 * Interface to get fundamental data in case db is not having one...
 * */
public interface Initiator {

	// Method to get array of fundamental vehicle classes
	public static List<VehicleClass> getFundamentalClasses() {

		List<VehicleClass> list = new ArrayList<VehicleClass>();
		String[] classNames = { "HatchBack", "HatchBack Prime", "Sedan", "Sedan Prime", "SUV", "SUV Prime", "Economy" };
		String[] imageNames = { "hatchback.jpg", "hatchback-prime.jpg", "sedan.png", "Sedan_prime.png", "SUV.jpg ",
				"suv_prime.jpg", "Economy.jpg" };
		String imageHost = "vehicleClass/";
		Integer[] seats = { 4, 4, 4, 4, 6, 7, 8 };
		Boolean[] acSwitches = { true, false, true, false, true, false, true };
		Double[] minAcKmsCharges = { 5.0, 5.0, 6.0, 6.0, 8.0, 9.0, 9.0 };
		Double[] minAcDayCharges = { 400.0, 400.0, 450.0, 450.0, 600.0, 700.0, 700.0 };
		Double[] minAcMileages = { 7.0, 7.0, 7.0, 7.0, 5.0, 5.0, 6.0 };
		Double[] minDefaultKmsCharges = { 5.0, 5.0, 6.0, 6.0, 7.0, 8.0, 8.0 };
		Double[] minDefaultDayCharges = { 300.0, 350.0, 350.0, 400.0, 500.0, 600.0, 600.0 };
		Double[] minDefaultMileages = { 8.0, 8.0, 8.0, 8.0, 7.0, 7.0, 8.0 };
		Double[] maxAcKmsCharges = { 10.0, 10.0, 10.0, 12.0, 12.0, 15.0, 13.0 };
		Double[] maxAcDayCharges = { 500.0, 600.0, 500.0, 700.0, 1000.0, 1200.0, 1000.0 };
		Double[] maxAcMileages = { 8.0, 8.0, 8.0, 8.0, 7.0, 7.0, 8.0 };
		Double[] maxDefaultKmsCharges = { 9.0, 9.0, 9.0, 11.0, 11.0, 14.0, 12.0 };
		Double[] maxDefaultDayCharges = { 400.0, 500.0, 450.0, 600.0, 950.0, 1100.0, 900.0 };
		Double[] maxDefaultMileages = { 10.0, 10.0, 10.0, 10.0, 8.0, 8.0, 8.0 };
		Double[] appDayIncentives = { 100.0, 100.0, 100.0, 100.0, 150.0, 150.0, 150.0 };
		Double[] travelDayIncentives = { 50.0, 50.0, 50.0, 50.0, 50.0, 80.0, 50.0 };
		Double[] appKmsIncentives = { 1.5, 1.5, 1.5, 2.0, 2.0, 2.0, 2.0 };
		Double[] travelKmsIncentives = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };

		// Get Vehicle Class Object & add to Collection...
		for (int i = 0; i < classNames.length; i++) {
			VehicleClass vehicleClass = new VehicleClass();
			vehicleClass.setAcSwitch(acSwitches[i]);
			vehicleClass.setAppDayIncentive(appDayIncentives[i]);
			vehicleClass.setAppKmsIncentive(appKmsIncentives[i]);
			vehicleClass.setClassName(classNames[i]);
			vehicleClass.setMaxAcDayCharge(maxAcDayCharges[i]);
			vehicleClass.setMaxAcKmsCharge(maxAcKmsCharges[i]);
			vehicleClass.setMaxAcMileage(maxAcMileages[i]);
			vehicleClass.setMaxDefaultDayCharge(maxDefaultDayCharges[i]);
			vehicleClass.setMaxDefaultKmsCharge(maxDefaultKmsCharges[i]);
			vehicleClass.setMaxDefaultMileage(maxDefaultMileages[i]);
			vehicleClass.setMinAcDayCharge(minAcDayCharges[i]);
			vehicleClass.setMinAcKmsCharge(minAcKmsCharges[i]);
			vehicleClass.setMinAcMileage(minAcMileages[i]);
			vehicleClass.setMinAcMileage(minAcMileages[i]);
			vehicleClass.setMinDefaultDayCharge(minDefaultDayCharges[i]);
			vehicleClass.setMinDefaultKmsCharge(minDefaultKmsCharges[i]);
			vehicleClass.setMinDefaultMileage(minDefaultMileages[i]);
			vehicleClass.setSeat(seats[i]);
			vehicleClass.setTravelDayIncentive(travelDayIncentives[i]);
			vehicleClass.setTravelKmsIncentive(travelKmsIncentives[i]);
			DataFile image = new DataFile();
			image.setFileLocation(imageHost + imageNames[i]);
			vehicleClass.setImage(image);
			list.add(vehicleClass);
		}

		// Free up used heap memory
		classNames = imageNames = null;
		imageHost = null;
		seats = null;
		acSwitches = null;
		minAcKmsCharges = minAcDayCharges = minAcMileages = minDefaultKmsCharges = minDefaultDayCharges = null;
		minDefaultMileages = maxAcKmsCharges = maxAcDayCharges = maxAcMileages = maxDefaultKmsCharges = null;
		maxDefaultDayCharges = maxDefaultMileages = appDayIncentives = travelDayIncentives = appKmsIncentives = null;
		travelKmsIncentives = null;
		
		return list;
	}

	// Method to get first fundamental Role to operate on admin purpose...
	public static Role getFundaMentalRole() {
		Role role=new Role();
		role.setRoleName("Admin");
		return role;
	}
	
	// Method to get Mandatory Access for Super Admin...
	public static List<Access> getFundamentalAccess(){
		List<Access> access=new ArrayList<Access>();
		access.add(new Access("Vehicle","vehicles"));
		access.add(new Access("Trips","trips"));
		access.add(new Access("Transaction","transaction"));
		access.add(new Access("User","users"));
		access.add(new Access("Owner","owners"));
		return access;
	}
	
	// Method to get Super Admin Details...
	public static Admin getSuperAdmin() {
		Admin admin=new Admin();
		admin.setFullName("Satyam Pandey");
		admin.setMail("saty8055@gmail.com");
		admin.setPassword(new BCryptPasswordEncoder().encode("allow"));
		admin.setUserName("saty8055");
		return admin;
	}
	
}
