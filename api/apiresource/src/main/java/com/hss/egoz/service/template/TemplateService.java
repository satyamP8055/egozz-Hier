package com.hss.egoz.service.template;

import com.hss.egoz.bean.InvoiceBean;
import com.hss.egoz.model.Trip;

public interface TemplateService {

	String getRegistrationTemplate(String fullName, String roleName, String eMail);

	String getOTPTemplate(String fullName, String eMail, String otp);

	String getApologyTemplate(Trip trip);

	String getTripNotificationTemplate(Trip trip);

	String getCustomerInvoice(Trip trip);

	String getCustomerProfarma(Trip trip);

	String getInvoiceHtml(Trip trip, InvoiceBean bean);

	String getProfarmaHtml(Trip trip, InvoiceBean bean);

	String getHtmlFromFtl(String ftlName, Object data);

}
