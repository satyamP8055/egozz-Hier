package com.hss.egoz.service.util;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hss.egoz.constants.Constants;
import com.hss.egoz.constants.EncryptMode;
import com.hss.egoz.constants.Random;
/*
 * @author Satyam Pandey
 * A Utility Service to perform some fundamental data operations related to App...
 * */
public interface DataService {

	String STRING_BREAKER=Constants.egozz.toString();
	
	List<String> breakString(String data, String breakPoint);

	Map<String, String> params(HttpServletRequest request);

	HttpServletRequest getRequest();

	String getData(String key);

	Double format(Double value);

	String random(String prefix, Random mode, Integer size);

	String encodeSha(String string, EncryptMode mode) throws NoSuchAlgorithmException;

}
