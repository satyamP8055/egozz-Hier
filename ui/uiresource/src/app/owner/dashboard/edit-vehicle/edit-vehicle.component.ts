import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { VehicleDetailService } from '../vehicle-detail/vehicle-detail.service';
import { AddVehicleService } from '../add-vehicle/add-vehicle.service';
import { FormBuilder, Validators } from '@angular/forms';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-edit-vehicle',
  templateUrl: './edit-vehicle.component.html',
  styleUrls: ['./edit-vehicle.component.css']
})
export class EditVehicleComponent implements OnInit {

  constructor(private router: Router, private service:EgozzService, private fb: FormBuilder, private route: ActivatedRoute, private vehicleService: VehicleDetailService, private addVehicleService: AddVehicleService) {
    route.params.subscribe(val => this.ngOnInit());
  }

  token: string;
  vehicleId: number;
  vehicle: any = {
    vehicleClass: {}
  };
  currentImage: any = {};
  currentIndex: number = 0;
  updateImageFlag = true;

  ngOnInit() {
    this.token = localStorage.getItem("ownerToken");
    this.getVehicle();
  }

  getVehicle(): void {
    this.currentIndex = 0;
    this.vehicleId = +this.route.snapshot.paramMap.get("vehicleId");
    this.vehicleService.fetchVehicle(this.token, this.vehicleId).subscribe(response => {
      if (response["status"] == "success") {
        this.vehicle = response["data"];
        this.startotherOperations();
      }
      else
        this.failed(response["message"]);
    });
  }

  startotherOperations(): void {
    this.initialiseClassList();
    this.addValidators();
  }

  classList: any;
  acSwitch: boolean;
  classElement: any = {};

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

  myFiles:string[] = [];

  saveFile(event) {
    this.data = (event.target as HTMLInputElement).files[0];
    this.form.patchValue({
      imageFiles: this.data
    });
    for(var t=0;t<event.target.files.length;t++){
      this.myFiles.push(event.target.files[t]);
    }
  }

  logoLocation: string = this.service.host+"/files/egozz_ORG.png";

  addValidators(): void {
    this.form.controls["vehicleName"]
      .setValidators([
        Validators.pattern('^[a-zA-Z0-9 ]+$'),
        Validators.required]);
    this.form.controls["vehicleName"].updateValueAndValidity();

    this.form.controls["vehicleColor"]
      .setValidators([
        Validators.pattern('^[a-zA-Z]+$'),
        Validators.maxLength(30),
        Validators.required]);
    this.form.controls["vehicleColor"].updateValueAndValidity();

    this.form.controls["kmsSwitch"]
      .setValidators([
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$')
      ])
    this.form.controls["kmsSwitch"].updateValueAndValidity();

  }

  private initialiseClassList(): void {
    this.addVehicleService.getClassList(this.token).subscribe(response => {
      if (response["status"] == "success") {
        this.classList = response["data"];
        for (var c = 0; c < this.classList.length; c++) {
          if (this.classList[c].vehicleClassId == this.vehicle.vehicleClass.vehicleClassId) {
            this.classElement = this.classList[c];
            this.initialiseForms();
            this.checkAcSwitch();
          }
          else {
          }
        }
      }
      else
        this.failed(response["message"]);
    });
  }

  initialiseForms() {
    this.form.patchValue({
      acDayCharge: this.vehicle.acDayCharge,
      acKmsCharge: this.vehicle.acKmsCharge,
      acMileage: this.vehicle.acMileage,
      kmsSwitch: this.vehicle.kmsSwitch,
      vehicleColor: this.vehicle.vehicleColor,
      vehicleName: this.vehicle.vehicleName,
      vehicleNumber: this.vehicle.vehicleNumber,
      vehicleRemarks: this.vehicle.vehicleRemarks
    });
    if (this.classElement.acSwitch) {
      this.form.patchValue({
        defaultDayCharge: this.vehicle.defaultDayCharge,
        defaultKmsCharge: this.vehicle.defaultKmsCharge,
        defaultMileage: this.vehicle.defaultMileage
      });
    }
  }

  loading: boolean = false;

  formSubmit(event: Event): any {
    event.preventDefault();
    if (this.validateForm()) {
      var vehicleData: any = new FormData();
      vehicleData.append("vehicleName", this.form.get("vehicleName").value);
      vehicleData.append("vehicleColor", this.form.get("vehicleColor").value);
      vehicleData.append("vehicleNumber", this.vehicle.vehicleNumber);
      vehicleData.append("vehicleStatus", this.vehicle.vehicleStatus);
      vehicleData.append("kmsSwitch", this.form.get("kmsSwitch").value);
      vehicleData.append("vehicleClassId", this.classElement["vehicleClassId"]);
      vehicleData.append("acDayCharge", this.form.get("acDayCharge").value);
      vehicleData.append("acKmsCharge", this.form.get("acKmsCharge").value);
      vehicleData.append("acMileage", this.form.get("acMileage").value);
      vehicleData.append("defaultDayCharge", this.form.get("defaultDayCharge").value);
      vehicleData.append("defaultKmsCharge", this.form.get("defaultKmsCharge").value);
      vehicleData.append("defaultMileage", this.form.get("defaultMileage").value);
      vehicleData.append("vehicleRemarks", this.form.get("vehicleRemarks").value);
      for(var t=0;t<this.myFiles.length;t++){
        vehicleData.append("vehicleImages",this.myFiles[t]);
      }
      this.loading = true;
      this.addVehicleService.updateVehicle(this.token, vehicleData, this.vehicleId).subscribe(response => {
        if (response["status"] == "success") {
          localStorage.setItem("message", response["message"]);
          this.router.navigateByUrl("/owner/dashboard/vehicle-detail/" + this.vehicle.vehicleId);
          setInterval(() => {
            localStorage.setItem("activeNav", "profile");
            localStorage.setItem("activeSide", null);
            window.location.reload();
          }, 1000);
        }
        else {
          this.loading = false;
          this.failed(response["message"]);
        }
      });
    }
    return false;
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
    return true;
  }

  checkAcSwitch(): void {
    this.acSwitch = this.classElement["acSwitch"];
    this.form.controls["acDayCharge"]
      .setValidators([
        Validators.min(this.classElement["minAcDayCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcDayCharge"])]);
    this.form.controls["acDayCharge"].updateValueAndValidity();

    this.form.controls["acKmsCharge"]
      .setValidators([
        Validators.min(this.classElement["minAcKmsCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcKmsCharge"])]);
    this.form.controls["acKmsCharge"].updateValueAndValidity();

    this.form.controls["acMileage"]
      .setValidators([
        Validators.min(this.classElement["minAcMileage"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxAcMileage"])]);
    this.form.controls["acMileage"].updateValueAndValidity();

    this.form.controls["defaultDayCharge"]
      .setValidators([
        Validators.min(this.classElement["minDefaultDayCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultDayCharge"])]);
    this.form.controls["defaultDayCharge"].updateValueAndValidity();

    this.form.controls["defaultKmsCharge"]
      .setValidators([
        Validators.min(this.classElement["minDefaultKmsCharge"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultKmsCharge"])]);
    this.form.controls["defaultKmsCharge"].updateValueAndValidity();

    this.form.controls["defaultMileage"]
      .setValidators([
        Validators.min(this.classElement["minDefaultMileage"]),
        Validators.required,
        Validators.pattern('^([1-9][0-9]*)$'),
        Validators.max(this.classElement["maxDefaultMileage"])]);
    this.form.controls["defaultMileage"].updateValueAndValidity();

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
