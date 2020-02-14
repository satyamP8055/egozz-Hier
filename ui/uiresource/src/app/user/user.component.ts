import { Component, OnInit, HostListener, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { UserService} from './user.service';
import { EgozzService } from '../egozz.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor(private router:Router, private service:EgozzService, private userService:UserService) { }

  ngOnInit() {
    this.checkWindowSize();
    var current=localStorage.getItem("currentUserNav");
    if(current==null || current=="null" || current==undefined){}
    else 
      this.currentNav=current;
      if(current=="aboutUs"){
        this.switchToDark();
        this.showFooter=false;
      }
      if(current=="trip"){
        this.switchToDark();
      }
    window.scroll(0, 0);
    this.token=localStorage.getItem("userToken");
  }

  token:string;
  showNav:boolean=true;
  loggedIn: boolean = true;
  darkNav: any = false;
  lightNav: any = true;
  logoLocation: string = this.service.host+"/files/egozz_ORG.png";
  darkLogo: string = this.service.host+"/files/egozz_ORG.png";
  lightLogo: string = this.service.host+"/files/logo_white.png";
  firstName: string = "Profile";
  currentNav="home";
  shortWindow:boolean=true;
  showFooter=true;

  @HostListener("window:resize", [])
  onWindowResize() {
    this.checkWindowSize();
  }
  switchToLight() {
    this.darkNav = false;
    this.lightNav = true;
    this.logoLocation = this.darkLogo;
  }

  switchToDark() {
    this.darkNav = true;
    this.lightNav = false;
    this.logoLocation = this.lightLogo;
  }

  getScrollValue(element: ElementRef) {
    const rect = element.nativeElement.getBoundingClientRect();
    return rect.top + window.pageYOffset - document.documentElement.clientTop;
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    this.checkScrolls();
  }

  checkWindowSize() {
    var width = window.innerWidth;
    if (width < 700)
      this.shortWindow = true;
    else
      this.shortWindow = false;
  }

  checkScrolls() {
    const vertex = window.pageYOffset;
  }

  homeClicked() {
    this.currentNav = "home";
    this.switchToLight();
    this.checkWindowSize();
    localStorage.setItem("currentUserNav", this.currentNav);
    this.router.navigateByUrl("/home");
    this.showFooter=true;
  }

  aboutUsClicked() {
    this.currentNav = "aboutUs";
    this.switchToDark();
    localStorage.setItem("currentUserNav", this.currentNav);
    this.router.navigateByUrl("/home/aboutUs");
    this.showFooter=false;
  }

  catalogueClicked() {
    this.currentNav = "classes";
    this.switchToLight();
    localStorage.setItem("currentUserNav", this.currentNav);
    this.router.navigateByUrl("/home/catalogue");
    this.showFooter=true;
  }

  tripClicked() {
    this.currentNav = "trip";
    this.switchToDark();
    localStorage.setItem("currentUserNav", this.currentNav);
    this.showFooter=true;
    this.router.navigateByUrl("/home/trips");
  }
  
  profileClicked() {
    this.currentNav = "profile";
    this.switchToLight();
    localStorage.setItem("currentUserNav", this.currentNav);
    this.router.navigateByUrl("/home/profile");
    this.showFooter=true;
  }

  logoutClicked(){
    localStorage.setItem("currentUserNav",null);
    this.userService.logout(this.token,response=>{
      this.router.navigateByUrl("/");
    });
  }

}
