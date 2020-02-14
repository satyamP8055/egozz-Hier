import { Injectable } from '@angular/core';
import { EgozzService } from 'src/app/egozz.service';
import { WebService } from 'src/app/web.service';

@Injectable({
  providedIn: 'root'
})
export class DataChartService {

  constructor(private service: EgozzService, private webService: WebService) { }

  private vehicleIncomeUrl = this.service.host + "/api/trip/vehicle-income";
  private vehicleTripUrl = this.service.host + "/api/trip/vehicle-trip-data";
  private transactionUrl = this.service.host + "/api/trip/transaction-data";
  private tripUrl = this.service.host + "/api/trip/data";

  public vehicleIncome(token: string, callback) {
    this.webService.sendGetRequestWithToken(this.vehicleIncomeUrl, token).subscribe(response => {
      callback(response);
    })
  }

  public vehicleTrip(token: string, callback) {
    this.webService.sendGetRequestWithToken(this.vehicleTripUrl, token).subscribe(response => {
      callback(response);
    })
  }

  public transaction(token: string, callback) {
    this.webService.sendGetRequestWithToken(this.transactionUrl, token).subscribe(response => {
      callback(response);
    })
  }
  
  public trip(token: string, callback) {
    this.webService.sendGetRequestWithToken(this.tripUrl, token).subscribe(response => {
      callback(response);
    })
  }

}
