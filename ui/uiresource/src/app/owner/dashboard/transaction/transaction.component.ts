import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { TransactionService } from './transaction.service';
import { EgozzService } from 'src/app/egozz.service';
import { WebService } from 'src/app/web.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  constructor(private transactionService: TransactionService, private service: EgozzService, private webService: WebService, private activatedroute: ActivatedRoute, private fb: FormBuilder) {
    this.activatedroute.queryParams.subscribe(params => {
      if (params['paymentId'] == null || params['paymentId'] == 'null' || params['paymentId'] == undefined || params['paymentId'] == 'undefined') {
        if (params['egozzKey'] == null || params['egozzKey'] == 'null' || params['egozzKey'] == undefined || params['egozzKey'] == 'undefined') { }
        else {
          this.confirmPayU(params['egozzKey']);
        }
      }
      else {
        this.payOffline(localStorage.getItem("currentTransactionId"));
      }
    });
  }

  ngOnInit() {
    this.token = localStorage.getItem("ownerToken");
    this.getTransactions();
  }

  paypalLogo:string=this.service.host+"/files/PaymentGateways/paypal.png";

  payULogo:string=this.service.host+"/files/PaymentGateways/payU.png";

  confirmPayU(key:string){
    var tken=localStorage.getItem("tken");
    var url=this.service.host+"/api/payu/verify?paymentToken="+key;
    this.webService.sendGetRequestWithToken(url, tken).subscribe(response=>{
      if(response["status"]=="success"){
        this.payOffline(localStorage.getItem("currentTransactionId"));
      }
      else
        this.failed("Transaction Failed !");
    });
  }
  token: string;
  transactions: any[] = [];

  getTransactions() {
    this.transactionService.getTransaction(this.token, response => {
      if (response["status"] == "success")
        this.transactions = response["data"];
      else
        this.failed(response["message"]);
    });
  }

  paymentId: number;
  amount: number;
  currentPayment: any;

  pay(transaction) {
    this.currentPayment = transaction;
    localStorage.setItem("currentTransactionId", transaction.paymentId);
    this.paymentId = transaction.paymentId;
    this.amount = transaction.amount;
    let paymentBtn: HTMLElement = document.getElementById("paymentBoxInvoker") as HTMLElement;
    paymentBtn.click();
  }

  payByPayPal() {
    this.loading = true;
    var url = this.service.host + "/api/paypal/pay?sum=" + this.amount;
    this.webService.sendPostRequestWithToken(url, this.token, {}).subscribe(response => {
      window.location = response.redirect_url;
    });
  }

  payUBase: string = "";
  payUForm = this.fb.group({
    "key": [''],
    "hash": [''],
    "hashString": [''],
    "txnid": [''],
    "amount": [''],
    "firstname": [''],
    "email": [''],
    "phone": [''],
    "productinfo": [''],
    "surl": [''],
    "furl": [''],
    "service_provider":['']
  });

  @ViewChild("payUFinalForm", { static: true })
  payForm: ElementRef;

  payByPayU() {
    this.loading = true;
    var processUrl = "http://localhost:4200/owner/dashboard/transactions";
    var productInfo = "EGOZZ_INCENTIVE";
    var url = this.service.host + "/api/payu/pay";
    var formData = new FormData();
    formData.append("processUrl", processUrl);
    formData.append("productInfo", productInfo);
    formData.append("amount", this.amount + "");
    this.webService.sendPostRequestWithToken(url, this.token, formData).subscribe(response => {
      var model = response["data"];
      var tken = response["token"];
      localStorage.setItem("tken", tken);
      if (response.status == "success") {
        var payment = new FormData();
        payment.append("amount", model.amount);
        payment.append("firstname", model.firstname);
        payment.append("email", model.email);
        payment.append("phone", model.phone);
        payment.append("productinfo", model.productinfo);
        payment.append("surl", model.surl);
        payment.append("furl", model.furl);
        this.webService.sendPostRequestWithToken(this.service.host + "/api/payu/confirm", tken, payment).subscribe(res => {
          if (res.status == "success") {
            var confirmed = res["data"];
            this.payUForm.patchValue({
              "key": confirmed.key,
              "hash": confirmed.hash,
              "hashString": confirmed.hashstring,
              "txnid": confirmed.txnid,
              "amount": confirmed.amount,
              "firstname": confirmed.firstname,
              "email": confirmed.email,
              "phone": confirmed.phone,
              "productinfo": confirmed.productinfo,
              "surl": confirmed.surl,
              "furl": confirmed.furl,
              "service_provider" : confirmed.service_provider
            });
            this.payUBase = confirmed.baseUrl;
            console.log("value placed in form & base = " + this.payUBase);
            this.loading = false;
            console.log(this.payForm.nativeElement.action);
            this.payForm.nativeElement.action = this.payUBase;
            this.payForm.nativeElement.submit();
          }
          else
            this.failed(res["message"]);
        });
      }
      else {
        this.loading = false;
        this.failed(response["message"]);
      }
    });
  }

  startPayUPayment(e) {
    // e.target.submit();
  }

  payOffline(transactionId) {
    this.transactionService.pay(this.token, transactionId, response => {
      localStorage.setItem("currentTransactionId", null);
      if (response["status"] == "success") {
        this.succeed("Paid Successfully");
        this.transactions = response["data"];
      }
      else
        this.failed(response["message"]);
    });
  }

  logoLocation: string = this.service.host + "/files/egozz_ORG.png";

  loading: boolean = false;
  fail: boolean = false;
  msg: boolean = true;
  success: boolean = false;
  message: string;

  invokeAlert(): void {

    let modalBtn: HTMLElement = document.getElementById("alertInvoker") as HTMLElement;
    modalBtn.click();
  }

  alert(message: string): void {
    this.message = message;
    this.msg = true;
    this.success = false;
    this.fail = false;
    this.invokeAlert();
  }

  succeed(message: string): void {
    this.message = message;
    this.msg = false;
    this.success = true;
    this.fail = false;
    this.invokeAlert();
  }

  failed(message: string): void {
    this.message = message;
    this.msg = false;
    this.success = false;
    this.fail = true;
    this.invokeAlert();
  }

}
