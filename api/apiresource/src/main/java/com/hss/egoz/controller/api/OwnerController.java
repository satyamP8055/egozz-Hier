package com.hss.egoz.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.MailBox;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Admin;
import com.hss.egoz.model.Owner;
import com.hss.egoz.model.Transaction;
import com.hss.egoz.service.admin.AdminService;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.owner.OwnerService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.transaction.PaymentService;

/*
 * @author Satyam Pandey
 * Controller for Owner Operations...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.OWNER)
public class OwnerController {

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PaymentService transactionService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ReporterService reporterService;

	/*
	 * @Param Owner details...
	 * 
	 * @Method Post..
	 */
	@RequestMapping(value = Url.SIGNUP, method = RequestMethod.POST)
	public ResponseEntity<EResponse<Boolean>> signup(Owner owner) {

		EResponse<Boolean> base = new EResponse<Boolean>();
		try {
			Integer ownerId = ownerService.register(owner);
			if (ownerId >= 0) {
				String content = templateService.getRegistrationTemplate(owner.getFullName(), "Vehicle Owner",
						owner.getMail());
				mailService.send(owner.getMail(), "Welcome Message", content, null, MailBox.HTML);
				String token = tokenService.generateToken(String.valueOf(ownerId));
				base.setToken(token);
				base.success("Registration Successfull");
			} else
				base.fail("Error Occured !");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Param Login Credentials i.e. userName & password
	 * 
	 * @Method Post
	 */
	@RequestMapping(value = Url.LOGIN, method = RequestMethod.POST)
	public ResponseEntity<EResponse<Owner>> login(Owner owner) {

		EResponse<Owner> base = new EResponse<>();
		try {
			if (owner == null || owner.getUserName() == null)
				base.fail("Invalid Credentials !");
			else {
				Integer ownerId = ownerService.login(owner);
				if (ownerId >= 0) {
					String token = tokenService.generateToken(String.valueOf(ownerId));
					base.setToken(token);
					base.setData(ownerService.findById(ownerId));
					base.success("Login Successfull");
				} else
					base.fail("Invalid Credentials !");
			}
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	@RequestMapping(value = Url.LOGOUT)
	public ResponseEntity<EResponse<String>> logout(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {

		EResponse<String> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(null);
				tokenService.invalidate(tokenKey);
				base.success("Logged Out ");
			} else
				base.fail("Invalid token !");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : Owner profile as JSON
	 */
	@RequestMapping(value = Url.PROFILE)
	public ResponseEntity<EResponse<Owner>> profile(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {

		EResponse<Owner> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				base.setData(ownerService.findById(tokenService.getMemberId(tokenKey)));
				base.success("profile retrieved");
			} else
				base.fail("Invalid token !");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : List of Transactions related to owner as JSON Array...
	 */
	@RequestMapping(value = Url.TRANSACTION)
	public ResponseEntity<EResponse<List<Transaction>>> transactions(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {

		EResponse<List<Transaction>> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)
					&& ownerService.findById(tokenService.getMemberId(tokenKey)) != null) {
				base.setData(
						transactionService.listForOwner(ownerService.findById(tokenService.getMemberId(tokenKey))));
				base.success("Transactions Fetched !");
			} else
				base.fail("Invalid token !");
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
	@RequestMapping(value = Url.PAY)
	public ResponseEntity<EResponse<List<Transaction>>> pay(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@PathVariable Integer transactionId) {

		EResponse<List<Transaction>> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)
					&& ownerService.findById(tokenService.getMemberId(tokenKey)) != null) {
				transactionService.pay(transactionId);
				base.setData(
						transactionService.listForOwner(ownerService.findById(tokenService.getMemberId(tokenKey))));
				base.success("Transactions Fetched !");
			} else
				base.fail("Invalid token !");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

	@Autowired
	AdminService adminService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : Updated List of Transactions as JSON Array
	 */
	@RequestMapping(value = Url.BLANK)
	public ResponseEntity<EResponse<List<Owner>>> list(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<List<Owner>> base = new EResponse<>();
		Admin admin;
		try {
			admin = adminService.getCurrent(tokenService.getMemberId(tokenKey));
		} catch (Exception ex) {
			throw new InvalidTokenException();
		}
		base.setData(admin != null ? ownerService.list() : null);
		base.success("fetched successfully");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

}
