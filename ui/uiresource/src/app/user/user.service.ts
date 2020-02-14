import { Injectable } from '@angular/core';
import { WebService } from '../web.service';
import { EgozzService } from '../egozz.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private webService:WebService, private service:EgozzService) { }

  host:string=this.service.host;

  postReview(tripId, star, review, token, callback){
    let url:string=this.host+"/api/trip/user/review?key="+tripId+"&star="+star+"&review="+review;
    this.webService.sendGetRequestWithToken(url, token).subscribe(response=>{
      callback(response);
    });
  }

  logout(token:string, callback){
    var logoutUrl=this.host+"/api/user/logout";
    this.webService.sendGetRequestWithToken(logoutUrl, token).subscribe(response=>{
      callback(response);
    });
  }

  getVehicles(token:string, params:string, callback){
    var vehicleUrl=this.host+"/api/trip/vehicles/available?"+params;
    this.webService.sendGetRequestWithToken(vehicleUrl,token).subscribe(response=>{
      callback(response);
    });
  }

  book(token:string, params:string, formData:any, callback){
    var vehicleUrl=this.host+"/api/trip/user/book?"+params;
    this.webService.sendPostRequestWithToken(vehicleUrl,token, formData).subscribe(response=>{
      callback(response);
    });
  }

  getTrips(token:string, callback){
    var url=this.host+"/api/trip/user/list";
    this.webService.sendGetRequestWithToken(url, token).subscribe(response=>{
      callback(response);
    });
  }

  getProfile(token:string, callback){
    var url=this.host+"/api/user/profile";
    this.webService.sendGetRequestWithToken(url, token).subscribe(response=>{
      callback(response);
    });
  }

  cancelTrip(token:string, tripId:number, callback){
    var url=this.host+"/api/trip/user/update/"+tripId+"/-2";
    this.webService.sendPutRequestWithToken(url,token,new FormData()).subscribe(response=>{
      callback(response);
    });
  }

}
