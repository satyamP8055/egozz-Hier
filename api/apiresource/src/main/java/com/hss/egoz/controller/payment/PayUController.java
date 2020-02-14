package com.hss.egoz.controller.payment;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.constants.EncryptMode;
import com.hss.egoz.constants.Random;
import com.hss.egoz.constants.Secrets;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Owner;
import com.hss.egoz.model.PayUPayments;
import com.hss.egoz.model.payments.PayUModel;
import com.hss.egoz.service.owner.OwnerService;
import com.hss.egoz.service.payment.PayUService;
import com.hss.egoz.service.util.DataService;

@CrossOrigin
@RestController
@RequestMapping(Url.PAYU)
public class PayUController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private DataService dataService;

	@Autowired
	private PayUService payUService;

	@PostMapping(Url.PAY_LIVE)
	public ResponseEntity<EResponse<PayUModel>> getPaymentModel(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam String amount, @RequestParam String productInfo, @RequestParam String processUrl) {
		EResponse<PayUModel> base = new EResponse<>();
		if (tokenService.validateToken(tokenKey)) {
			PayUModel data = new PayUModel();
			data.setAmount(amount);
			data.setProductinfo(productInfo);
			Owner owner = ownerService.findById(tokenService.getMemberId(tokenKey));
			if (owner == null)
				base.invalidToken();
			else {
				data.setEmail(owner.getMail());
				data.setFirstname(dataService.breakString(owner.getFullName(), " ").get(0));
				data.setPhone(owner.getPhone());
				data.setFurl(processUrl + "?egozzKey=" + dataService.random("", Random.ALPHANUMERIC, 6));
				String successToken = dataService.random("", Random.MISCELLANEOUS, 7);
				data.setSurl(processUrl + "?egozzKey=" + successToken);
				Integer payULocalId = payUService.save(new PayUPayments(data, successToken));
				data.setFurl(Url.CURRENT_HOST + Url.PAYU + Url.REDIRECT + "?egozzKey="
						+ dataService.random("", Random.ALPHANUMERIC, 6) + "&payULocalId=" + payULocalId);
				data.setSurl(Url.CURRENT_HOST + Url.PAYU + Url.REDIRECT + "?egozzKey=" + successToken
						+ "&payULocalId=" + payULocalId);
				String currentUser = tokenKey + DataService.STRING_BREAKER + payULocalId;
				base.setToken(currentUser);
				base.setData(data);
				base.success("INITATED");
			}
		} else
			base.invalidToken();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	@PostMapping(Url.PAYU_CONFIRM)
	public ResponseEntity<EResponse<PayUModel>> confirmPayment(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			PayUModel payUModel) throws NoSuchAlgorithmException {
		EResponse<PayUModel> base = new EResponse<>();
		if (tokenService.validateToken(dataService.breakString(tokenKey, DataService.STRING_BREAKER).get(0))) {
			String transactionId = dataService.random("", Random.NUMERIC, 60);
			String txnid = dataService.encodeSha(transactionId, EncryptMode.SHA256).substring(0, 20);
			String hashstring = payUService.getHashString(txnid, payUModel.getKey(), payUModel.getSalt(),
					Secrets.PAYU_HASH_SEQUENCE);
			payUModel.setHashstring(hashstring);
			payUModel.setTxnid(txnid);
			System.out.println("HASHSTRING = " + hashstring);
			payUModel.setHash(dataService.encodeSha(hashstring, EncryptMode.SHA512));
			try {
				payUService.confirm(
						Integer.parseInt(dataService.breakString(tokenKey, DataService.STRING_BREAKER).get(1)),
						"CONFIRMED", payUModel.getTxnid());
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				base.invalidToken();
			}
			base.setData(payUModel);
			base.setToken(tokenKey);
			base.success("confirmed");
		} else
			base.invalidToken();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	@GetMapping(Url.VERIFY)
	public ResponseEntity<EResponse<PayUModel>> verifyPayment(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam String paymentToken) throws NoSuchAlgorithmException {
		EResponse<PayUModel> base = new EResponse<>();
		try {
			if (tokenService.validateToken(dataService.breakString(tokenKey, DataService.STRING_BREAKER).get(0))) {
				PayUPayments payment = payUService.getModel(
						Integer.parseInt(dataService.breakString(tokenKey, DataService.STRING_BREAKER).get(1)));
				if (payment.getSuccessToken().equals(paymentToken)) {
					payUService.update(payment.getId(), "PAID");
					// operation on successfull payment
					base.success("Payment Received");
				} else {
					payUService.update(payment.getId(), "FAILED");
					// operation on payment failure
					base.fail("Transaction Failed");
				}
				tokenKey = dataService.breakString(tokenKey, DataService.STRING_BREAKER).get(0);
				base.setToken(tokenKey);
			} else
				base.invalidToken();
		} catch (ArrayIndexOutOfBoundsException e) {
			base.invalidToken();
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	@RequestMapping(value = Url.REDIRECT, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<String> processPayment(HttpServletRequest request) throws URISyntaxException {
		System.out.println(dataService.params(request));
		Integer payULocalId = Integer.parseInt(request.getParameter("payULocalId"));
		PayUPayments local = payUService.getModel(payULocalId);
		String redirectUrl = request.getParameter("egozzKey").equals(local.getSuccessToken()) ? local.getSuccessUrl()
				: local.getFailureUrl();
		URI uri = new URI(redirectUrl);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(uri);
		return new ResponseEntity<>(header, HttpStatus.SEE_OTHER);
	}

}
