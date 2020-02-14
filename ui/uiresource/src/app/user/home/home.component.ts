import { Component, OnInit, HostListener, ViewChild, ElementRef, NgZone } from '@angular/core';
import { HomeService } from '../../home/home.service';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { EgozzService } from 'src/app/egozz.service';
import { MapsAPILoader, MouseEvent } from '@agm/core';
import { WebService } from 'src/app/web.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers : [DatePipe]
})
export class HomeComponent implements OnInit {

  constructor(private datePipe: DatePipe, private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone, private fb: FormBuilder, private service: EgozzService, private homeService: HomeService, private userService: UserService, private router: Router, private webService: WebService) { }
  vehicle: any = {};

  host: string = this.service.host;
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
          if (this.stage == 0) {
            this.startAddress = results[0].formatted_address;
            this.startLong = longitude;
            this.startLat = latitude;
          }
          else if (this.stage == 1) {
            this.endAddress = results[0].formatted_address;
            this.endLong = longitude;
            this.endLat = latitude;
          }

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

      
      this.setCurrentLocation();
    });
  }

  startAddress: string;
  startLat: any;
  startLong: any;
  endAddress: string;
  endLat: any;
  endLong: any;
  estDistance: number;
  minStartDate;
  minEndDate;

  initDate(){
    let today=new Date();
    this.minStartDate=this.datePipe.transform(today, 'yyyy-MM-dd');
    this.minEndDate=this.minStartDate;
  }

  startDateChanged(){
    this.minEndDate=this.tripData.get("startOn").value;
    this.tripData.patchValue({
      endOn : this.minEndDate
    });
  }

  btnLabel = "Proceed";
  stage: number = 0;
  toNextStage(nextStage: number) {
    if (nextStage == 1) {
      this.startAddress = this.address;
      this.startLong = this.longitude;
      this.startLat = this.latitude;
      this.setCurrentLocation();
      this.searchElementRef.nativeElement.value = "";
      window.scroll(0, 0);
    }
    else if (nextStage == 2) {
      this.endAddress = this.address;
      this.endLong = this.longitude;
      this.endLat = this.latitude;
      this.btnLabel = "Get Available Cars";
    }
    else if (nextStage == 3) {
      var origin = this.startLat + "," + this.startLong;
      var destination = this.endLat + "," + this.endLong;
      this.loading = true;
      new google.maps.DistanceMatrixService().getDistanceMatrix({ 'origins': [origin], 'destinations': [destination], travelMode: google.maps.TravelMode.DRIVING }, result => {
        var meter = result.rows[0].elements[0].distance.value;
        this.estDistance = (meter / 1000) * 2;
        console.log("origin "+origin);
        console.log("Destination "+destination);
        this.formSubmit();
      });
      this.vehicleLoaded = true;
    }
    this.stage = nextStage;
  }

  ngOnInit() {
    this.initDate();
    this.doForStartUp();
    this.checkWindowSize();
    this.token = localStorage.getItem("userToken");
    this.initialiseClass();
    this.vehicle.vehicleClass = {};
  }

  token: string;

  quit() {
    this.vehicleLoaded = false;
    this.stage = 0;
  }

  viewDetails(v: any) {
    this.vehicle = v;
    this.images = this.vehicle["images"];
    this.currentImage = this.images[0];
    if (this.images.length <= 1)
      this.updateImageFlag = false;
    this.currentIndex = 0;
    let modalBtn: HTMLElement = document.getElementById("viewInvoker") as HTMLElement;
    modalBtn.click();
  }

  bookNow(v: any) {
    this.vehicle = v;
    var params = "startDate=" + this.tripData.get("startOn").value
      + "&endDate=" + this.tripData.get("endOn").value
      + "&distance=" + this.estDistance
      + "&ac=" + this.ac
      + "&vehicleId=" + this.vehicle.vehicleId
      + "&charge=" + this.vehicle.charge;
    var formData = new FormData();
    formData.append("pickUpAddress", this.startAddress);
    formData.append("dropAddress", this.endAddress);
    this.loading = true;
    this.userService.book(this.token, params, formData, response => {
      this.loading = false;
      if (response["status"] == "success") {
        localStorage.setItem("currentUserNav", "trip");
        this.router.navigateByUrl("/home/trips");
        setInterval(() => {
          window.location.reload();
        }, 300);
      }
      else if (response["status"] == "unAuthorized")
        this.router.navigateByUrl("/");
      else
        this.failed(response["message"]);
    });
  }

  tripData: any = this.fb.group({
    startOn: ['', Validators.required],
    endOn: ['', Validators.required],
    vehicleClassIndex: ['', Validators.required],
    source: ['', Validators.required],
    dropAddress: ['', Validators.required],
    distance: ['', Validators.required],
    pickUpAddress: ['', Validators.required]
  });

  startOnError = "";
  endOnError = "";
  vehicleClassIndexError = "";
  sourceError = "";
  dropAddressError = "";
  distanceError = "";
  pickUpAddressError = "";
  startOnErrorFlag = false;
  endOnErrorFlag = false;
  vehicleClassIndexErrorFlag = false;
  sourceErrorFlag = false;
  dropAddressErrorFlag = false;
  distanceErrorFlag = false;
  pickUpAddressErrorFlag = false;

  shortWindow: boolean = true;
  ac = true;
  acSwitch = false;
  vClasses: any[] = [];
  vehicles: any[] = [];
  vehicleLoaded: boolean = false;
  noVehicle = false;

  validateForm(): boolean {
    var trip = this.tripData;
    if (trip.get("startOn").invalid) {
      this.startOnError = "Cannot be blank !";
      this.startOnErrorFlag = true;
      setInterval(() => {
        this.startOnErrorFlag = false;
        this.startOnError = "";
      }, 3000);
      return false;
    }

    if (trip.get("endOn").invalid) {
      this.endOnError = "Cannot be blank !";
      this.endOnErrorFlag = true;
      setInterval(() => {
        this.endOnErrorFlag = false;
        this.endOnError = "";
      }, 3000);
      return false;
    }

    return true;
  }

  logoLocation: string = this.service.host + "/files/egozz_ORG.png";
  darkLogo: string = this.service.host + "/files/egozz_ORG.png";
  formSubmit() {
    var params = "startDate=" + this.tripData.get("startOn").value
      + "&endDate=" + this.tripData.get("endOn").value
      + "&distance=" + this.estDistance
      + "&ac=" + this.ac
      + "&lat=" + this.startLat
      + "&lon=" + this.startLong
      + "&class=" + this.vClasses[this.selected].vehicleClassId;
    if (!this.validateForm()) {
      this.failed("Please Enter the dates !");
    }
    else {
      this.loading = true;
      this.userService.getVehicles(this.token, params, response => {
        this.loading = false;
        if (response["status"] == "success") {
          this.vehicles = response["data"];
          console.log(this.vehicles.length);
          if (this.vehicles.length <= 0){
            this.failed("Sorry ! No " + this.vClasses[this.selected].className + " available for the time near you!");
            this.quit();
          }
          else
            this.vehicleLoaded = true;
        }
        else if (response["status"] == "unAuthorized") {
          this.router.navigateByUrl("/");
        }
        else{
          let ms:string=""+response["message"];
          this.failed(ms);
          this.quit();
        }
      });
    }
  }

  @HostListener("window:resize", [])
  onWindowResize() {
    this.checkWindowSize();
  }

  initialiseClass() {
    this.homeService.getClassList(response => {
      if (response["status"] == "success") {
        this.vClasses = response["data"];
      }
    });
  }

  checkWindowSize() {
    var width = window.innerWidth;
    if (width < 700)
      this.shortWindow = true;
    else
      this.shortWindow = false;
  }

  switchAc() {
    if (this.ac)
      this.ac = false;
    else
      this.ac = true;
  }

  selected: number = -1;

  checkAcSwitch(index: number) {
    this.selected = index;
    this.acSwitch = this.vClasses[index].acSwitch;
    if (this.acSwitch)
      this.ac = true;
  }

  fail: boolean = false;
  msg: boolean = false;
  success: boolean = false;
  message: string="";
  loading: boolean = false;

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
    this.msg = false;
    this.success = false;
    this.fail = true;
    this.message = message;
    this.invokeAlert();
  }

  images: any[];
  currentImage: any = {};
  currentIndex: number = 0;
  updateImageFlag = true;

  next(): void {
    if (this.updateImageFlag) {
      if (this.currentIndex >= this.images.length - 1)
        this.currentIndex = 0;
      else
        this.currentIndex++;
      this.currentImage = this.images[this.currentIndex];
    }
  }

  previous(): void {
    if (this.updateImageFlag) {
      if (this.currentIndex <= 0)
        this.currentIndex = this.images.length - 1;
      else
        this.currentIndex--;
      this.currentImage = this.images[this.currentIndex];
    }
  }


}
