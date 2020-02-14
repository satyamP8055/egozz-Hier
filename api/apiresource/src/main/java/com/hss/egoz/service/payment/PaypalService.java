package com.hss.egoz.service.payment;

import java.util.Map;

public interface PaypalService {

	Map<String, Object> createPayment(String sum, String returnUrl, String cancelUrl);

	Map<String, Object> completePayment(String paymentId, String payerId);

}
