package com.hss.egoz.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.hss.egoz.model.payments.PayUModel;

@Entity
public class PayUPayments {

	@Id
	@SequenceGenerator(name = "payU_seq", allocationSize = 1, initialValue = 1, sequenceName = "PAYU_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payU_seq")
	private Integer id;

	private String txnId, amount, firstName, mail, phone, productInfo, paymentStatus, successToken, successUrl, failureUrl;

	private Date dateTime;

	public PayUPayments() {

	}

	public PayUPayments(PayUModel model, String successToken) {
		this.txnId = model.getTxnid();
		this.amount = model.getAmount();
		this.firstName = model.getFirstname();
		this.mail = model.getEmail();
		this.phone = model.getPhone();
		this.productInfo = model.getProductinfo();
		this.dateTime = new Date();
		this.successToken = successToken;
		this.successUrl=model.getSurl();
		this.failureUrl=model.getFurl();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getSuccessToken() {
		return successToken;
	}

	public void setSuccessToken(String successToken) {
		this.successToken = successToken;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
