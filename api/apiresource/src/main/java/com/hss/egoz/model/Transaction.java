package com.hss.egoz.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Transaction {

	@Id
	@SequenceGenerator(name="payment_seq", allocationSize = 1, initialValue = 1, sequenceName = "PAYMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
	private Integer paymentId;

	@ManyToOne
	@JoinColumn(name="tripId")
	private Trip trip;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	@ManyToOne
	@JoinColumn(name="ownerId")
	private Owner owner;

	@ManyToOne
	@JoinColumn(name="adminId")
	private Admin admin;

	private Double amount;
	
	private String transactionType;
	
	private String userRole;
	
	private String remarks;
	
	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
