package com.hss.egoz.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hss.egoz.constants.EncryptMode;
import com.hss.egoz.constants.Library;
import com.hss.egoz.constants.Random;

/*
 * @author Satyam Pandey
 * Service to perform some basic String & data related operations...
 * */
@Service
public class DataServiceImpl implements DataService {

	private final DecimalFormat formatter = new DecimalFormat("0.00");

	/*
	 * @Param double value
	 * 
	 * @Return double value rounded to 0.00 format..
	 */
	@Override
	public Double format(Double value) {
		return Double.parseDouble(formatter.format(value));
	}

	/*
	 * @Param String key
	 * 
	 * @Return String value associated with the key..
	 */
	@Override
	public String getData(String key) {
		if (key.equalsIgnoreCase("fuelRate"))
			return "70.63";
		else
			return null;
	}

	/*
	 * @Param 1. data : String to be broken 2. breakPoint : String supposed to be
	 * breaker of data
	 * 
	 * @Return : List<String> i.e. a java.util.List object as collection of all the
	 * data broken
	 */
	@Override
	public java.util.List<String> breakString(String data, String breakPoint) {

		// Get a new List object...
		java.util.List<String> brokenString = new java.util.ArrayList<String>();
		int i = 0;
		String temp = "";

		/*
		 * Traverse the whole string to create a sequence matching with the breakPoint &
		 * add to brokenString if matched
		 */
		for (i = 0; i < data.length(); i++) {
			temp = temp + data.charAt(i);
			String myTem = "";
			for (int index = 0; (index < breakPoint.length()) && (i + index < data.length()); index++) {
				myTem = myTem + data.charAt(i + index);
			}
			if (myTem.equals(breakPoint)) {
				brokenString.add(temp.substring(0, temp.length() - 1));
				i += breakPoint.length() - 1;
				temp = "";
			}
		}

		// Add the last left String as the last element of List...
		brokenString.add(temp);

		return brokenString;
	}

	private StringBuilder randomValue;

	/*
	 * @Param 1. prefix: String supposed to be starter of the Random Text Generated
	 * 2. mode: enum supposed to present the mode of random text... 3. size: integer
	 * for the size of random text
	 * 
	 * @Return String a Random Text Code...
	 */
	@Override
	public String random(String prefix, Random mode, Integer size) {
		// Initialise the random value to prefix...
		randomValue = new StringBuilder(prefix);

		// Get a Key of which the Random value will be extracted...
		String key = "";

		// Initialise the key based on the mode of the service requirement...
		if (mode == Random.ALPHANUMERIC)
			key = Library.UPPER_ALPHABETS + Library.NATURAL_NUMBERS;
		else if (mode == Random.MISCELLANEOUS)
			key = Library.UPPER_ALPHABETS + Library.NATURAL_NUMBERS + Library.LOWER_ALPHABETS;
		else if (mode == Random.UPPER)
			key = Library.UPPER_ALPHABETS;
		else if (mode == Random.LOWER)
			key = Library.LOWER_ALPHABETS;
		else if (mode == Random.NUMERIC)
			key = Library.NATURAL_NUMBERS;
		else
			return "";

		// Get the object of java.util.Random
		java.util.Random utilRandom = new java.util.Random();

		/*
		 * Iterate size times to extract an element from key & append to the randomValue
		 */
		for (int i = 0; i < size; i++) {
			randomValue.append(key.charAt(utilRandom.nextInt(key.length())));
		}

		return randomValue.toString();
	}

	/*
	 * @Param HttpServletRequest Object
	 * 
	 * @Return Map<String, String> containing key, value pairs of parameters in
	 * request..
	 */
	@Override
	public Map<String, String> params(HttpServletRequest request) {
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, String> param = new LinkedHashMap<String, String>();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			param.put(key, request.getParameter(key));
		}
		return param;
	}

	/*
	 * @Return HttpServletRequest Object if in a Spring web context, null otherwise
	 */
	@Override
	public HttpServletRequest getRequest() {
		ServletRequestAttributes pool = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request;
		if (pool != null)
			request = pool.getRequest();
		else
			request = null;
		return request;
	}

	/*
	 * @Return SHA-256, or SHA-512 encrytion of string depending on the mode passed
	 */
	@Override
	public String encodeSha(String string, EncryptMode mode) throws NoSuchAlgorithmException {

		String sha=mode.toString();
		
		// convert string to bytes array
		byte[] seq = string.getBytes();

		// Get StringBuffer & MessageDigest instance..
		StringBuffer hex = new StringBuffer();
		MessageDigest algorithm = MessageDigest.getInstance(sha);
		algorithm.reset();
		algorithm.update(seq);

		// Get the encrypted element as byte array & store in stringBuffer
		byte[] messageDigest = algorithm.digest();
		for (int i = 0; i < messageDigest.length; i++) {
			String temp = Integer.toHexString(0xFF & messageDigest[i]);
			if (temp.length() == 1)
				hex.append("0");
			hex.append(temp);
		}

		// return string of stored in stringBuffer...
		return hex.toString();
	}

}
