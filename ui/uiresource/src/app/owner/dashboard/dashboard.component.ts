import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { Router } from '@angular/router';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private dashboardService: DashboardService, private service: EgozzService, private router: Router) { }

  token: string;

  ngOnInit() {
    var fullName = localStorage.getItem("ownerFullName");
    this.firstName = fullName.split(" ")[0];
    this.profile = true;
    this.token = localStorage.getItem("ownerToken");
    this.getElements(() => {
      if (localStorage.getItem("activeSide") == null || localStorage.getItem("activeSide") == "null" || localStorage.getItem("activeSide") == undefined) {
        if (localStorage.getItem("activeNav") == null || localStorage.getItem("activeNav") == "null" || localStorage.getItem("activeNav") == undefined) {
          this.selectElement(this.elements[0]);
        }
        else
          this.selectNav(localStorage.getItem("activeNav"));
      }
      else {
        var index = localStorage.getItem("activeSide");
        var tem = this.elements.filter(e => (e.vehicleId == index))[0];
        if (tem == undefined) {
          var t = this.elements.filter(e => (e.label == index))[0];
          this.selectElement(t);
        }
        else
          this.selectElement(tem);
      }
    });
  }

  currentNav: any;
  elements: any[] = [];
  vehicles: any[];
  sideBar: boolean = true;
  profile: any;
  firstName: string;
  currentElement: any;

  logoLocation: string = this.service.host + "/files/logo_white.png";

  sideBarToggle(): void {
    if (this.sideBar) {
      this.sideBar = false;
    }
    else
      this.sideBar = true;
  }

  selectElement(element) {
    localStorage.setItem("activeNav", null);
    if (element.vehicleId == null || element.vehicleId == undefined || element.vehicleId == "null" || element.vehicleId == "undefined") {
      localStorage.setItem("activeSide", element.label);
    }
    else
      localStorage.setItem("activeSide", element.vehicleId);
    this.currentNav = null;
    this.currentElement = element;
  }

  selectNav(navElement) {
    localStorage.setItem("activeNav", navElement);
    localStorage.setItem("activeSide", null);
    this.currentElement = null;
    this.currentNav = navElement;
  }

  getElements(callback): void {
    this.dashboardService.getVehicles(this.token).subscribe(response => {
      if (response["status"] == "success") {
        var list = response["data"];
        var i = 0;
        for (i = 0; i < list.length; i++) {
          this.elements[i + 3] = {
            vehicleId: list[i].vehicleId,
            label: list[i].vehicleName,
            url: "vehicle-detail/" + list[i].vehicleId
          };
        }
        this.vehicles = list;
        this.elements[0] = {
          label: "Home",
          url: "/owner/dashboard"
        };
        this.elements[1] = {
          label: "Trips",
          url: "trips"
        };
        this.elements[2] = {
          label: "Transactions",
          url: "transactions"
        };
        callback();
      }
    });
  }

  logout(): void {
    this.dashboardService.doLogout(this.token).subscribe(response => {
      localStorage.setItem("activeNav", null);
      localStorage.setItem("activeSide", null);
      this.router.navigateByUrl("/owner");
    });
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
