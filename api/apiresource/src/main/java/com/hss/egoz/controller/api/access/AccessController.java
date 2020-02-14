package com.hss.egoz.controller.api.access;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.config.InvalidTokenException;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.Access;
import com.hss.egoz.model.Admin;
import com.hss.egoz.service.admin.AdminService;

/*
 * @author Satyam Pandey
 * Controller to perform Role Specific Operations...
 * */
@CrossOrigin
@RequestMapping(Url.ACCESS)
@RestController
public class AccessController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AdminService adminService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Return data : Access allowed to admin as JSON Array..
	 */
	@GetMapping(Url.BLANK)
	public ResponseEntity<EResponse<List<Access>>> getAccess(@RequestHeader(Url.AUTH_HEADER) String tokenKey)
			throws InvalidTokenException {
		if (!tokenService.validateToken(tokenKey))
			throw new InvalidTokenException();
		EResponse<List<Access>> base = new EResponse<List<Access>>();
		Admin admin = adminService.getCurrent(tokenService.getMemberId(tokenKey));
		base.setData(admin.getRole().getAccess().stream().sorted(Comparator.comparing(Access::getAccessName))
				.collect(Collectors.toList()));
		base.success("ACCESS GRANTED");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);
	}

}
