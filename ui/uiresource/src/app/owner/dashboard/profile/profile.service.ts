import { Injectable } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  host:string=this.service.host;

  constructor(private webService: WebService, private service:EgozzService) { }

  
  getProfile(token:string, callback){
    var url=this.host+"/api/owner/profile";
    this.webService.sendGetRequestWithToken(url, token).subscribe(response=>{
      callback(response);
    });
  }

}
