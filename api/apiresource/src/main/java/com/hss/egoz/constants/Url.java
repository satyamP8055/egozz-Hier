package com.hss.egoz.constants;

/*
 * @author Satyam Pandey
 * Interface to contain the URL patterns for request mapping in Controllers..
 * */
public interface Url {

	String ACCESS = "/api/access";

	String ADD = "/add";
	
	String ADMIN = "/api/admin";

	String AUTH_HEADER = "X-ACCESS-TOKEN";

	String AVAILABLE_VEHICLES = "vehicles/available";

	String BOOK = "/book";

	String BLANK = "";

	String COMPLETE = "complete";

	String CURRENT_HOST = App.HOST;

	String OWNER = "/api/owner";

	String OWNER_VEHICLE = "/api/vehicle/owner";

	String OWNER_TRIP = "/api/trip/owner";

	String LIST = "/list";

	String LOGIN = "login";

	String LOGOUT = "logout";

	String PAY = "pay/{transactionId}";

	String PAY_LIVE = "pay";

	String PAYPAL = "/api/paypal";

	String PAYU = "/api/payu";

	String PAYU_CONFIRM = "/confirm";

	String REDIRECT = "/redirect";

	String PROFILE = "profile";

	String REVIEW = "review";

	String TEST = "/api/test";

	String TRANSACTION = "transactions";

	String TRANSACTION_GRAPH = "transaction-data";

	String TRIP_GRAPH = "data";

	String TRIP_UPDATE = "update/{tripId}/{statusId}";

	String REMOTE_HOST = "http://localhost:4200";

	String SIGNUP = "signup";

	String TRIP = "/api/trip";

	String USER = "/api/user";

	String USER_TRIP = "/api/trip/user";

	String VEHICLE = "/api/vehicle";

	String VEHICLE_CLASS_LIST = "/class/list";

	String VEHICLE_INCOME_GRAPH = "vehicle-income";

	String VEHICLE_TRIP_GRAPH = "vehicle-trip-data";

	String VEHICLE_UPDATE = "/update/{vehicleId}";

	String VEHICLE_DELETE = "delete/{vehicleId}";

	String VERIFY = "verify";

}
