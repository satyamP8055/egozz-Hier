import { Component, OnInit } from '@angular/core';
import { WebService } from 'src/app/web.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-admin-transaction',
  templateUrl: './admin-transaction.component.html',
  styleUrls: ['./admin-transaction.component.css']
})
export class AdminTransactionComponent implements OnInit {

  constructor(private webService: WebService, private service:EgozzService) { }

  
  ngOnInit() {
    this.token=localStorage.getItem("token");
    this.getTransaction();
  }

  token:string;
  host:string=this.service.host;
  private url:string=this.host+"/api/admin/transactions";
  transactions: any[] = [];

  getTransaction(){
    this.webService.sendGetRequestWithToken(this.url, this.token).subscribe(response=>{
      if(response.status=="success")
        this.transactions=response.data;
    })
  }

}
