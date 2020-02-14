package com.hss.egoz.bean;

import org.springframework.transaction.annotation.Transactional;

/*
 * @author Satyam Pandey
 * Transactional bean to communicate between Services to create Invoice
 * */
@Transactional
public class InvoiceBean {

	public double kmsCharge;
	public double kmsUnitCharge;
	public double kmsUnit;
	public double reservationCharge;
	public int reservationUnit;
	public double reservationUnitCharge;
	public double fuelCharge;
	public String fuelUnitCharge;
	public double fuelUnit;
	public double totalCharge;
	public double mileage;
}
