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

  constructor(private dashboardService: DashboardService, private router: Router, private service:EgozzService) { }

  token: string;

  firstName: string;
  access: any[];

  currentAccess: any;

  sideBar: boolean = false;
  profile: any;

  logoLocation: string = this.service.host+"/files/logo_white.png";

  ngOnInit() {
    this.token = localStorage.getItem("token");
    this.getAccess();
    this.selectDivision(undefined);
  }

  sideBarToggle(): void {
    if (this.sideBar) {
      this.sideBar = false;
    }
    else
      this.sideBar = true;
  }

  getAccess(): void {
    this.dashboardService.getAccessList(this.token).subscribe(response => {
      if (response["status"] == "success") {
        this.access = response["data"];
        if (this.access.length > 0)
          this.sideBar = true;
      }
      else
        console.log(response);
    });
  }

  logout() {
    this.dashboardService.doAdminLogout(this.token).subscribe(response => {
      this.router.navigateByUrl("/admin");
    });
  }

  selectDivision(ac) {
    if(ac==undefined || ac=="undefined"){
      this.currentAccess=localStorage.getItem("currentAccess");
    }
    else{
      this.currentAccess = ac.accessName;
      localStorage.setItem("currentAccess",ac.accessName);
    }
  }

}
