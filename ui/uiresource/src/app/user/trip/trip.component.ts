import { Component, OnInit, HostListener } from '@angular/core';
import { UserService } from '../user.service';
import { Router } from '@angular/router';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.css']
})
export class TripComponent implements OnInit {

  constructor(private userService: UserService, private service: EgozzService, private router: Router) { }

  ngOnInit() {
    this.checkWindowSize();
    this.token = localStorage.getItem("userToken");
    this.fetchTrips();
  }

  token: string;
  trips: any[] = [];
  reviewStar: number[] = [];
  reviewMessage: string[] = [];

  markReview(index, star) {
    this.reviewStar[index] = star;
  }

  post(index, id) {
    if (this.reviewStar[index] == 0)
      this.failed("Cannot post without rating the stars !")
    else
    {
      this.userService.postReview(id, this.reviewStar[index],this.reviewMessage[index], this.token, response=>{
        this.loading=false;
        this.fetchTrips();
        this.alert(response["message"]);
      });
    }
  }

  darkLogo: string = this.service.host + "/files/egozz_ORG.png";
  fetchTrips() {
    this.loading=true;
    this.userService.getTrips(this.token, response => {
      this.loading=false;
      if (response["status"] == "success") {
        this.trips = response["data"];
        for (var i = 0; i < this.trips.length; i++) {
          this.showFlag[i] = true;
          if (this.trips[i].userReviewStars == null || this.trips[i].userReviewStars == 'null') {
            this.reviewStar[i] = 0;
            this.reviewMessage[i] = "";
          }
          else {
            this.reviewStar[i] = this.trips[i].userReviewStars;
            this.reviewMessage[i] = this.trips[i].userReviewMessage;
          }
        }
      }
      else
        this.failed(response["message"]);
    });
  }

  cancelTrip(tripId: number) {
    this.userService.cancelTrip(this.token, tripId, response => {
      if (response["status"] == "success") {
        this.succeed(response["message"]);
        this.fetchTrips();
      }
      else
        this.failed(response["message"]);
    });
  }

  showTripDetails(index: number) {
    // for(var i=0;i<this.trips.length;i++){
    //   if(i==index && !this.showFlag[i])
    //     this.showFlag[i]=true;
    //   else
    //     this.showFlag[i]=false;
    // }
    if (this.showFlag[index])
      this.showFlag[index] = false;
    else
      this.showFlag[index] = true;
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

  shortWindow: boolean = false;

  fail: boolean = false;
  msg: boolean = true;
  success: boolean = false;
  message: string;
  loading: boolean = false;

  invokeAlert(): void {
    let modalBtn: HTMLElement = document.getElementById("alertInvoker") as HTMLElement;
    modalBtn.click();
  }

  showFlag: boolean[] = [];

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
