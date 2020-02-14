package com.hss.egoz.constants;

public enum EncryptMode {

	SHA256("SHA-256"), SHA512("SHA-512");

	private final String name;

	private EncryptMode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
