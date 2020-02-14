package com.hss.egoz.service.payment;

import com.hss.egoz.model.PayUPayments;

public interface PayUService {

	String getHashString(String txnid, String key, String salt, String hashSequence);

	Integer save(PayUPayments payment);

	void update(Integer id, String paymentStatus);

	PayUPayments getModel(Integer id);

	void confirm(Integer id, String paymentStatus, String txnId);

}
