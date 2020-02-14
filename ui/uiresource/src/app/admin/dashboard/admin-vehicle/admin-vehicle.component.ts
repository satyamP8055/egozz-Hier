import { Component, OnInit } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-admin-vehicle',
  templateUrl: './admin-vehicle.component.html',
  styleUrls: ['./admin-vehicle.component.css']
})
export class AdminVehicleComponent implements OnInit {

  constructor(private webService:WebService, private service:EgozzService) { }

  ngOnInit() {
    this.token=localStorage.getItem("token");
    this.getVehicles();
  }

  url:string=this.service.host+"/api/vehicle";
  token:string;
  vehicles:any[]=[];

  getVehicles(){
    this.webService.sendGetRequestWithToken(this.url,this.token).subscribe(res=>{
      if(res.status=="success")
        this.vehicles=res.data;
    });
  }

}
