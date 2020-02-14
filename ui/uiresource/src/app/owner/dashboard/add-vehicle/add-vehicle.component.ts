import { Component, OnInit, ViewChild, ElementRef, NgZone  } from '@angular/core';
import { AddVehicleService } from './add-vehicle.service';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { EgozzService } from 'src/app/egozz.service';
import { MapsAPILoader, MouseEvent } from '@agm/core';
import { WebService } from 'src/app/web.service';

@Component({
  selector: 'app-add-vehicle',
  templateUrl: './add-vehicle.component.html',
  styleUrls: ['./add-vehicle.component.css']
})
export class AddVehicleComponent implements OnInit {

  token: string;
 
  constructor(private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone, private addVehicleService: AddVehicleService, private service:EgozzService, private fb: FormBuilder, private router: Router) { }
 //MAP STARTS HERE
 latitude: number;
 longitude: number;
 zoom: number;
 address: string;
 private geoCoder;

 @ViewChild('search', { static: false })
 public searchElementRef: ElementRef;


 // Get Current Location Coordinates
 private setCurrentLocation() {
   if ('geolocation' in navigator) {
     navigator.geolocation.getCurrentPosition((position) => {
       this.latitude = position.coords.latitude;
       this.longitude = position.coords.longitude;
       this.zoom = 8;
       this.getAddress(this.latitude, this.longitude);
     });
   }
 }

 markerDragEnd($event: MouseEvent) {
   this.latitude = $event.coords.lat;
   this.longitude = $event.coords.lng;
   this.getAddress(this.latitude, this.longitude);
 }

 getAddress(latitude, longitude) {
   this.geoCoder.geocode({ 'location': { lat: latitude, lng: longitude } }, (results, status) => {
     if (status === 'OK') {
       if (results[0]) {
         this.zoom = 12;
         this.address = results[0].formatted_address;
       } else {
         window.alert('No results found');
       }
     } else {
       window.alert('Geocoder failed due to: ' + status);
     }

   });
 }

