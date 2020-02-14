package com.hss.egoz.bean;

import org.springframework.transaction.annotation.Transactional;

/*
 * @author Satyam Pandey
 * Transactional bean to communicate in request for Statistics
 * */
@Transactional
public class TransactionData {

	private double deposit;

	private double withdrawl;

	private double pending;

	public double getPending() {
		return pending;
	}

	public void setPending(double pending) {
		this.pending = pending;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getWithdrawl() {
		return withdrawl;
	}

	public void setWithdrawl(double withdrawl) {
		this.withdrawl = withdrawl;
	}

}
