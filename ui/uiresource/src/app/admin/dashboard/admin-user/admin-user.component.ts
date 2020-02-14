import { Component, OnInit } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css']
})
export class AdminUserComponent implements OnInit {

  constructor(private webService:WebService, private service:EgozzService) { }

  ngOnInit() {
    this.token=localStorage.getItem("token");
    this.getUsers();
  }

  url:string=this.service.host+"/api/user";
  token:string;
  users:any[]=[];

  getUsers(){
    this.webService.sendGetRequestWithToken(this.url,this.token).subscribe(res=>{
      if(res.status=="success")
        this.users=res.data;
    });
  }
}
