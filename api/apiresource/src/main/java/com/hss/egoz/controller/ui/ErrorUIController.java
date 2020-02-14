package com.hss.egoz.controller.ui;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @author Satyam Pandey
 * Controller to handle 404 Errors
 * */
@Controller
public class ErrorUIController implements ErrorController {

	// set default 404 to search for '/error' mapping
	@Override
	public String getErrorPath() {
		return "/error";
	}

	// map '/error' to index.html
	@RequestMapping("/error")
	public String handlerError() {
		return "index.html";
	}

}
