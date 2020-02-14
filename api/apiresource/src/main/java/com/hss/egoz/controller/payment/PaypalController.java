package com.hss.egoz.controller.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.constants.Url;
import com.hss.egoz.service.payment.PaypalService;

/*
 * @author Satyam Pandey
 * Controller to perform paypal payment requests...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.PAYPAL)
public class PaypalController {

	@Autowired
	private PaypalService paypalService;

	@Autowired
	private TokenService tokenService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Param sum : amount to be processed in transaction.
	 * 
	 * @Return data : Payment URL to which client is supposed to be redirected...
	 */
	@RequestMapping(value = Url.PAY_LIVE, method = RequestMethod.POST)
	public Map<String, Object> makePayment(@RequestParam String sum, @RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		String cancelUrl = Url.REMOTE_HOST + "/owner/dashboard/transactions";
		String returnUrl = Url.REMOTE_HOST + "/owner/dashboard/transactions/paid";
		if (tokenService.validateToken(tokenKey))
			return paypalService.createPayment(sum, returnUrl, cancelUrl);
		else
			return new HashMap<>();
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Param paymentId & payerId
	 * 
	 * @Return data : Payment Details
	 */
	@RequestMapping(value = Url.COMPLETE, method = RequestMethod.POST)
	public Map<String, Object> completePayment(@RequestParam String paymentId, @RequestParam String payerId,
			@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		if (tokenService.validateToken(tokenKey))
			return paypalService.completePayment(paymentId, payerId);
		else
			return new HashMap<>();
	}

}