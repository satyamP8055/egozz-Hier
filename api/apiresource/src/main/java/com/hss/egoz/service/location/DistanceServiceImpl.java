package com.hss.egoz.service.location;

import org.springframework.stereotype.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.hss.egoz.constants.Secrets;

/*
 * @author Satyam Pandey
 * Service to perform Distance Matrix operations
 * */
@Service
public class DistanceServiceImpl implements DistanceService {

	private final String API_KEY = Secrets.MAP_API_KEY;

	/*
	 * @Param addrOne (Origin Address), addrTwo (Destination Address)
	 * 
	 * @NOTE : IF API RESTRICTS LOCATION USAGE FOR THE DAY, IT WILL SEND 30 BY
	 * DEFAULT WHICH MEANS NO MORE VEHICLES WILL BE AVAILABLE FOR THE DAY
	 */
	@Override
	public Double distance(String addrOne, String addrTwo) {
		try {

			// Get GeoApiContext
			GeoApiContext distCalcer = new GeoApiContext.Builder().apiKey(API_KEY).build();

			// Get DistanceMatrixApiRequest
			DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(distCalcer);

			// Get Result in the format of DistanceMatrix Object
			DistanceMatrix result = req.origins(addrOne).destinations(addrTwo).mode(TravelMode.DRIVING)
					.language("en-US").await();

			// Get distance in meters
			long distApart = result.rows[0].elements[0].distance.inMeters;

			// convert distance to KMS
			double distance = distApart / 1000;
			return distance;
		} catch (Exception e) {
			return 30.00;
		}
	}

}
