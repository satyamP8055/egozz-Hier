import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class VehicleDetailService {

  constructor(private http: HttpClient, private service:EgozzService) { }

  public fetchVehicle(token:string, vehicleId:Number):Observable<any>{
    var vehicleFetchUrl=this.service.host+"/api/vehicle/owner/"+vehicleId;
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(vehicleFetchUrl,header);
  }

  public deleteVehicle(token:string, vehicleId:Number):Observable<any>{
    var vehicleFetchUrl=this.service.host+"/api/vehicle/owner/delete/"+vehicleId;
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.delete(vehicleFetchUrl,header);
  }

}
