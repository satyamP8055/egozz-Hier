import { Injectable, OnInit } from '@angular/core';
import {WebService} from '../web.service';
import { EgozzService } from '../egozz.service';

@Injectable({
  providedIn: 'root'
})
export class HomeService implements OnInit{

  constructor(private webService:WebService, private service:EgozzService){}

  ngOnInit(){}


  private vehicleClassListUrl=this.service.host+"/api/vehicle/class/list";
  private otpUrl=this.service.host+"/api/user/verify";
  private signUpUrl=this.service.host+"/api/user/signup";
  private loginUrl=this.service.host+"/api/user/login";

  public getClassList(callback){
    this.webService.sendGetRequest(this.vehicleClassListUrl).subscribe(response=>{
      callback(response);
    });
  }

  public sendOtp(userData, callback){
    this.webService.sendPostRequest(this.otpUrl,userData).subscribe(response=>callback(response));
  }

  public login(userData, callback){
    this.webService.sendPostRequest(this.loginUrl,userData).subscribe(response=>callback(response));
  }

  public signUp(otp:string, token:string, callback){
    var url=this.signUpUrl+"?otp="+otp;
    this.webService.sendGetRequestWithToken(url,token).subscribe(response=>callback(response));
  }

}
