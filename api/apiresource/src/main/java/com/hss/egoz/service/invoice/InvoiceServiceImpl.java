package com.hss.egoz.service.invoice;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.bean.InvoiceBean;
import com.hss.egoz.constants.MailBox;
import com.hss.egoz.model.Trip;
import com.hss.egoz.service.files.FileService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.util.DataService;

/*
 * @author Satyam Pandey
 * Service to perform invoice based operations
 * */
@Service
public class InvoiceServiceImpl implements InvoiceService {

	private InvoiceBean bean;

	@PostConstruct
	public void init() {
		bean = new InvoiceBean();
	}

	@Autowired
	private DataService dataService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MailService mailService;

	@Autowired
	private FileService fileService;

	/*
	 * @Param Trip
	 */
	@Override
	public void createInvoice(Trip trip) throws Exception {

		// Get Days difference between start & end date of trip
		long difference = trip.getEndDate().getTime() - trip.getStartDate().getTime();
		bean.reservationUnit = (int) (difference / (1000 * 60 * 60 * 24)) + 1;
		bean.kmsUnit = trip.getDistance();

		// Get average per day travelled
		double kmSubject = bean.kmsUnit / bean.reservationUnit;
		bean.mileage = trip.getAc() ? trip.getVehicle().getAcMileage() : trip.getVehicle().getDefaultMileage();
		bean.fuelUnitCharge = dataService.getData("fuelRate") + " per Litre @ " + (bean.mileage);

		// Get Charges for Invoice Bean from the trip
		if (kmSubject > trip.getVehicle().getKmsSwitch()) {
			bean.kmsUnitCharge = dataService.format(
					trip.getAc() ? trip.getVehicle().getAcKmsCharge() : trip.getVehicle().getDefaultKmsCharge());
			bean.kmsCharge = dataService.format(bean.kmsUnitCharge * bean.kmsUnit);
			bean.totalCharge = bean.kmsCharge;
			bean.reservationCharge = 0;
			bean.reservationUnitCharge = 0;
			bean.fuelCharge = 0;
			bean.fuelUnit = 0;
		} else {
			bean.kmsUnitCharge = 0;
			bean.kmsCharge = 0;
			bean.fuelUnit = dataService.format(bean.kmsUnit / bean.mileage);
			bean.fuelCharge = bean.fuelUnit * Double.parseDouble(dataService.getData("fuelRate"));
			bean.reservationUnitCharge = trip.getAc() ? trip.getVehicle().getAcDayCharge()
					: trip.getVehicle().getDefaultDayCharge();
			bean.reservationCharge = bean.reservationUnitCharge * bean.reservationUnit;
			bean.totalCharge = bean.reservationCharge + bean.fuelCharge;
		}

		// Generate the HTML file...
		String htmlFile = fileService.writeHtml("Invoices/" + trip.getTripId(),
				templateService.getInvoiceHtml(trip, bean), true);

		// Convert html to pdf...
		String pdfFile = fileService.htmlToPdf(htmlFile, "Invoices/" + trip.getTripId() + "/invoice.pdf", "Invoice",
				true);

		// Delete the HTML file as it is not needed anymore
		new File(htmlFile).delete();

		// Send the invoice to customer
		mailService.send(trip.getUser().getMail(),
				"Invoice for Your Trip dated " + new SimpleDateFormat("dd/MM/yyyy").format(trip.getStartDate()),
				templateService.getCustomerInvoice(trip), pdfFile, MailBox.ATTACHMENT);
	}

	// @Param Trip
	@Override
	public String createProfarma(Trip trip) throws Exception {

		// Get Days difference between start & end date of trip
		long difference = trip.getEndDate().getTime() - trip.getStartDate().getTime();
		bean.reservationUnit = (int) (difference / (1000 * 60 * 60 * 24)) + 1;
		bean.kmsUnit = trip.getDistance();

		// Get average per day travelled
		double kmSubject = bean.kmsUnit / bean.reservationUnit;
		bean.mileage = trip.getAc() ? trip.getVehicle().getAcMileage() : trip.getVehicle().getDefaultMileage();
		bean.fuelUnitCharge = dataService.getData("fuelRate") + " per Litre @ " + (bean.mileage);

		// Get Charges for Invoice Bean from the trip
		if (kmSubject > trip.getVehicle().getKmsSwitch()) {
			bean.kmsUnitCharge = dataService.format(
					trip.getAc() ? trip.getVehicle().getAcKmsCharge() : trip.getVehicle().getDefaultKmsCharge());
			bean.kmsCharge = dataService.format(bean.kmsUnitCharge * bean.kmsUnit);
			bean.totalCharge = bean.kmsCharge;
			bean.reservationCharge = 0;
			bean.reservationUnitCharge = 0;
			bean.fuelCharge = 0;
			bean.fuelUnit = 0;
		} else {
			bean.kmsUnitCharge = 0;
			bean.kmsCharge = 0;
			bean.fuelUnit = dataService.format(bean.kmsUnit / bean.mileage);
			bean.fuelCharge = bean.fuelUnit * Double.parseDouble(dataService.getData("fuelRate"));
			bean.reservationUnitCharge = trip.getAc() ? trip.getVehicle().getAcDayCharge()
					: trip.getVehicle().getDefaultDayCharge();
			bean.reservationCharge = bean.reservationUnitCharge * bean.reservationUnit;
			bean.totalCharge = bean.reservationCharge + bean.fuelCharge;
		}

		// Generate the HTML file...
		String htmlFile = fileService.writeHtml("Invoices/" + trip.getTripId(),
				templateService.getProfarmaHtml(trip, bean), true);

		// Convert PDF to HTML..
		String pdfFile = fileService.htmlToPdf(htmlFile, "Invoices/" + trip.getTripId() + "/profarma.pdf", "Profarma",
				true);

		// Delete the HTML as its no more needed
		new File(htmlFile).delete();

		// Send the profarma to customer
		mailService.send(trip.getUser().getMail(), "Profarma for upcoming trip",
				templateService.getCustomerProfarma(trip), pdfFile, MailBox.ATTACHMENT);

		return pdfFile;
	}

}
