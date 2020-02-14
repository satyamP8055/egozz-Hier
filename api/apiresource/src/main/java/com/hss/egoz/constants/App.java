package com.hss.egoz.constants;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/*
 * @author Satyam
 * Interface to store basic App Related Data...
 * */
public interface App {

	String APP_NAME = "EGOZZ Web API";

	String BACK_COLOR = "rgb(14,117,10)";

	String DEVELOPER_MAIL = "saty8055@gmail.com";

	String FALLBACK_COLOR = "rgb(black)";

	String HOST = "https://localhost:8055";

	String LOGO = "https://firebasestorage.googleapis.com/v0/b/kartik-crud.appspot.com/o/cQzaOcX3wTXZ9JNNdHSvbOe4Szp2egozz_ORG.png?alt=media&token=ef3e6caa-af65-4e71-80e6-4a1975966747";

	String NAME = "EGOZZ";

	String PRIME_COLOR = "rgb(8,11,90)";

	// Directory of webapps Folder ...
	String ROOT_DIRECTORY = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
			.getServletContext().getRealPath("/");

	String TEAM_CODE = "Team EGOZZ";

	String WARNING_COLOR = "rgb(192,39,45)";

}