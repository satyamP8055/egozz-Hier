package com.hss.egoz.controller.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Admin;
import com.hss.egoz.model.Transaction;
import com.hss.egoz.service.admin.AdminService;
import com.hss.egoz.service.transaction.PaymentService;
import com.hss.egoz.service.vehicle.VehicleService;

/*
 * @author Satyam Pandey
 * Controller to perform Admin specific Operations...
 * */
@CrossOrigin
@RestController
@RequestMapping(Url.ADMIN)
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	PaymentService transactionService;

	@Autowired
	TokenService tokenService;

	@Autowired
	VehicleService vehicleService;

	/*
	 * @Param userName & password for login
	 * 
	 * @Return data : Token if login succeeds..
	 */
	@PostMapping(Url.LOGIN)
	public ResponseEntity<EResponse<Boolean>> login(Admin admin) {
		EResponse<Boolean> base = new EResponse<Boolean>();
		Admin inDb = adminService.login(admin);
		if (inDb == null)
			base.fail("Invalid Credentials");
		else {
			base.success("Login Success");
			base.setToken(tokenService.generateToken(inDb.getAdminId()));
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 */
	@RequestMapping(value = Url.LOGOUT)
	public ResponseEntity<EResponse<String>> logout(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<String> base = new EResponse<>();
		if (tokenService.validateToken(tokenKey)) {
			base.setData(null);
			tokenService.invalidate(tokenKey);
			base.success("Logged Out ");
		} else
			throw new InvalidTokenException();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : List of Transactions related to owner as JSON Array...
	 */
	@RequestMapping(value = Url.TRANSACTION)
	public ResponseEntity<EResponse<List<Transaction>>> transactions(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<List<Transaction>> base = new EResponse<>();
		if (tokenService.validateToken(tokenKey)
				&& adminService.getCurrent(tokenService.getMemberId(tokenKey)) != null) {
			base.setData(transactionService.list());
			base.success("Transactions Fetched !");
		} else
			throw new InvalidTokenException();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}
}
