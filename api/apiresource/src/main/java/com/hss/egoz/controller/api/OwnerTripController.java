package com.hss.egoz.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.constants.MailBox;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Transaction;
import com.hss.egoz.model.Trip;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.invoice.InvoiceService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.transaction.PaymentService;
import com.hss.egoz.service.trip.TripService;
import com.hss.egoz.service.util.DataService;

/*
 * @author Satyam Pandey
 * Controller to perform trip operations of Vehicle Owner...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.OWNER_TRIP)
public class OwnerTripController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private DataService dataService;

	@Autowired
	private TripService tripService;

	@Autowired
	private ReporterService reporterService;

	@Autowired
	private PaymentService transactionService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MailService mailService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : Trips of Owner's vehicles as JSON Array..
	 */
	@RequestMapping(value = Url.LIST, method = RequestMethod.GET)
	public ResponseEntity<EResponse<List<Trip>>> ownerTrips(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<List<Trip>> base = new EResponse<List<Trip>>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(tripService
						.listAll().stream().filter(trip -> (trip.getVehicle().getOwner().getOwnerId()
								.intValue() == tokenService.getMemberId(tokenKey).intValue()))
						.collect(Collectors.toList()));
				base.success("trips retrieved");
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Param tripId, statusId, & totalKms
	 * 
	 * @Return data : Updated List of Trips of Owner's vehicles as JSON Array..
	 */
	@RequestMapping(value = Url.TRIP_UPDATE, method = RequestMethod.PUT)
	public ResponseEntity<EResponse<List<Trip>>> updateOwnerTrip(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@PathVariable Integer tripId, @PathVariable("statusId") Integer status,
			@RequestParam(name = "totalKms", required = false) Double kms) {
		EResponse<List<Trip>> base = new EResponse<List<Trip>>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(null);
				Trip trip = tripService.getTrip(tripId);

				// Validate if trip exists for the specific vehicle owner...
				if (trip.getUser() != null
						&& trip.getVehicle().getOwner().getOwnerId().intValue() == tokenService.getMemberId(tokenKey)) {

					// If trip is gonna be rejected..
					if (status == -1) {

						// Send cancellation e-Mail to User..
						String content = templateService.getApologyTemplate(trip);
						mailService.send(trip.getUser().getMail(), "Trip Cancelled !", content, null, MailBox.HTML);

						// Add a penalty fee charge to vehicle owner...
						Transaction transaction = new Transaction();
						transaction.setAmount(50.0);
						transaction.setOwner(trip.getVehicle().getOwner());
						transaction.setUser(trip.getUser());
						transaction.setTransactionType("IncPending");
						transaction.setRemarks("Fine for Rejection of the trip.");
						transaction.setTrip(trip);
						transaction.setUserRole(trip.getUser().getUserRole());
						transactionService.insert(transaction);

					}

					// If trip is completed
					if (status == 3) {

						// update trip charges according to the distance travelled..
						trip.setDistance(kms);
						long difference = trip.getEndDate().getTime() - trip.getStartDate().getTime();
						double days = (difference / (1000 * 60 * 60 * 24)) + 1;
						double tr = trip.getDistance() / days;
						Double dayCharge = 0.0;
						Double mileage = 0.0;
						Double kmsCharge = 0.0;
						Double incentive = 0.0;

						// Get trip rates per Day...
						if (trip.getAc()) {
							dayCharge = trip.getVehicle().getAcDayCharge();
							kmsCharge = trip.getVehicle().getAcKmsCharge() * tr;
							mileage = trip.getVehicle().getAcMileage();
						} else {
							dayCharge = trip.getVehicle().getDefaultDayCharge();
							kmsCharge = trip.getVehicle().getDefaultKmsCharge() * tr;
							mileage = trip.getVehicle().getDefaultMileage();
						}

						// Get Daily based cost & incentive...
						Double fuelCost = Double.parseDouble(dataService.getData("fuelRate")) * (tr / mileage);
						Double expectedCharge = trip.getVehicle().getKmsSwitch() < tr ? kmsCharge
								: dayCharge + fuelCost;
						incentive = trip.getVehicle().getKmsSwitch() < tr
								? trip.getVehicleClass().getAppKmsIncentive() * tr
								: trip.getVehicleClass().getAppDayIncentive() * days;

						// Calculate Exact Charges by number of days trip went on...
						Double tripCharge = expectedCharge * days;
						trip.setTripCharge(dataService.format(tripCharge));

						// Add Cash Transaction Data for trip payment paid to Owner...
						Transaction transaction = new Transaction();
						transaction.setAmount(dataService.format(tripCharge));
						transaction.setOwner(trip.getVehicle().getOwner());
						transaction.setUser(trip.getUser());
						transaction.setTransactionType("Cash");
						transaction.setRemarks("Cash received on completing trip.");
						transaction.setTrip(trip);
						transaction.setUserRole(trip.getUser().getUserRole());
						transactionService.insert(transaction);

						// Add Pending Transaction Data for incentive of EGOZZ by owner...
						Transaction trnsc = new Transaction();
						trnsc.setAmount(dataService.format(incentive));
						trnsc.setOwner(trip.getVehicle().getOwner());
						trnsc.setTransactionType("IncPending");
						trnsc.setRemarks("EGOZZ incentive for the trip");
						trnsc.setTrip(trip);
						transactionService.insert(trnsc);

						// Create Invoice of the trip & send to user...
						invoiceService.createInvoice(trip);
					}

					// update trip status to DB...
					trip.setTripStatus(status);
					tripService.updateTrip(trip);

					base.success("Trip Updated");
				} else
					base.invalidToken();
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

}
