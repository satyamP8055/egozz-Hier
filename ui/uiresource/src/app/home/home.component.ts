import { Component, OnInit, HostListener, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HomeService } from './home.service';
import { Router } from '@angular/router';
import { EgozzService } from '../egozz.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  title = 'egozz';

  navFlag: any = false;
  showNav: any = false;
  darkNav: any = false;
  lightNav: any = true;
  loginBox: boolean = true;
  registerBox: boolean = false;
  otpSent: boolean = false;

  @ViewChild('catalogue', { static: false }) catalogue: ElementRef;
  @ViewChild('aboutUs', { static: false }) aboutUs: ElementRef;

  constructor(private service:EgozzService, private fb: FormBuilder, private homeService: HomeService, private router:Router) { }

  loginForm: any = this.fb.group({
    userName: [''],
    password: ['']
  });

  host:string=this.service.host;

  signUpForm = this.fb.group({
    userName: [''],
    mail: [''],
    fullName: [''],
    password: [''],
    confirmPassword: [''],
    otp: [''],
    phone:['']
  });

  getScrollValue(element: ElementRef) {
    const rect = element.nativeElement.getBoundingClientRect();
    return rect.top + window.pageYOffset - document.documentElement.clientTop;
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    this.checkScrolls();
  }

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

  checkScrolls() {
    const vertex = window.pageYOffset;
    var cat = this.getScrollValue(this.catalogue);
    var abt = this.getScrollValue(this.aboutUs);
    if (vertex >= abt && vertex < cat)
      this.highlightAboutUs();
    else
      if (vertex >= cat)
        this.highlightCatalogue();
      else
        this.highlightHomeCondition(vertex);
  }

  para: string = "";
  p = "Emphasized Gentle on Zen & Zappy Trips";

  ngOnInit() {
    this.currentClass.image={};
    this.initialiseClass();
    this.switchToLight();
    this.checkWindowSize();
    window.scroll(0, 0);
    this.vClasses[0] = { "image": {} };
  }

  shortWindow: boolean = false;
 
  phaseChange() {
    if (this.lightNav)
      this.switchToDark();
    else
      this.switchToLight();
  }

  loggedIn: boolean = false;
  token: string = null;
  logoLocation: string = this.service.host+"/files/logo_white.png";
  darkLogo: string = this.service.host+"/files/egozz_ORG.png";
  lightLogo: string = this.service.host+"/files/logo_white.png";
  vClasses: any[] = [];
  currentClass: any = {};
  currentClassIndex: number = 0;
  currentNav: string = "home";
  firstName: string = "Profile";
  last: number = 0;

  initialiseClass() {
    this.loading=true;
    this.homeService.getClassList(response => {
      if (response["status"] == "success") {
        this.loading=false;
        this.vClasses = response["data"];
        this.currentClass = this.vClasses[0];
        this.last = this.vClasses.length - 1;
        this.countDown();
      }
    });
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

  counter: number = 0;
  startedSlide: boolean = false;

  moto: string[] = ["Emphasized", "Gentle", "Zen", "Zappy"];
  stand: string = this.moto[0];
  motoIndex: number = 0;

  countDown() {
    setInterval(() => {
      var mg = this.p.split("");
      if (this.counter < mg.length) {
        this.para = this.para + mg[this.counter];
        this.counter = this.counter + 1;
      }
    }, 100);

    setInterval(() => {
      // Moto Slide...
      if (this.motoIndex >= this.moto.length - 1)
        this.motoIndex = 0;
      else
        this.motoIndex = this.motoIndex + 1;
      this.stand = this.moto[this.motoIndex];
    }, 2000);

    this.startSlide();
  }

  startSlide() {
    if (this.startedSlide) { }
    else {
      this.startedSlide = true;
      setInterval(() => {
        // Image Carousel ...
        if (this.currentClassIndex >= this.vClasses.length - 1)
          this.currentClassIndex = 0;
        else
          this.currentClassIndex = this.currentClassIndex + 1;
        this.currentClass = this.vClasses[this.currentClassIndex];
      }, 3000);
    }
  }

  homeClicked() {
    window.scroll(0, 0);
    this.highlightHome();
  }

  highlightHome() {
    this.currentNav = "home";
    this.showNav = false;
    this.navFlag = false;
    this.switchToLight();
    this.checkWindowSize();
  }

  highlightHomeCondition(vertex: number) {
    this.currentNav = "home";
    if (vertex < 50) {
      this.showNav = false;
      this.navFlag = false;
    }
    else {
      this.showNav = true;
      this.navFlag = true;
    }
    this.checkWindowSize();
    this.switchToLight();
  }

  aboutUsClicked() {
    var abt = this.getScrollValue(this.aboutUs);
    window.scroll(0, abt + 2);
    this.highlightAboutUs();
  }

  highlightAboutUs() {
    this.currentNav = "aboutUs";
    this.showNav = true;
    this.switchToDark();
  }

  catalogueClicked() {
    var catalogueOffset = this.getScrollValue(this.catalogue);
    window.scroll(0, catalogueOffset + 2);
    this.highlightCatalogue();
  }

  highlightCatalogue() {
    this.currentNav = "classes";
    this.showNav = true;
    this.navFlag = true;
    this.switchToLight();
  }

  next() {
    if (this.currentClassIndex >= this.vClasses.length - 1)
      this.currentClassIndex = 0;
    else
      this.currentClassIndex = this.currentClassIndex + 1;
    this.currentClass = this.vClasses[this.currentClassIndex];
  }

  previous() {
    if (this.currentClassIndex <= 0)
      this.currentClassIndex = this.last;
    else
      this.currentClassIndex = this.currentClassIndex - 1;
    this.currentClass = this.vClasses[this.currentClassIndex];
  }

  switchToRegister() {
    this.loginBox = false;
    this.registerBox = true;
  }

  switchToLogin() {
    this.registerBox = false;
    this.loginBox = true;
  }

  loginNow() {
    let dismissModal: HTMLElement = document.getElementById("dismissModalHere") as HTMLElement;
    dismissModal.click();
    var loginData=new FormData();
    loginData.append("userName", this.loginForm.get("userName").value);
    loginData.append("password", this.loginForm.get("password").value);
    this.loading=true;
    this.homeService.login(loginData, response=>{
      this.loading=false;
      if(response["status"]=="success"){
        this.router.navigateByUrl("/home");
        localStorage.setItem("userToken",response["token"]);
      }
      else
        this.failed(response["message"]);
    });
  }


  submitRegister() {
    let dismissModal: HTMLElement = document.getElementById("dismissModalHere") as HTMLElement;
    dismissModal.click();
    if (this.otpSent) {
      this.loading=true;
      this.homeService.signUp(this.signUpForm.get("otp").value, this.token, (response) => {
        this.loading=false;
        if (response["status"] == "success"){
          this.router.navigateByUrl("/home");
          localStorage.setItem("userToken",response["token"]);
        }
        else
          this.failed(response["message"]);
      });
    }
    else {
      this.loading=true;
      var formData = new FormData();
      formData.append("userName", this.signUpForm.get("userName").value);
      formData.append("fullName", this.signUpForm.get("fullName").value);
      formData.append("mail", this.signUpForm.get("mail").value);
      formData.append("password", this.signUpForm.get("password").value);
      formData.append("phone", this.signUpForm.get("phone").value);
      this.homeService.sendOtp(formData, response => {
        this.loading=false;
        if (response["status"] == "success") {
          this.token = response["token"];
          this.otpSent = true;
          let openFormModal: HTMLElement = document.getElementById("openFormModalHere") as HTMLElement;
          openFormModal.click();
        }
        else
          this.failed(response["message"]);
      });
    }
  }

  signUp() {
    this.succeed("Registration Success !");
  }

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