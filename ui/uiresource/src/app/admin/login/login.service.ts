import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})

export class LoginService {

  private loginUrl: string = this.service.host+"/api/admin/login";

  constructor(private http: HttpClient, private service:EgozzService) { }

  doAdminLogin(loginData:any): Observable<any>{
    return this.http.post(this.loginUrl, loginData);
  }
}
