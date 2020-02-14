import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})


export class DashboardService {

  private logoutUrl:string=this.service.host+"/api/admin/logout";
  private accessUrl:string=this.service.host+"/api/access";

  constructor(private http: HttpClient, private service:EgozzService) { }

  doAdminLogout(token:string): Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(this.logoutUrl,header);
  }

  getAccessList(token:string) : Observable<any>{
    var header= {
      headers : new HttpHeaders().set('X-ACCESS-TOKEN',token)
    };
    return this.http.get(this.accessUrl,header);
  }

}
