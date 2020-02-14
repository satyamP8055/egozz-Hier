import { Component, OnInit } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-admin-trips',
  templateUrl: './admin-trips.component.html',
  styleUrls: ['./admin-trips.component.css']
})
export class AdminTripsComponent implements OnInit {

  constructor(private webService:WebService, private service:EgozzService) { }

  ngOnInit() {
    this.token=localStorage.getItem("token");
    this.getTrips();
  }

  url:string=this.service.host+"/api/trip";
  token:string;
  trips:any[]=[];

  getTrips(){
    this.webService.sendGetRequestWithToken(this.url,this.token).subscribe(res=>{
      if(res.status=="success")
        this.trips=res.data;
    });
  }
}
