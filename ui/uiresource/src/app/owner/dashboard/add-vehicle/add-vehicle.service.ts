import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class AddVehicleService {

  constructor(private http: HttpClient, private service:EgozzService) { }

  private vehicleClassListUrl=this.service.host+"/api/vehicle/class/list";
  private vehicleAddUrl=this.service.host+"/api/vehicle/owner/add";

  public getClassList(token:string):Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(this.vehicleClassListUrl,header);
  }

  public addVehicle(token:string, vehicleData:any):Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.post(this.vehicleAddUrl, vehicleData ,header);
  }

  public updateVehicle(token:string, vehicleData:any, vehicleId:any):Observable<any>{
    var vehicleEditUrl=this.service.host+"/api/vehicle/owner/update/"+vehicleId;
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.put(vehicleEditUrl, vehicleData ,header);
  }

}
