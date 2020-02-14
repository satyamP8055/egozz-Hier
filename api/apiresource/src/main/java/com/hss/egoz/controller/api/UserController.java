package com.hss.egoz.controller.api;

import java.util.List;

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
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.MailBox;
import com.hss.egoz.constants.Random;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Admin;
import com.hss.egoz.model.User;
import com.hss.egoz.service.admin.AdminService;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.user.UserService;
import com.hss.egoz.service.util.DataService;

/*
 * @author Satyam Pandey
 * Controller to perform user based operations..
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.USER)
public class UserController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private DataService dataService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReporterService reporterService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MailService mailService;

	@Autowired
	AdminService adminService;

	/*
	 * @Param Login Credentials i.e. userName & password
	 * 
	 * @Method Post
	 */
	@CrossOrigin
	@RequestMapping(value = Url.LOGIN, method = RequestMethod.POST)
	public ResponseEntity<EResponse<Boolean>> login(User user) {
		EResponse<Boolean> base = new EResponse<Boolean>();
		try {
			Integer userId = userService.userLogin(user);
			if (userId >= 0) {
				base.success("Login Success !");
				base.setToken(tokenService.generateToken(userId));

			} else
				base.fail("Invalid Credentials !");

		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	@CrossOrigin
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
	 * @Param Owner details...
	 * 
	 * @Method Post..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.VERIFY, method = RequestMethod.POST)
	public ResponseEntity<EResponse<Boolean>> verify(User user) {
		EResponse<Boolean> base = new EResponse<Boolean>();
		try {
			String otp = dataService.random("", Random.NUMERIC, 4);
			String content = templateService.getOTPTemplate(user.getFullName(), user.getMail(), otp);
			if (user != null && user.getFullName() != null && user.getMail() != null) {
				mailService.send(user.getMail(), "Verify Your E-Mail", content, null, MailBox.HTML);
				String currentUser = user.toString();
				currentUser += DataService.STRING_BREAKER + otp;
				base.success("OTP SENT");
				base.setToken(tokenService.generateToken(currentUser));
			} else
				base.fail("Inappropriate Data !");
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Param otp...
	 * 
	 * @Method GET..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.SIGNUP, method = RequestMethod.GET)
	public ResponseEntity<EResponse<Boolean>> signUp(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam String otp) {
		EResponse<Boolean> base = new EResponse<Boolean>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				String userString = tokenService.getMember(tokenKey);
				List<String> userData = dataService.breakString(userString, DataService.STRING_BREAKER);
				int size = userData.size();
				String code = userData.get(size - 1);
				String fullName = userData.get(0);
				String mail = userData.get(1);
				String password = userData.get(2);
				String userName = userData.get(3);
				String phone = userData.get(4);
				User user = new User();
				user.setFullName(fullName);
				user.setMail(mail);
				user.setPassword(password);
				user.setUserName(userName);
				user.setPhone(phone);
				if (code.equals(otp)) {
					Integer userId = userService.userSignUp(user);
					if (userId >= 0) {
						base.success("Registration Success !");
						base.setToken(tokenService.generateToken(userId));
					} else
						base.fail("Some error communicating to database !");
				} else
					base.fail("Invalid OTP");
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
	 * @Method GET..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.PROFILE, method = RequestMethod.GET)
	public ResponseEntity<EResponse<User>> getProfile(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {
		EResponse<User> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {
				User user = userService.getUser(tokenService.getMemberId(tokenKey));
				if (user == null) {
					base.fail("No Such User");
				} else {
					base.setData(user);
					base.success("Profile Fetched !");
				}
			} else
				base.invalidToken();
		} catch (Exception ex) {
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : Updated List of Transactions as JSON Array
	 */
	@RequestMapping(value = Url.BLANK)
	public ResponseEntity<EResponse<List<User>>> list(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {

		EResponse<List<User>> base = new EResponse<>();
		Admin admin;
		try {
			admin = adminService.getCurrent(tokenService.getMemberId(tokenKey));
		} catch (Exception ex) {
			throw new InvalidTokenException();
		}
		base.setData(admin != null ? userService.list() : null);
		base.success("Fetched USers");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

}
