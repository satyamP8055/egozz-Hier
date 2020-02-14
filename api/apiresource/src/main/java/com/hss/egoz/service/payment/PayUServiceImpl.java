package com.hss.egoz.service.payment;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.model.PayUPayments;
import com.hss.egoz.repository.PayURepository;
import com.hss.egoz.service.util.DataService;

@Service
public class PayUServiceImpl implements PayUService {

	@Autowired
	private DataService dataService;

	@Autowired
	private PayURepository payURepository;

	@Override
	public String getHashString(String txnid, String key, String salt, String hashSequence) {

		// Get HttpServletRequest...
		HttpServletRequest request = dataService.getRequest();

		// Break hashSequence to array of elements
		String[] hashVarSeq = hashSequence.split("\\|");

		// Iniitialise hashString with key
		String hashString = key + "|";

		// Get Paramerter Names of the parameters received with request
		Enumeration<String> paramNames = request.getParameterNames();

		// create an empty hashmap & store request parameters as key value pairs...
		Map<String, String> params = new HashMap<String, String>();
		while (paramNames.hasMoreElements()) {
			String name = (String) paramNames.nextElement();
			params.put(name, request.getParameter(name));
		}

		// add each map pair to hashString following the hashSequence
		for (String part : hashVarSeq) {
			if (part.equals("txnid")) {
				hashString = hashString + txnid;
			} else {
				hashString = params.get(part) == null ? hashString.concat("")
						: hashString.concat(params.get(part).trim());
			}
			hashString = hashString.concat("|");
		}

		// Add Salt to hash
		hashString = hashString.concat(salt);

		return hashString;
	}

	@Override
	public Integer save(PayUPayments payment) {
		payment.setPaymentStatus("INITIATED");
		return payURepository.save(payment).getId();
	}

	@Override
	public void confirm(Integer id, String paymentStatus, String txnId) {
		PayUPayments payment = payURepository.findById(id).get();
		payment.setDateTime(new Date());
		payment.setTxnId(txnId);
		payment.setPaymentStatus(paymentStatus);
		payURepository.save(payment);
	}

	@Override
	public void update(Integer id, String paymentStatus) {
		PayUPayments payment = payURepository.findById(id).get();
		payment.setDateTime(new Date());
		payment.setPaymentStatus(paymentStatus);
		payURepository.save(payment);
	}

	@Override
	public PayUPayments getModel(Integer id) {
		return payURepository.findById(id).get();
	}

}
