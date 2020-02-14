import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http: HttpClient, private service:EgozzService) { }

  list: any[] = [{
    vehicleId: 1,
    vehicleName: "Innova"
  },{
    vehicleId: 2,
    vehicleName: "dZire"
  }];

  private logoutUrl:string=this.service.host+"/api/owner/logout";
  private vehicleListUrl:string=this.service.host+"/api/vehicle/owner/list";


  getVehicles(token: string):Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(this.vehicleListUrl,header);
    //return of(this.list);
  }

  doLogout(token:string):Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(this.logoutUrl,header);
  }

};
