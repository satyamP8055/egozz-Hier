import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AgmCoreModule } from '@agm/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminComponent } from './admin/admin.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { LoginComponent } from './admin/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { OwnerComponent } from './owner/owner.component';
import { LoginComponent as OwnerLogin } from './owner/login/login.component';
import { DashboardComponent as OwnerDashboard } from './owner/dashboard/dashboard.component';
import { RegisterComponent } from './owner/register/register.component';
import { AddVehicleComponent } from './owner/dashboard/add-vehicle/add-vehicle.component';
import { VehicleDetailComponent } from './owner/dashboard/vehicle-detail/vehicle-detail.component';
import { EditVehicleComponent } from './owner/dashboard/edit-vehicle/edit-vehicle.component';
import { HomeComponent } from './home/home.component';
import { UserComponent } from './user/user.component';
import { CatalogueComponent } from './user/catalogue/catalogue.component';
import { ProfileComponent } from './user/profile/profile.component';
import { TripComponent } from './user/trip/trip.component';
import { HomeComponent as UserHomeComponent} from './user/home/home.component';
import { AboutUsComponent } from './user/about-us/about-us.component';
import { OwnerTripComponent } from './owner/dashboard/owner-trip/owner-trip.component';
import { TransactionComponent } from './owner/dashboard/transaction/transaction.component';
import {ProfileComponent as OwnerProfileComponent} from './owner/dashboard/profile/profile.component';
import { NoElementComponent } from './no-element/no-element.component';
import { DataChartComponent } from './owner/dashboard/data-chart/data-chart.component';
import { AdminVehicleComponent } from './admin/dashboard/admin-vehicle/admin-vehicle.component';
import { AdminTripsComponent } from './admin/dashboard/admin-trips/admin-trips.component';
import { AdminTransactionComponent } from './admin/dashboard/admin-transaction/admin-transaction.component';
import { AdminOwnerComponent } from './admin/dashboard/admin-owner/admin-owner.component';
import { AdminUserComponent } from './admin/dashboard/admin-user/admin-user.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminComponent,
    DashboardComponent,
    LoginComponent,
    OwnerComponent,
    OwnerLogin,
    OwnerDashboard,
    RegisterComponent,
    AddVehicleComponent,
    VehicleDetailComponent,
    EditVehicleComponent,
    HomeComponent,
    UserComponent,
    CatalogueComponent,
    ProfileComponent,
    TripComponent,
    UserHomeComponent,
    AboutUsComponent,
    OwnerTripComponent,
    TransactionComponent,
    OwnerProfileComponent,
    NoElementComponent,
    DataChartComponent,
    AdminVehicleComponent,
    AdminTripsComponent,
    AdminTransactionComponent,
    AdminOwnerComponent,
    AdminUserComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    AgmCoreModule.forRoot({
      apiKey: '',
      libraries: ['places']
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
