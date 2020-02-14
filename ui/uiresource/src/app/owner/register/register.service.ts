import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import { EgozzService } from 'src/app/egozz.service';


@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private registerUrl: string = this.service.host+"/api/owner/signup";

  constructor(private http: HttpClient, private service:EgozzService) { }

  doRegister(ownerData:any): Observable<any>{
    return this.http.post(this.registerUrl, ownerData);
  }
}
