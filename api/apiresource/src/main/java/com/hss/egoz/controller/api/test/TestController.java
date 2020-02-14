package com.hss.egoz.controller.api.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.constants.Url;

@RestController
@CrossOrigin
@RequestMapping(Url.TEST)
public class TestController {

	@GetMapping(Url.BLANK)
	public String test() {
		return "TEST SUCCESS 0110";
	}

}
