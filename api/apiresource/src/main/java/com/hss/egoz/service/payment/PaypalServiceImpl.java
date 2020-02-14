package com.hss.egoz.service.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hss.egoz.constants.Secrets;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/*
 * @author Satyam Pandey
 * Service to perform paypal specific payment operations...
 * */
@Service
public class PaypalServiceImpl implements PaypalService{

	private final String clientId;
	private final String clientSecret;

	public PaypalServiceImpl() {
		clientId = Secrets.PAYPAL_CLIENT;
		clientSecret = Secrets.PAYPAL_SECRET;
	}

	/*
	 * @Param sum amount in the transaction typeCasted to string,
	 * returnUrl after payment is successful & cancelUrl if payment fails...
	 * 
	 * @Return Payment redirect url specifically with a status of success...
	 * */
	@Override
	public Map<String, Object> createPayment(String sum, String returnUrl, String cancelUrl){
		Map<String, Object> response= new HashMap<>();
		
		// Generate amount object..
		Amount amount=new Amount();
		amount.setCurrency("INR");
		amount.setTotal(sum);
		
		// Get transaction instance as List..
		Transaction transaction=new Transaction();
		transaction.setAmount(amount);
		List<Transaction> transactions=new ArrayList<Transaction>();
		transactions.add(transaction);
		
		//Get Payer instance..
		Payer payer=new Payer();
		payer.setPaymentMethod("paypal");
		
		// create payment object with all the data gathered
		Payment payment=new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		
		// set Redirect URLs to the payment object...
		RedirectUrls redirectUrls=new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(returnUrl);
		
		payment.setRedirectUrls(redirectUrls);
		Payment createdPayment;
		
		try {
			String redirectUrl="";
			
			// Get APIContext instance & create encryted payment instance...
			APIContext context= new APIContext(clientId, clientSecret, "sandbox");
			createdPayment=payment.create(context);
			
			if(createdPayment!=null) {
				List<Links> links=createdPayment.getLinks();
				for(Links link:links){
					if(link.getRel().contentEquals("approval_url")) {
						
						// get redirect link for the client to be redirected to..
						redirectUrl=link.getHref();
						break;
					}
				}
				response.put("status", "success");
				response.put("redirect_url", redirectUrl);
			}
		}
		catch(PayPalRESTException ex) {
			ex.printStackTrace();
		}
		
		return response;
	}

	/*
	 * @Param paymentId, & payerId
	 * 
	 * @Return Payment Object with all details of payments...
	 * */
	@Override
	public Map<String, Object> completePayment(String paymentId, String payerId){
		Map<String, Object> response= new HashMap<>();
		
		// get payment instance & set payment ID
		Payment payment= new Payment();
		payment.setId(paymentId);
		
		// get payment Execution object & set payer ID to execute & get the whole payment object
		PaymentExecution paymentExecution= new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		try {
			
			// Get APIContext instance..
			APIContext context= new APIContext(clientId, clientSecret, "sandbox");
			
			// get complete payment details by executing the details...
			Payment createdPayment=payment.execute(context, paymentExecution);
			
			if(createdPayment!=null) {
				response.put("status", "success");
				response.put("payment", createdPayment);
			}
		}
		catch(PayPalRESTException ex) {
			ex.printStackTrace();
		}
		
		return response;
	}
	
}
