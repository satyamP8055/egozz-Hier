package com.hss.egoz.config;

import com.hss.egoz.constants.Constants;

/*
 * @author Satyam Pandey
 * A Basic Layout to be the pattern of response of each request handled by the App
 * 
 * @Return
 *  statusCode :
 *  	200: success
 *  	400: Failure
 *  	403: Unauthorized
 *  	500: Exception
 *  
 * 	status :
 * 		success
 * 		fail
 * 		unauthorized
 * 		exception
 *  message : passed by service or controller
 *  data : eiether null or passed by controller to be of required data types
 * */
public class EResponse<T> {

	private Integer statusCode;

	private String status, message;

	private String token;

	private T data;

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void success(String message) {
		this.statusCode = 200;
		this.status = Constants.success.toString();
		this.message = message;
	}

	public void fail(String message) {
		this.statusCode = 400;
		this.status = Constants.fail.toString();
		this.data = null;
		this.message = message;
	}

	public void exception(Exception ex) {
		try {
			this.message = Constants.internal_server_error.toString();
			this.statusCode = 500;
			this.status = Constants.exception.toString();
			this.data = null;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void invalidToken() {
		this.status = Constants.unauthorised.toString();
		this.statusCode = 403;
		this.data = null;
		this.message = Constants.unauthorised.toString();
	}

}
