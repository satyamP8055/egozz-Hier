package com.hss.egoz.service.invoice;

import com.hss.egoz.model.Trip;

public interface InvoiceService {

	void createInvoice(Trip trip) throws Exception;

	String createProfarma(Trip trip) throws Exception;

}
