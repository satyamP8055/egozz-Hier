package com.hss.egoz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EgozApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgozApplication.class, args);
		for(String ar:args) {
			System.out.println("PARAMTER "+ar);
		}
	}

}
