import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { VehicleDetailService } from './vehicle-detail.service';
import { AddVehicleService } from '../add-vehicle/add-vehicle.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-vehicle-detail',
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.css']
})
export class VehicleDetailComponent implements OnInit {

  constructor(private router: Router, private service:EgozzService, private route: ActivatedRoute, private vehicleService: VehicleDetailService, private addVehicleService: AddVehicleService) {
    this.countdown();
    route.params.subscribe(val => this.ngOnInit());
  }

  token: string;
  vehicleId: number;
  vehicle: any = {
    vehicleClass: {}
  };
  images: any[];
  currentImage: any = {};
  currentIndex: number = 0;
  updateImageFlag = true;

  ngOnInit() {
    this.token = localStorage.getItem("ownerToken");
    this.getVehicle();
  }

  opacity: any = 1;
  countDownNotStarted: boolean = true;

  countdown() {
    if (this.updateImageFlag && this.countDownNotStarted) {
      setInterval(() => {
        this.countDownNotStarted = false;
        if (this.currentIndex >= this.images.length - 1)
          this.currentIndex = 0;
        else
          this.currentIndex++;
        this.currentImage = this.images[this.currentIndex];
      }, 3000);
    }
  }


  logoLocation: string = this.service.host+"/files/egozz_ORG.png";
  host:string=this.service.host;
  getVehicle(): void {
    this.currentIndex = 0;
    this.images = [];
    this.vehicleId = +this.route.snapshot.paramMap.get("vehicleId");
    this.vehicleService.fetchVehicle(this.token, this.vehicleId).subscribe(response => {
      if (response["status"] == "success") {
        this.vehicle = response["data"];
        this.images = this.vehicle["images"];
        this.currentImage = this.images[0];
      }
      else
        this.failed(response["message"]);
    });
  }

  next(): void {
    if (this.images.length > 1) {
      if (this.currentIndex >= this.images.length - 1)
        this.currentIndex = 0;
      else
        this.currentIndex++;
      this.currentImage = this.images[this.currentIndex];
    }
  }

  previous(): void {
    if (this.images.length > 1) {
      if (this.currentIndex <= 0)
        this.currentIndex = this.images.length - 1;
      else
        this.currentIndex--;
      this.currentImage = this.images[this.currentIndex];
    }
  }

  editClicked(): void {
    this.loading = true;
    this.router.navigateByUrl("/owner/dashboard/vehicle-update/" + this.vehicleId);
  }

  deleteVehicle(): void {
    this.vehicleService.deleteVehicle(this.token, this.vehicleId).subscribe(response => {
      if (response["status"] == "success") {
        this.loading = true;
        this.succeed(response["message"]);
        setInterval(() => {
          this.router.navigateByUrl("/owner/dashboard");
          setInterval(() => {
            this.loading = true;
            localStorage.setItem("activeNav", "profile");
            localStorage.setItem("activeSide", null);
            window.location.reload();
          }, 100);
        }, 3000);
      }
      else
        this.failed(response["message"]);
    });
  }

  loading: boolean = false;
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
