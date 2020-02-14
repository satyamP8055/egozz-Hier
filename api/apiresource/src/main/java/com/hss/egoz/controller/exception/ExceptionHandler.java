package com.hss.egoz.controller.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.service.exception.ReporterService;

/*
 * @author Satyam Pandey
 * An Exception Handler to catch all the exceptions throughout the controllers & handle accordingly
 * */
@ControllerAdvice
public class ExceptionHandler {

	@Autowired
	private ReporterService reporterService;

	@org.springframework.web.bind.annotation.ExceptionHandler(value = InvalidTokenException.class)
	public ResponseEntity<EResponse<Boolean>> handle(InvalidTokenException exception) {
		EResponse<Boolean> response = new EResponse<>();
		response.invalidToken();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public ResponseEntity<EResponse<Boolean>> handle(Exception exception) {
		System.out.println("Handler Invoked");
		EResponse<Boolean> response = new EResponse<>();
		reporterService.report(exception);
		response.exception(exception);
		exception.printStackTrace();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

}
