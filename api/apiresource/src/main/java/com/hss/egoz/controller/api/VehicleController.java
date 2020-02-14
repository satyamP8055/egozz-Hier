package com.hss.egoz.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.App;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.model.VehicleClass;
import com.hss.egoz.service.admin.AdminService;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.transaction.PaymentService;
import com.hss.egoz.service.vehicle.VehicleService;

/*
 * @author Satyam Pandey
 * Controller to perform vehicle based global operations
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.VEHICLE)
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ReporterService reporterService;

	@Autowired
	private AdminService adminService;

	@Autowired
	PaymentService transactionService;

	@Autowired
	TokenService tokenService;



	/*
	 * @Return class list of vehicles as JSON Array
	 */
	@RequestMapping(value = Url.VEHICLE_CLASS_LIST)
	public ResponseEntity<EResponse<List<VehicleClass>>> vehicleClassList() {

		EResponse<List<VehicleClass>> base = new EResponse<>();
		try {
			base.setData(vehicleService.getClassList());
			System.out.println(vehicleService.getClassList().get(0).getImage().getFileLocation());
			System.out.println(App.ROOT_DIRECTORY);
			base.success("Classes Listed ");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : List of owner vehicles as JSON array..
	 */
	@RequestMapping(value = Url.BLANK)
	public ResponseEntity<EResponse<List<Vehicle>>> vehicleList(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<List<Vehicle>> base = new EResponse<>();
		if (tokenService.validateToken(tokenKey)
				&& adminService.getCurrent(tokenService.getMemberId(tokenKey)) != null) {

			base.setData(vehicleService.getVehicleList());
			base.success("Vehicles Listed ");
		} else
			throw new InvalidTokenException();

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

}
