import { Component, OnInit, HostListener } from '@angular/core';
import { UserService } from '../user.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(private userService: UserService, private service:EgozzService) { }

  ngOnInit() {
    this.checkWindowSize();
    this.token=localStorage.getItem("userToken");
    this.getProfile();
  }

  getProfile(){
    this.loading=true;
    this.userService.getProfile(this.token, response=>{
      this.loading=false;
      if(response["status"]=="success")
        this.profile=response["data"];
      else
        this.failed(response["message"]); 
    });
  }

  logoLocation:string = this.service.host+"/files/egozz_ORG.png";
  lightLogo: string = this.service.host+"/files/logo_white.png";
  profile:any={};

  token:string;
  @HostListener("window:resize", [])
  onWindowResize() {
    this.checkWindowSize();
  }
  checkWindowSize() {
    var width = window.innerWidth;
    if (width < 700)
      this.shortWindow = true;
    else
      this.shortWindow = false;
  }

  shortWindow:boolean=false;

  fail: boolean = false;
  msg: boolean = true;
  success: boolean = false;
  message: string;
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
    this.message = message;
    this.msg = false;
    this.success = false;
    this.fail = true;
    this.invokeAlert();
  }

}
