import { Injectable } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private webService: WebService, private service:EgozzService) { }

  host:string=this.service.host;
  private getUrl:string=this.host+"/api/owner/transactions";
  private payUrl:string=this.host+"/api/owner/pay";

  getTransaction(token:string, callback){
    this.webService.sendGetRequestWithToken(this.getUrl, token).subscribe(response=>{
      callback(response);
    })
  }

  pay(token:string, transactionId:number, callback){
    this.webService.sendGetRequestWithToken(this.payUrl+"/"+transactionId,token).subscribe(response=>{
      callback(response);
    });
  }

}
