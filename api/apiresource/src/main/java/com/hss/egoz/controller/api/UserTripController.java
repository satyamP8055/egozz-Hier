package com.hss.egoz.controller.api;

import java.text.SimpleDateFormat;
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
import com.hss.egoz.model.Trip;
import com.hss.egoz.model.User;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.invoice.InvoiceService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.trip.TripService;
import com.hss.egoz.service.user.UserService;
import com.hss.egoz.service.util.DataService;
import com.hss.egoz.service.vehicle.VehicleService;

/*
 * @author Satyam Pandey
 * Controller to perform trip-statistics operations for user...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.USER_TRIP)
public class UserTripController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private DataService dataService;

	@Autowired
	private ReporterService reporterService;

	@Autowired
	private TripService tripService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private UserService userService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MailService mailService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization of user
	 * 
	 * @Param startDate, endDate, class, ac, distance, pickUpAddress, dropAddress
	 */
	@RequestMapping(value = Url.BOOK, method = RequestMethod.POST)
	public ResponseEntity<EResponse<String>> book(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam("startDate") String startString, @RequestParam("endDate") String endString,
			@RequestParam Integer vehicleId, @RequestParam Boolean ac, @RequestParam Double distance,
			@RequestParam Double charge, @RequestParam String pickUpAddress, @RequestParam String dropAddress) {
		EResponse<String> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				
				// Get Start & End Date String with time
				String startTime = startString + " 00:00:00";
				String endTime = endString + " 23:59:59";
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				// Get Current User & requested Vehicle ...
				User user = userService.getUser(tokenService.getMemberId(tokenKey));
				Vehicle vehicle = vehicleService.get(vehicleId);
				
				// Set Corresponding data to trip
				Trip trip = new Trip();
				trip.setUser(user);
				trip.setUserRole(user.getUserRole());
				trip.setVehicle(vehicle);
				trip.setPickUpAddress(pickUpAddress);
				trip.setDropAddress(dropAddress);
				trip.setAc(ac);
				trip.setDistance(dataService.format(distance));
				trip.setEgozzTrip(true);
				trip.setEndDate(formatter.parse(endTime));
				trip.setStartDate(formatter.parse(startTime));
				trip.setTripCharge(dataService.format(charge));
				trip.setTripStatus(0);
				trip.setVehicleClass(vehicle.getVehicleClass());
				
				// Set KMS Based flag
				double days = (trip.getEndDate().getTime() - trip.getStartDate().getTime() / (1000 * 60 * 60 * 24)) + 1;
				double tr = distance / days;
				trip.setKmsBased(tr > trip.getVehicle().getKmsSwitch());
				
				// update change to database using service
				if (tripService.insertTrip(trip) >= 0) {
					base.success("Trip Booked");
					String content = templateService.getTripNotificationTemplate(trip);
					
					// Inform Vehicle Owner & send profarma to user...
					mailService.send(trip.getVehicle().getOwner().getMail(),
							"Booking at " + new SimpleDateFormat("dd/MM/yyyy").format(trip.getStartDate()), content,
							invoiceService.createProfarma(trip), MailBox.ATTACHMENT);
				} else
					base.fail("Error Updating Database");

			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization of user
	 * 
	 * @Return List of trips booked by user
	 */
	@RequestMapping(value = Url.LIST, method = RequestMethod.GET)
	public ResponseEntity<EResponse<List<Trip>>> userTrips(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<List<Trip>> base = new EResponse<List<Trip>>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(
						tripService
								.listAll().stream().filter(trip -> (trip.getUser().getUserId()
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
	 * @Header X-ACCESS-TOKEN for authorization of user
	 * 
	 * @PathParam tripId, statusId
	 */
	@RequestMapping(value = Url.TRIP_UPDATE, method = RequestMethod.PUT)
	public ResponseEntity<EResponse<String>> updateUserTrip(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@PathVariable Integer tripId, @PathVariable("statusId") Integer status) {
		EResponse<String> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(null);
				Trip trip = tripService.getTrip(tripId);
				if (trip.getUser() != null
						&& trip.getUser().getUserId().intValue() == tokenService.getMemberId(tokenKey)) {
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

	/*
	 * @Header X-ACCESS-TOKEN for authorization of user
	 * 
	 * @Param key, star, review
	 */
	@RequestMapping(value = Url.REVIEW, method = RequestMethod.GET)
	public ResponseEntity<EResponse<List<Trip>>> review(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam("key") Integer tripId, @RequestParam Double star, @RequestParam String review) {
		EResponse<List<Trip>> base = new EResponse<List<Trip>>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(null);
				Trip trip = tripService.getTrip(tripId);
				
				// Validate if user is reviewing his own trip
				if (trip.getUser() != null
						&& trip.getUser().getUserId().intValue() == tokenService.getMemberId(tokenKey)) {
					
					// set star & review and update trip
					trip.setUserReviewStars(star);
					trip.setUserReviewMessage(review);
					tripService.updateTrip(trip);
					base.success("Review Updated Successfully");
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
