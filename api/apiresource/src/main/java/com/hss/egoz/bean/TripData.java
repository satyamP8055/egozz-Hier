package com.hss.egoz.bean;

import org.springframework.transaction.annotation.Transactional;

/*
 * @author Satyam Pandey
 * Transactional bean to communicate in request for Statistics
 * */
@Transactional
public class TripData {
	
	private int total;

	private int completed;

	private int upcoming;

	private int cancelled;

	private int rejected;

	private int ongoing;
	
	public int getOngoing() {
		return ongoing;
	}

	public void setOngoing(int ongoing) {
		this.ongoing = ongoing;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getUpcoming() {
		return upcoming;
	}

	public void setUpcoming(int upcoming) {
		this.upcoming = upcoming;
	}

	public int getCancelled() {
		return cancelled;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	public int getRejected() {
		return rejected;
	}

	public void setRejected(int rejected) {
		this.rejected = rejected;
	}
	
}
