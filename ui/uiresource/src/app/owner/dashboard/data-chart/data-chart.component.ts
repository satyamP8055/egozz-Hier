import { Component, OnInit } from '@angular/core';
import { DataChartService } from './data-chart.service';
import { EgozzService } from 'src/app/egozz.service'; 
import { Chart } from 'chart.js';  

@Component({
  selector: 'app-data-chart',
  templateUrl: './data-chart.component.html',
  styleUrls: ['./data-chart.component.css']
})
export class DataChartComponent implements OnInit {

  constructor(private dataService: DataChartService, private service: EgozzService) { }

  ngOnInit() {
    this.token = localStorage.getItem("ownerToken");
    this.trips();
    this.transactions();
    this.vTrips();
    this.income();
  }

  tripLabels= [];
  tripData=[];
  tripChart=[];

  vTripLabels= [];
  vTripData=[];
  vTripChart=[];

  incomeLabels= [];
  incomeData=[];
  incomeChart=[];

  transactionLabels= [];
  transactionData=[];
  transactionChart=[];

  trips() {
    this.dataService.trip(this.token, response => {
      var trip = response["data"];
      if (response["status"] == "success") {
        this.tripLabels.push("Completed");
        this.tripData.push(trip.completed);
        this.tripLabels.push("Cancelled");
        this.tripData.push(trip.cancelled);
        this.tripLabels.push("Rejected");
        this.tripData.push(trip.rejected);
        this.tripLabels.push("Upcoming");
        this.tripData.push(trip.upcoming);
        this.tripLabels.push("Ongoing");
        this.tripData.push(trip.ongoing);
        this.tripChart=new Chart('tripCanvas',{
          type:'polarArea',
          data: {
            labels: this.tripLabels,
            datasets: [
              {
                data:this.tripData,
                borderColor: 'transparent',
                backgroundColor: [
                  'rgb(14,117,10)',
                  'rgb(192,39,45)',
                  'black',
                  'rgb(249,249,249)',
                  'rgb(8,11,90)'
                ],
                fill:true
              }
            ]
          },
          options: {
            legend:{
              display:true
            },
            scales :{
              xAxes :[
                {
                  display:false
                }
              ],
              yAxes :[
                {
                  display:false
                }
              ]
            }
          }
        });
      }
      else
        this.failed(response["message"]);
    });
  }
  
  vTrips() {
    this.dataService.vehicleTrip(this.token, response => {
      var vTrip = response["data"];
      if (response["status"] == "success") {
        for(var i=0;i<vTrip.length;i++){
          var vehicle=vTrip[i];
          this.vTripLabels.push(vehicle.vehicleName);
          this.vTripData.push(vehicle.trips);
        }
        this.vTripChart=new Chart('vehicleTripCanvas',{
          type:'doughnut',
          data: {
            labels: this.vTripLabels,
            datasets: [
              {
                data:this.vTripData,
                borderColor: 'transparent',
                backgroundColor: [
                  'rgb(8,11,90)',
                  'rgb(14,117,10)',
                  'rgb(229,229,229)',
                  'black'
                ],
                fill:true
              }
            ]
          },
          options: {
            legend:{
              display:true
            },
            scales :{
              xAxes :[
                {
                  display:false
                }
              ],
              yAxes :[
                {
                  display:false
                }
              ]
            }
          }
        });
      }
      else
        this.failed(response["message"]);
    });
  }

  income() {
    this.dataService.vehicleIncome(this.token, response => {
      var incomes = response["data"];
      if (response["status"] == "success") {
        for(var i=0;i<incomes.length;i++){
          var income=incomes[i];
          this.incomeLabels.push(income.vehicleName);
          this.incomeData.push(income.income);
        }
        this.incomeChart=new Chart('incomeCanvas',{
          type:'bar',
          data: {
            labels: this.incomeLabels,
            datasets: [
              {
                data:this.incomeData,
                borderColor: 'transparent',
                backgroundColor: [
                  'rgb(8,11,90)',
                  'rgb(14,117,10)',
                  'rgb(229,229,229)',
                  'black'
                ],
                fill:true
              }
            ]
          },
          options: {
            legend:{
              display:false
            },
            scales :{
              xAxes :[
                {
                  display:true
                }
              ],
              yAxes :[
                {
                  display:true
                }
              ]
            }
          }
        });
      }
      else
        this.failed(response["message"]);
    });
  }
  
  transactions() {
    this.dataService.transaction(this.token, response => {
      var transaction = response["data"];
      if (response["status"] == "success") {
        this.transactionLabels.push("Deposit");
        this.transactionData.push(transaction.deposit);
        this.transactionLabels.push("Withdrawl");
        this.transactionData.push(transaction.withdrawl);
        this.transactionLabels.push("Pending");
        this.transactionData.push(transaction.pending);
        this.transactionChart=new Chart('transactionCanvas',{
          type:'pie',
          data: {
            labels: this.transactionLabels,
            datasets: [
              {
                data:this.transactionData,
                borderColor: 'transparent',
                backgroundColor: [
                  'rgb(14,117,10)',
                  'rgb(192,39,45)',
                  'rgb(8,11,90)'
                ],
                fill:true
              }
            ]
          },
          options: {
            legend:{
              display:true
            },
            scales :{
              xAxes :[
                {
                  display:false
                }
              ],
              yAxes :[
                {
                  display:false
                }
              ]
            }
          }
        });
      }
      else
        this.failed(response["message"]);
    });
  }

  token: string;
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