 doForStartUp() {
   //load Places Autocomplete
   this.mapsAPILoader.load().then(() => {
     this.setCurrentLocation();
     this.geoCoder = new google.maps.Geocoder;

     let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
       types: ["address"]
     });
     autocomplete.addListener("place_changed", () => {
       this.ngZone.run(() => {
         //get the place result
         let place: google.maps.places.PlaceResult = autocomplete.getPlace();

         //verify result
         if (place.geometry === undefined || place.geometry === null) {
           return;
         }

         //set latitude, longitude, address and zoom
         this.latitude = place.geometry.location.lat();
         this.longitude = place.geometry.location.lng();
         this.getAddress(this.latitude, this.longitude);
         this.zoom = 12;
       });
     });
   });
 }

  ngOnInit() {
    this.doForStartUp();
    this.token = localStorage.getItem("ownerToken");
    this.initialiseClassList();
    this.addValidators();
    this.setCurrentLocation();
  }

  addressToBeSelected:boolean=true;

  addressSelected(){
    this.addressToBeSelected=false;
  }

  classList: any;
  acSwitch: boolean;
  classElement: any;

  form = this.fb.group({
    acDayCharge: [''],
    acKmsCharge: [''],
    acMileage: [''],
    defaultDayCharge: [''],
    defaultKmsCharge: [''],
    defaultMileage: [''],
    kmsSwitch: [''],
    vehicleColor: [''],
    vehicleName: [''],
    vehicleNumber: [''],
    vehicleRemarks: [''],
    vehicleClassIndex: [''],
    imageFiles: [null]
  });


  acDayChargeError: any;
  acKmsChargeError: any;
  acMileageError: any;
  defaultDayChargeError: any;
  defaultKmsChargeError: any;
  defaultMileageError: any;
  kmsSwitchError: any;
  vehicleColorError: any;
  vehicleNameError: any;
  vehicleNumberError: any;
  vehicleRemarksError: any;
  vehicleClassIndexError: any;
  vehicleImageError: any;

  data: any;

  myFiles: string[] = [];

  saveFile(event) {
    this.data = (event.target as HTMLInputElement).files[0];
    for (var t = 0; t < event.target.files.length; t++) {
      this.myFiles.push(event.target.files[t]);
    }
    this.form.patchValue({
      imageFiles: this.data
    });
    this.form.get("imageFiles").updateValueAndValidity();
  }

  logoLocation: string = this.service.host+"/files/egozz_ORG.png";


  addValidators(): void {
    this.form.controls["vehicleName"]
      .setValidators([
        Validators.pattern('^[a-zA-Z0-9 ]+$'),
        Validators.required]);
    this.form.controls["vehicleColor"]
      .setValidators([
        Validators.pattern('^[a-zA-Z]+$'),
        Validators.maxLength(30),
        Validators.required]);
    this.form.controls["kmsSwitch"]
      .setValidators([
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$')
      ])
    this.form.controls["vehicleNumber"]
      .setValidators([
        Validators.required,
        Validators.pattern('^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$')
      ]);
    this.form.controls["vehicleClassIndex"]
      .setValidators([
        Validators.required
      ]);
  }

  private initialiseClassList(): void {
    this.addVehicleService.getClassList(this.token).subscribe(response => {
      if (response["status"] == "success")
        this.classList = response["data"];
      else
        this.failed(response["message"]);
    });
  }

  loading: boolean = false;

  formSubmit(): void {
    if (this.validateForm()) {
      var vehicleData: any = new FormData();
      vehicleData.append("vehicleName", this.form.get("vehicleName").value);
      vehicleData.append("vehicleColor", this.form.get("vehicleColor").value);
      vehicleData.append("vehicleNumber", this.form.get("vehicleNumber").value);
      vehicleData.append("kmsSwitch", this.form.get("kmsSwitch").value);
      vehicleData.append("vehicleClassId", this.classElement["vehicleClassId"]);
      vehicleData.append("acDayCharge", this.form.get("acDayCharge").value);
      vehicleData.append("acKmsCharge", this.form.get("acKmsCharge").value);
      vehicleData.append("acMileage", this.form.get("acMileage").value);
      vehicleData.append("defaultDayCharge", this.form.get("defaultDayCharge").value);
      vehicleData.append("defaultKmsCharge", this.form.get("defaultKmsCharge").value);
      vehicleData.append("defaultMileage", this.form.get("defaultMileage").value);
      vehicleData.append("vehicleRemarks", this.form.get("vehicleRemarks").value);
      vehicleData.append("latitude",this.latitude);
      vehicleData.append("longitude", this.longitude);
      vehicleData.append("address", this.address);
      for (var t = 0; t < this.myFiles.length; t++) {
        vehicleData.append("vehicleImages", this.myFiles[t]);
      }
      this.loading = true;
      this.addVehicleService.addVehicle(this.token, vehicleData).subscribe(response => {
        if (response["status"] == "success") {
          var vehicle = response["data"];
          localStorage.setItem("message", vehicle.vehicleName + " added Successfully !");
          this.router.navigateByUrl("/owner/dashboard/vehicle-detail/" + vehicle.vehicleId);
          localStorage.setItem("activeNav", null);
          var element=response["data"];
          localStorage.setItem("activeSide", element.vehicleId);
          setInterval(() => {
            window.location.reload();
          }, 1000);
        }
        else {
          this.loading = false;
          this.failed(response["message"]);
        }
      });
    }
  }

  validateForm(): boolean {
    var vehicle = this.form;
    if (vehicle.get("vehicleName").invalid) {
      this.vehicleNameError = "Invalid Vehicle Name";
      setInterval(() => {
        this.vehicleNameError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("vehicleColor").invalid) {
      this.vehicleColorError = "Invalid Vehicle Color";
      setInterval(() => {
        this.vehicleColorError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("vehicleNumber").invalid) {
      this.vehicleNumberError = "Please Enter Number as ST 01 AL 4563";
      setInterval(() => {
        this.vehicleNumberError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("kmsSwitch").invalid) {
      this.kmsSwitchError = "Invalid Data !";
      setInterval(() => {
        this.kmsSwitchError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("vehicleClassIndex").invalid) {
      this.vehicleClassIndexError = "Vehicle Class can't be Blank !";
      setInterval(() => {
        this.vehicleClassIndexError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("acDayCharge").invalid) {
      this.acDayChargeError = "Should be between " + this.classElement["minAcDayCharge"] +
        " and " + this.classElement["maxAcDayCharge"];
      setInterval(() => {
        this.acDayChargeError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("acKmsCharge").invalid) {
      this.acKmsChargeError = "Should be between " + this.classElement["minAcKmsCharge"] +
        " and " + this.classElement["maxAcKmsCharge"];
      setInterval(() => {
        this.acKmsChargeError = null;
      }, 3000);
      return false;
    }
    if (vehicle.get("acMileage").invalid) {
      this.acMileageError = "Should be between " + this.classElement["minAcMileage"] +
        " and " + this.classElement["maxAcMileage"];
      setInterval(() => {
        this.acMileageError = null;
      }, 3000);
      return false;
    }
    if (this.acSwitch && vehicle.get("defaultDayCharge").invalid) {
      this.defaultDayChargeError = "Should be between " + this.classElement["minDefaultDayCharge"] +
        " and " + this.classElement["maxDefaultDayCharge"];
      setInterval(() => {
        this.defaultDayChargeError = null;
      }, 3000);
      return false;
    }
    if (this.acSwitch && vehicle.get("defaultKmsCharge").invalid) {
      this.defaultKmsChargeError = "Should be between " + this.classElement["minDefaultKmsCharge"] +
        " and " + this.classElement["maxDefaultKmsCharge"];
      setInterval(() => {
        this.defaultKmsChargeError = null;
      }, 3000);
      return false;
    }
    if (this.acSwitch && vehicle.get("defaultMileage").invalid) {
      this.defaultMileageError = "Should be between " + this.classElement["minDefaultMileage"] +
        " and " + this.classElement["maxDefaultMileage"];
      setInterval(() => {
        this.defaultMileageError = null;
      }, 3000);
      return false;
    }
    if (this.data == undefined) {
      this.vehicleImageError = "No image uploaded !";
      setInterval(() => {
        this.vehicleImageError = null;
      }, 3000);
      return false;
    }
    return true;
  }

  checkAcSwitch(): void {
    this.classElement = this.classList[this.form.get("vehicleClassIndex").value];
    this.acSwitch = this.classElement["acSwitch"];
    this.form.controls["acDayCharge"]
      .setValidators([
        Validators.min(this.classElement["minAcDayCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcDayCharge"])]);
    this.form.controls["acKmsCharge"]
      .setValidators([
        Validators.min(this.classElement["minAcKmsCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcKmsCharge"])]);
    this.form.controls["acMileage"]
      .setValidators([
        Validators.min(this.classElement["minAcMileage"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcMileage"])]);
    this.form.controls["defaultDayCharge"]
      .setValidators([
        Validators.min(this.classElement["minDefaultDayCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultDayCharge"])]);
    this.form.controls["defaultKmsCharge"]
      .setValidators([
        Validators.min(this.classElement["minDefaultKmsCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultKmsCharge"])]);
    this.form.controls["defaultMileage"]
      .setValidators([
        Validators.min(this.classElement["minDefaultMileage"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultMileage"])]);
  }


  fail: boolean = false;
  msg: boolean = true;
  success: boolean = false;
  message: string;

  invokeAlert(): void {

    let modalBtn: HTMLElement = document.getElementById("alertInvoker") as HTMLElement;
    modalBtn.click();
  }

  alert(message: string): void {
    this.message = message;
    this.msg = true;
    this.success = false;
    this.fail = false;
    this.invokeAlert();
  }

  succeed(message: string): void {
    this.message = message;
    this.msg = false;
    this.success = true;
    this.fail = false;
    this.invokeAlert();
  }

  failed(message: string): void {
    this.message = message;
    this.msg = false;
    this.success = false;
    this.fail = true;
    this.invokeAlert();
  }


}
