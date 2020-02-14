package com.hss.egoz.model.payments;

import com.hss.egoz.constants.Secrets;

/*
 * @author Satyam Pandey
 * Model as it is to transact data between the app & PayUMoney provider...
 * @Note this model does not follow java naming convention as it is following the payU convention
 * */
public class PayUModel {

	private final String baseUrl = Secrets.PAYU_BASE_URL;

	private final String key = Secrets.PAYU_KEY;

	private final String salt = Secrets.PAYU_SALT;

	private final String service_provider = Secrets.PAYU_SERVICE_PROVIDER;

	private String hash, txnid, amount, firstname, email, phone, productinfo, surl, furl, hashstring;

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getKey() {
		return key;
	}

	public String getSalt() {
		return salt;
	}

	public String getService_provider() {
		return service_provider;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProductinfo() {
		return productinfo;
	}

	public void setProductinfo(String productinfo) {
		this.productinfo = productinfo;
	}

	public String getSurl() {
		return surl;
	}

	public void setSurl(String surl) {
		this.surl = surl;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public String getHashstring() {
		return hashstring;
	}

	public void setHashstring(String hashstring) {
		this.hashstring = hashstring;
	}

}
