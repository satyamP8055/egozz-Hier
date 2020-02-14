import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class WebService {

  constructor(private http: HttpClient) { }

  public sendPostRequestWithToken(url: string, token: string, formData: any): Observable<any> {
    var header = {
      headers: new HttpHeaders().set('X-ACCESS-TOKEN', token)
    };
    return this.http.post(url, formData, header);
  }

  public sendGetRequestWithToken(url: string, token: string): Observable<any> {
    var header = {
      headers: new HttpHeaders().set('X-ACCESS-TOKEN', token)
    };
    return this.http.get(url, header);
  }
  
  public sendPutRequestWithToken(url: string, token: string, formData: any): Observable<any> {
    var header = {
      headers: new HttpHeaders().set('X-ACCESS-TOKEN', token)
    };
    return this.http.put(url, formData, header);
  }
  
  public sendDeleteRequestWithToken(url: string, token: string): Observable<any> {
    var header = {
      headers: new HttpHeaders().set('X-ACCESS-TOKEN', token)
    };
    return this.http.get(url, header);
  }
  
  public sendPostRequest(url: string, formData: any): Observable<any> {
    return this.http.post(url, formData);
  }
  
  public sendGetRequest(url: string): Observable<any> {
    return this.http.get(url);
  }
  
  public sendPutRequest(url: string, formData: any): Observable<any> {
    return this.http.put(url, formData);
  }
  
  public sendDeleteRequest(url: string): Observable<any> {
    return this.http.get(url);
  }

}
