package com.hss.egoz.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hss.egoz.authentication.jwt.TokenService;
import com.hss.egoz.config.EResponse;
import com.hss.egoz.constants.Extension;
import com.hss.egoz.constants.Url;
import com.hss.egoz.model.DataFile;
import com.hss.egoz.model.Owner;
import com.hss.egoz.model.Vehicle;
import com.hss.egoz.model.VehicleClass;
import com.hss.egoz.service.exception.ReporterService;
import com.hss.egoz.service.files.FileService;
import com.hss.egoz.service.owner.OwnerService;
import com.hss.egoz.service.vehicle.VehicleService;

/*
 * @author Satyam Pandey
 * Controller to perform Vehicle Based operations of Vehicle Owner...
 * */
@RestController
@CrossOrigin
@RequestMapping(Url.OWNER_VEHICLE)
public class OwnerVehicleController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ReporterService reporterService;

	/*
	 * @Header X-ACCESS-TOKEN for authorization
	 * 
	 * @Param vehicleClassId & vehicle with vehicleImages as array of images
	 * 
	 * @Return data : Added Vehicle..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.ADD, method = RequestMethod.POST)
	public ResponseEntity<EResponse<Vehicle>> addVehicle(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam Integer vehicleClassId, Vehicle vehicle,
			@RequestParam("vehicleImages") MultipartFile[] uploads) {

		EResponse<Vehicle> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				// Get Owner ID from token & set a negative flag for vehicleId...
				Integer ownerId = Integer.parseInt(tokenService.getMember(tokenKey));
				Integer vehicleId = -1;

				// Validate vehicle is not null & is having a name..
				if (vehicle != null && vehicle.getVehicleName() != null) {

					// Get Owner Profile & add to vehicle
					Owner owner = ownerService.findById(ownerId);
					vehicle.setOwner(owner);

					// Set Vehicle status active & set vehicle class
					vehicle.setVehicleStatus("active");
					VehicleClass vehicleClass = vehicleService.getVehicleClass(vehicleClassId);
					vehicle.setVehicleClass(vehicleClass);

					// Add the vehicle & store the new created Id in previous id flag..
					vehicleId = vehicleService.addVehicle(vehicle);

					// Save vehicle Image Files...
					for (MultipartFile upload : uploads) {
						DataFile file = new DataFile();
						file.setFileLocation(fileService.upload("vehicles/" + vehicleId, Extension.jpg, upload));
						file.setVehicleId(vehicleId);
						file.setFileId(fileService.addFile(file));
					}

					// Add Vehicle to have all the images...
					vehicleService.updateVehicle(vehicle);
				}

				// Validate the flag is not negative & if success, operation success...
				if (vehicleId >= 0) {
					base.setData(vehicleService.get(vehicleId));
					base.success("Vehicle Added");
				} else
					base.fail("Error Occured");

				base.setToken(tokenKey);
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
	 * @Param vehicleClassId & vehicle with vehicleImages as array of images
	 * 
	 * @Return data : updated Vehicle..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.VEHICLE_UPDATE, method = RequestMethod.PUT)
	public ResponseEntity<EResponse<Vehicle>> editVehicle(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@RequestParam Integer vehicleClassId, Vehicle vehicle,
			@RequestParam("vehicleImages") MultipartFile[] uploads, @PathVariable Integer vehicleId) {

		EResponse<Vehicle> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				// Get Vehicle from Database...
				Vehicle fromDb = vehicleService.get(vehicleId);
				if (fromDb == null)
					base.fail("Invalid Reference !");
				else {
					List<DataFile> images = new ArrayList<>();

					// validate if to replace the images depending on uploads sent with request
					if (uploads.length != 0) {
						for (DataFile file : fromDb.getImages()) {
							fileService.deleteFile(file);
						}
						for (MultipartFile upload : uploads) {
							DataFile file = new DataFile();
							file.setFileLocation(fileService.upload("vehicles/" + vehicleId, Extension.jpg, upload));
							file.setVehicleId(vehicleId);
							images.add(file);
						}
					} else
						images = fromDb.getImages();
					vehicle.setImages(images);

					// reset owner & vehicle ID...
					vehicle.setOwner(fromDb.getOwner());
					vehicle.setVehicleId(vehicleId);

					// Update Vehicle using Service...
					vehicleService.updateVehicle(vehicle);

					// set the newly update vehicle for response data...
					base.setData(vehicleService.get(vehicle.getVehicleId()));
					base.success("Vehicle Updated");
				}
				base.setToken(tokenKey);
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
	 * @Return data : List of owner vehicles as JSON array..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.LIST)
	public ResponseEntity<EResponse<List<Vehicle>>> vehicleList(@RequestHeader(Url.AUTH_HEADER) String tokenKey) {

		EResponse<List<Vehicle>> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				/*
				 * Filter the vehicle list retrieved from vehicle Services to contain only
				 * vehicles related to the current owner
				 */
				base.setData(vehicleService
						.getVehicleList().stream().filter(
								vehicle -> (vehicle.getVehicleStatus() != null
										&& vehicle.getVehicleStatus().equalsIgnoreCase("active")
										&& vehicle.getOwner() != null
										&& vehicle.getOwner().getOwnerId().intValue() == Integer
												.parseInt(tokenService.getMember(tokenKey))))
						.collect(Collectors.toList()));
				base.success("Classes Listed ");
				base.setToken(tokenKey);
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
	 * @Return data : Requested Vehicle.
	 */
	@CrossOrigin
	@RequestMapping(value = "/{vehicleId}")
	public ResponseEntity<EResponse<Vehicle>> getVehicle(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@PathVariable Integer vehicleId) {

		EResponse<Vehicle> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				// get vehicle of the received vehicle Id & validate the owner
				base.setData(vehicleService.get(vehicleId).getOwner() != null && vehicleService.get(vehicleId)
						.getOwner().getOwnerId().intValue() == Integer.parseInt(tokenService.getMember(tokenKey))
								? vehicleService.get(vehicleId)
								: null);

				if (base.getData() != null)
					base.success("Vehicle Retrieved ");
				else
					base.fail("Unable to find Vehicle");
				base.setToken(tokenKey);
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
	 * @Return data : List of owner vehicles as JSON array..
	 */
	@CrossOrigin
	@RequestMapping(value = Url.VEHICLE_DELETE, method = RequestMethod.DELETE)
	public ResponseEntity<EResponse<Vehicle>> deleteVehicle(@RequestHeader(Url.AUTH_HEADER) String tokenKey,
			@PathVariable Integer vehicleId) {

		EResponse<Vehicle> base = new EResponse<>();
		try {
			if (tokenService.validateToken(tokenKey)) {

				// get vehicle of the received vehicle Id & validate the owner
				Vehicle vehicle = vehicleService.get(vehicleId).getOwner() != null && vehicleService.get(vehicleId)
						.getOwner().getOwnerId().intValue() == Integer.parseInt(tokenService.getMember(tokenKey))
								? vehicleService.get(vehicleId)
								: null;

				// check if vehicle exists for owner...
				if (vehicle != null)
					vehicleService.deleteVehicle(vehicle);
				
				base.setData(null);
				base.success("Vehicle Removed ");
				base.setToken(tokenKey);
			} else
				base.invalidToken();
		} catch (Exception ex) {
			base.exception(ex);
			reporterService.report(ex);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(base);

	}

}
