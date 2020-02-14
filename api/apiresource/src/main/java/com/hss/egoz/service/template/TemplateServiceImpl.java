package com.hss.egoz.service.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.bean.InvoiceBean;
import com.hss.egoz.constants.App;
import com.hss.egoz.model.Trip;
import com.hss.egoz.service.util.DataService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
/*
 * @author Satyam Pandey Service to get HTML Templates for different Purpose...
 */
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private DataService dataService;

	/*
	 * @Param
	 * 
	 * 1. ftlName, name of the ftl file in the declared directory 2. data a model
	 * containing data to be retrieved in template
	 * 
	 * @Return html code of the template generated with the data as String...
	 */
	@Override
	public String getHtmlFromFtl(String ftlName, Object data) {

		// Create a StringWriter object for stream to write html data
		StringWriter dataBox = new StringWriter();
		try {

			// Get Configuration Object
			Configuration config = new Configuration(Configuration.VERSION_2_3_29);

			// Set Directory where templates will be stored..
			config.setDirectoryForTemplateLoading(new File(App.ROOT_DIRECTORY + "/templates"));

			// Get the Tamplate object to directory
			Template template = config.getTemplate(ftlName);

			// Process the data to the stringwriter
			template.process(data, dataBox);

			// return created HTML code as String...
			return dataBox.toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
		return dataBox.toString();
	}

	// service to get a template for registration mail
	@Override
	public String getRegistrationTemplate(String fullName, String roleName, String eMail) {
		Map<String, String> data = new HashMap<>();
		data.put("firstName", dataService.breakString(fullName, " ").get(0));
		data.put("roleName", roleName);
		String html = this.getHtmlFromFtl("registrationMail.ftlh", data);
		return html;
	}

	// service to get a template for apology on rejected trip
	@Override
	public String getApologyTemplate(Trip trip) {
		String ftlName = "apologyMail.ftlh";
		Map<String, String> data = new HashMap<>();
		data.put("firstName", dataService.breakString(trip.getUser().getFullName(), " ").get(0));
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		data.put("tripDate", df.format(trip.getStartDate()));
		return this.getHtmlFromFtl(ftlName, data);
	}

	// service to get a template for notifying owner about new booked trip
	@Override
	public String getTripNotificationTemplate(Trip trip) {
		Map<String, String> data = new HashMap<>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		data.put("startDate", df.format(trip.getStartDate()));
		data.put("firstName", dataService.breakString(trip.getVehicle().getOwner().getFullName(), " ").get(0));
		data.put("vehicleName", trip.getVehicle().getVehicleName());
		data.put("endDate", df.format(trip.getEndDate()));
		data.put("userFullName", trip.getUser().getFullName());
		data.put("userPhone", trip.getUser().getPhone());
		return this.getHtmlFromFtl("tripNotificationMail.ftlh", data);
	}

	// service to get a template for invoice description mail
	@Override
	public String getCustomerInvoice(Trip trip) {
		Map<String, String> data = new HashMap<>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		data.put("mode", "Invoice");
		data.put("status", "recent");
		data.put("startDate", df.format(trip.getStartDate()));
		data.put("firstName", dataService.breakString(trip.getUser().getFullName(), " ").get(0));
		String html = this.getHtmlFromFtl("invoiceContent.ftlh", data);
		return html;
	}

	// service to get a template for profarma description mail
	@Override
	public String getCustomerProfarma(Trip trip) {
		Map<String, String> data = new HashMap<>();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		data.put("mode", "Profarma");
		data.put("status", "Upcoming");
		data.put("startDate", df.format(trip.getStartDate()));
		data.put("firstName", dataService.breakString(trip.getUser().getFullName(), " ").get(0));
		String html = this.getHtmlFromFtl("invoiceContent.ftlh", data);
		return html;
	}

	// service to get a template for mail verification
	@Override
	public String getOTPTemplate(String fullName, String eMail, String otp) {
		Map<String, String> data = new HashMap<>();
		data.put("firstName", dataService.breakString(fullName, " ").get(0));
		data.put("otp", otp);
		return this.getHtmlFromFtl("otpMail.ftlh", data);
	}

	// service to get a template for invoice
	@Override
	public String getInvoiceHtml(Trip trip, InvoiceBean bean) {

		// Create a Map model to be sent to template for generation...
		Map<String, String> input = this.getInvoiceModel(trip, bean);

		// Get HTML for invoice...
		String html = this.getHtmlFromFtl("invoice.ftlh", input);
		return html;
	}

	// service to get a template for profarma
	@Override
	public String getProfarmaHtml(Trip trip, InvoiceBean bean) {

		// Create a Map model to be sent to template for generation...
		Map<String, String> input = this.getInvoiceModel(trip, bean);

		// Get HTML for invoice...
		String html = this.getHtmlFromFtl("profarma.ftlh", input);
		return html;
	}

	// get map model of data to be used while creating an invoice in freemarker
	private Map<String, String> getInvoiceModel(Trip trip, InvoiceBean bean) {
		Map<String, String> input = new HashMap<String, String>();
		input.put("logo", App.LOGO);
		input.put("pickUpAddress", trip.getPickUpAddress());
		input.put("dropAddress", trip.getDropAddress());
		input.put("tripId", String.valueOf(trip.getTripId()));
		input.put("currentDate", new SimpleDateFormat("dd/MM/YYYY hh:mm aa").format(new Date()));
		input.put("tripDate", new SimpleDateFormat("dd/MM/YYYY").format(trip.getStartDate()));
		input.put("vehicleNumber", trip.getVehicle().getVehicleNumber());
		input.put("fuelCharge", String.valueOf(dataService.format(bean.fuelCharge)));
		input.put("fuelUnit", String.valueOf(dataService.format(bean.fuelUnit)));
		input.put("reservationUnitCharge", String.valueOf(dataService.format(bean.reservationUnitCharge)));
		input.put("reservationCharge", String.valueOf(dataService.format(bean.reservationCharge)));
		input.put("reservationUnit", String.valueOf(bean.reservationUnit));
		input.put("kmsUnitCharge", String.valueOf(dataService.format(bean.kmsUnitCharge)));
		input.put("kmsUnit", String.valueOf(dataService.format(bean.kmsUnit)));
		input.put("kmsCharge", String.valueOf(dataService.format(bean.kmsCharge)));
		input.put("ninePercent", String.valueOf(dataService.format(bean.totalCharge * 9 / 100)));
		input.put("eighteenPercent", String.valueOf(dataService.format(bean.totalCharge * 18 / 100)));
		input.put("subTotal", String.valueOf(dataService.format(bean.totalCharge + (bean.totalCharge * 18 / 100))));
		input.put("totalCharge", String.valueOf(dataService.format(bean.totalCharge)));
		return input;
	}

}
