import { Injectable } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class OwnerTripService {

  constructor(private webService:WebService, private service:EgozzService) { }

  host:string=this.service.host;

  getTrips(token:string, callback){
    var url=this.host+"/api/trip/owner/list";
    this.webService.sendGetRequestWithToken(url, token).subscribe(response=>{
      callback(response);
    });
  }
 
  updateTrip(token:string, tripId:number, statusId:number, callback){
    var url=this.host+"/api/trip/owner/update/"+tripId+"/"+statusId;
    this.webService.sendPutRequestWithToken(url,token,new FormData()).subscribe(response=>{
      callback(response);
    });
  }

  completeTrip(token:string, tripId:number, statusId:number, distance:number, callback){
    var url=this.host+"/api/trip/owner/update/"+tripId+"/"+statusId+"?totalKms="+distance;
    this.webService.sendPutRequestWithToken(url,token,new FormData()).subscribe(response=>{
      callback(response);
    });
  }

}
