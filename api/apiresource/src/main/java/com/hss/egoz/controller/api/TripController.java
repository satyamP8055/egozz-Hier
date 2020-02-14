package com.hss.egoz.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.bean.TransactionData;
import com.hss.egoz.bean.TripData;
import com.hss.egoz.bean.VehicleIncome;
import com.hss.egoz.bean.VehicleTripData;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Admin;
import com.hss.egoz.model.Transaction;
import com.hss.egoz.model.Trip;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.service.admin.AdminService;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.location.DistanceService;
import com.hss.egoz.service.owner.OwnerService;
import com.hss.egoz.service.transaction.PaymentService;
import com.hss.egoz.service.trip.TripService;
import com.hss.egoz.service.util.DataService;
import com.hss.egoz.service.vehicle.VehicleService;

/*
 * @author Satyam Pandey
 * Controller to perform trip-statistics operations...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.TRIP)
public class TripController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private DataService dataService;

	@Autowired
	private TripService tripService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private PaymentService transactionService;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private ReporterService reporterService;

	@Autowired
	private DistanceService distanceService;

	@Autowired
	private AdminService adminService;
	
	int counter = 0;

	/*
	 * @Header X-ACCESS-TOKEN for authorization of user
	 * 
	 * @Param startDate, endDate, class, ac, distance, lat, lon
	 * 
	 * @Return data : Vehicle available for given data, time & place..
	 */
	@RequestMapping(value = Url.AVAILABLE_VEHICLES, method = RequestMethod.GET)
	public ResponseEntity<EResponse<List<Vehicle>>> signUp(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam("startDate") String startString, @RequestParam("endDate") String endString,
			@RequestParam("class") Integer classId, @RequestParam Boolean ac, @RequestParam Double distance,
			@RequestParam Double lat, @RequestParam Double lon) {
		EResponse<List<Vehicle>> base = new EResponse<List<Vehicle>>();
		try {
			counter = 0;
			if (tokenService.validateToken(tokenKey)) {

				// Get Origin Lat-Long String to be passed in Distance Matrix API...
				String origin = lat + "," + lon;

				// Modify time to start by the midnight & end at modnight i.e. for 24 hours..
				String startTime = startString + " 00:00:00";
				String endTime = endString + " 23:59:59";

				// Get a date formatter to get Date objects of above strings...
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				// Calculate number of Days the trip will go...
				long difference = formatter.parse(endTime).getTime() - formatter.parse(startTime).getTime();
				double days = (difference / (1000 * 60 * 60 * 24)) + 1;

				// Get Perday estimated travel during the trip...
				double tr = distance / days;

				// Validate perday travel less than 500 KMS..
				if (tr > 500) {
					base.fail("No trip allowed more than 500 KMS a day !");
				} else {
					// Get Vehicles engaged between the given dates...
					List<Vehicle> vehicles = tripService.filterVehiclesForDate(formatter.parse(startTime),
							formatter.parse(endTime), classId);

					/*
					 * Get all the vehicles that are :
					 * 
					 * @1 Not engaged between given dates..
					 * 
					 * @2 Not far than 25 KMS from Origin according to distance matrix API
					 * 
					 * @3 Active as their current status
					 */
					List<Vehicle> availableVehicles = vehicleService.getVehicleList().stream()
							.filter(vehicle -> !vehicles.contains(vehicle)
									&& vehicle.getVehicleClass().getVehicleClassId().intValue() == classId
									&& vehicle.getVehicleStatus().equalsIgnoreCase("active")
									&& distanceService.distance(origin,
											vehicle.getLatitude() + "," + vehicle.getLongitude()) <= 25)
							.collect(Collectors.toList());

					// Caculate expected Charge for each Vehicles
					availableVehicles.forEach(vehicle -> {

						// Calculate Per Day Charge
						Double dayCharge = (ac ? vehicle.getAcDayCharge() : vehicle.getDefaultDayCharge());
						Double mileage = ac ? vehicle.getAcMileage() : vehicle.getDefaultMileage();
						Double kmsCharge = ac ? tr * vehicle.getAcKmsCharge() : tr * vehicle.getDefaultKmsCharge();
						Double fuelCost = Double.parseDouble(dataService.getData("fuelRate")) * (tr / mileage);

						// Calculate exact expected charge for the whole trip
						Double expectedCharge = vehicle.getKmsSwitch() < tr ? kmsCharge : dayCharge + fuelCost;
						vehicle.setCharge(dataService.format(expectedCharge * days));

						counter++;
					});
					base.success("Vehicles Retrieved !");
					base.setData(availableVehicles);
				}
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	int total = 0;
	int completed = 0;
	int cancelled = 0;
	int rejected = 0;
	int upcoming = 0;
	int ongoing = 0;

	/*
	 * @Header X-ACCESS-TOKEN for authorization of owner
	 * 
	 * @Return data : Trip by Status Statistics (completed, pending, rejected &
	 * cancelled )...
	 */
	@RequestMapping(value = Url.TRIP_GRAPH)
	public ResponseEntity<EResponse<TripData>> statistics(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<TripData> base = new EResponse<>();
		total = 0;
		completed = 0;
		cancelled = 0;
		rejected = 0;
		upcoming = 0;
		ongoing = 0;
		try {
			TripData tripData = new TripData();
			if (tokenService.validateToken(tokenKey)) {

				// Get all trips of current owner...
				List<Trip> trips = tripService
						.listAll().stream().filter(trip -> (trip.getVehicle().getOwner().getOwnerId()
								.intValue() == tokenService.getMemberId(tokenKey).intValue()))
						.collect(Collectors.toList());
				total = trips.size();

				// Traverse the list & increment the corresponding representative based on
				// status of the trip...
				trips.stream().forEach(trip -> {
					switch (trip.getTripStatus()) {
					case -2:
						cancelled++;
						break;
					case -1:
						rejected++;
						break;
					case 0:
						upcoming++;
						break;
					case 1:
						upcoming++;
						break;
					case 2:
						ongoing++;
						break;
					case 3:
						completed++;
						break;
					}
				});

				// Update the total counts...
				tripData.setCancelled(cancelled);
				tripData.setCompleted(completed);
				tripData.setRejected(rejected);
				tripData.setTotal(total);
				tripData.setUpcoming(upcoming);
				tripData.setOngoing(ongoing);

				// Set Data..
				base.setData(tripData);
				base.success("Data retrieved !");
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	double withdrawl = 0;
	double pending = 0;
	double deposit = 0;

	/*
	 * @Header X-ACCESS-TOKEN for authorization of owner
	 * 
	 * @Return data : Transaction by Status Statistics (Withdrawl, Deposit, Pending
	 * )...
	 */
	@RequestMapping(value = Url.TRANSACTION_GRAPH)
	public ResponseEntity<EResponse<TransactionData>> transactionStatistics(
			@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<TransactionData> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				withdrawl = 0;
				deposit = 0;
				pending = 0;

				// Get transaction list for owner & traverse to filter & count..
				transactionService.listForOwner(ownerService.findById(tokenService.getMemberId(tokenKey))).stream()
						.forEach(transaction -> {
							switch (transaction.getTransactionType()) {
							case "IncDeposit":
								withdrawl += transaction.getAmount();
								break;
							case "IncPending":
								pending += transaction.getAmount();
								break;
							case "Cash":
								deposit += transaction.getAmount();
							}
						});

				// Create the bean & set respective data...
				TransactionData data = new TransactionData();
				data.setDeposit(deposit);
				data.setWithdrawl(withdrawl);
				data.setPending(pending);
				base.setData(data);
				base.success("Retrieval Success");
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization of owner
	 * 
	 * @Return data : Trip by Vehicle Statistics (tripNumbers for vehicles )...
	 */
	@RequestMapping(value = Url.VEHICLE_TRIP_GRAPH)
	public ResponseEntity<EResponse<List<VehicleTripData>>> vehicleStatistics(
			@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<List<VehicleTripData>> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				// create a blank arrayList of statistics..
				List<VehicleTripData> data = new ArrayList<VehicleTripData>();

				// Get Active vehicles of current owner...
				List<Vehicle> vList = vehicleService.getVehicleList().stream().filter(
						vehicle -> (vehicle.getOwner().getOwnerId().intValue() == tokenService.getMemberId(tokenKey)
								&& vehicle.getVehicleStatus().equalsIgnoreCase("active")))
						.collect(Collectors.toList());

				// Get All trips...
				List<Trip> tripList = tripService.listAll();

				// Traverse vehicles & count their trips & add to the statistics array..
				vList.stream().forEach(vehicle -> {
					long trips = tripList.stream().filter(
							trip -> (trip.getVehicle().getVehicleId().intValue() == vehicle.getVehicleId().intValue()
									&& trip.getTripStatus() >= 0))
							.count();
					String vehicleName = vehicle.getVehicleName();
					VehicleTripData e = new VehicleTripData();
					e.setTrips((int) trips);
					e.setVehicleName(vehicleName);
					data.add(e);
				});

				// Set the data
				base.setData(data);
				base.success("Data retrieved !");
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	double income = 0;

	/*
	 * @Header X-ACCESS-TOKEN for authorization of owner
	 * 
	 * @Return data : Income of Vehicle Statistics (completed trips only )...
	 */
	@RequestMapping(value = Url.VEHICLE_INCOME_GRAPH)
	public ResponseEntity<EResponse<List<VehicleIncome>>> incomeStatistics(
			@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<List<VehicleIncome>> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				
				// create a blank arraylist to add data..
				List<VehicleIncome> data = new ArrayList<VehicleIncome>();
				
				// Get All Vehicles of the current owner...
				List<Vehicle> vList = vehicleService.getVehicleList().stream().filter(
						vehicle -> (vehicle.getOwner().getOwnerId().intValue() == tokenService.getMemberId(tokenKey)
								&& vehicle.getVehicleStatus().equalsIgnoreCase("active")))
						.collect(Collectors.toList());
				
				// Get Owner's total transactions...
				List<Transaction> transactions = transactionService
						.listForOwner(ownerService.findById(tokenService.getMemberId(tokenKey)));
				
				// Traverse vehicles & count their income from transactions...
				vList.stream().forEach(vehicle -> {
					String vehicleName = vehicle.getVehicleName();
					income = 0;
					transactions.stream()
							.filter(transaction -> (transaction.getTrip().getVehicle().getVehicleId()
									.intValue() == vehicle.getVehicleId().intValue()
									&& transaction.getTransactionType().equalsIgnoreCase("Cash")))
							.collect(Collectors.toList()).forEach(transaction -> {
								income += transaction.getAmount();
							});
					VehicleIncome e = new VehicleIncome();
					e.setIncome(income);
					e.setVehicleName(vehicleName);
					data.add(e);
				});
				
				// Set the data
				base.setData(data);
				base.success("Data retrieved !");
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
	 * @Return data : Updated List of Transactions as JSON Array
	 */
	@RequestMapping(value = Url.BLANK)
	public ResponseEntity<EResponse<List<Trip>>> list(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<List<Trip>> base = new EResponse<>();
		Admin admin;
		try {
			admin = adminService.getCurrent(tokenService.getMemberId(tokenKey));
		} catch (Exception ex) {
			throw new InvalidTokenException();
		}
		base.setData(admin != null ? tripService.listAll() : null);
		base.success("Fetched Trips");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

}
