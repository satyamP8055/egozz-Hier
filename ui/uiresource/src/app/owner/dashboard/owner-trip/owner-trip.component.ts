import { Component, OnInit } from '@angular/core';
import { OwnerTripService } from './owner-trip.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-owner-trip',
  templateUrl: './owner-trip.component.html',
  styleUrls: ['./owner-trip.component.css']
})
export class OwnerTripComponent implements OnInit {

  constructor(private ownerTripService: OwnerTripService, private service:EgozzService) { }

  token:string;

  ngOnInit() {
    this.token=localStorage.getItem("ownerToken");
    this.fetchTrips();
  }

  trips:any[]=[];

  fetchTrips(){
    this.loading=true;
    this.ownerTripService.getTrips(this.token,response=>{
      this.loading=false;
      if(response["status"]=="success")
        this.trips=response["data"];
      else
        this.failed(response["message"]);
    });
  }

  kmsCovered:number=0;

  updateTrip(tripId, statusId){
    this.loading=true;
    this.ownerTripService.updateTrip(this.token, tripId, statusId, response=>{
      this.loading=false;
      if(response["status"]=="success"){
        this.succeed(response["message"]);
        this.fetchTrips();
      }
      else
        this.failed(response["message"]);
    });
  }

  logoLocation: string = this.service.host+"/files/egozz_ORG.png";

  currentId:number=0;

  completeTrip(tripId, distance){
    this.currentId=tripId;
    this.kmsCovered=distance;
    let endBtn: HTMLElement = document.getElementById("endInvoker") as HTMLElement;
    endBtn.click();
  }

  completeTripNow(){
    this.loading=true;
    this.ownerTripService.completeTrip(this.token, this.currentId, 3, this.kmsCovered, response=>{
      this.loading=false;
      if(response["status"]=="success"){
        this.succeed(response["message"]);
        this.fetchTrips();
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
