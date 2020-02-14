import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { LoginComponent } from './admin/login/login.component';
import { DashboardComponent} from './admin/dashboard/dashboard.component';
import { OwnerComponent } from './owner/owner.component';
import { LoginComponent as OwnerLogin } from './owner/login/login.component';
import { DashboardComponent as OwnerDashboard } from './owner/dashboard/dashboard.component';
import { RegisterComponent } from './owner/register/register.component';
import { AddVehicleComponent } from './owner/dashboard/add-vehicle/add-vehicle.component';
import { VehicleDetailComponent } from './owner/dashboard/vehicle-detail/vehicle-detail.component';
import { EditVehicleComponent } from './owner/dashboard/edit-vehicle/edit-vehicle.component';
import { HomeComponent } from './home/home.component';
import { HomeComponent as UserHomeComponent} from './user/home/home.component';
import { UserComponent } from './user/user.component';
import { AboutUsComponent } from './user/about-us/about-us.component';
import { CatalogueComponent } from './user/catalogue/catalogue.component';
import { TripComponent } from './user/trip/trip.component';
import { ProfileComponent } from './user/profile/profile.component';
import { OwnerTripComponent } from './owner/dashboard/owner-trip/owner-trip.component';
import { TransactionComponent } from './owner/dashboard/transaction/transaction.component';
import {ProfileComponent as OwnerProfileComponent} from './owner/dashboard/profile/profile.component';
import { NoElementComponent } from './no-element/no-element.component';
import { DataChartComponent } from './owner/dashboard/data-chart/data-chart.component';
import { AdminOwnerComponent } from './admin/dashboard/admin-owner/admin-owner.component';
import { AdminTransactionComponent } from './admin/dashboard/admin-transaction/admin-transaction.component';
import { AdminTripsComponent } from './admin/dashboard/admin-trips/admin-trips.component';
import { AdminUserComponent } from './admin/dashboard/admin-user/admin-user.component';
import { AdminVehicleComponent } from './admin/dashboard/admin-vehicle/admin-vehicle.component';
const routes: Routes = [
  {
    path:"admin",
    component : AdminComponent,
    children : [
      {
        path : 'login',
        component : LoginComponent
      },
      {
        path : 'dashboard',
        component : DashboardComponent,
        children : [
          {
            path : 'owners',
            component : AdminOwnerComponent
          },
          {
            path : 'transaction',
            component : AdminTransactionComponent
          },
          {
            path : 'trips',
            component : AdminTripsComponent
          },
          {
            path : 'users',
            component : AdminUserComponent
          },
          {
            path : 'vehicles',
            component : AdminVehicleComponent
          }
        ]
      },
      {
        path : '',
        redirectTo : 'login',
        pathMatch : 'full'
      }
    ]
  },
  {
    path:"owner",
    component : OwnerComponent,
    children : [
      {
        path : 'login',
        component : OwnerLogin
      },
      {
        path : 'register',
        component : RegisterComponent
      },
      {
        path : 'dashboard',
        component : OwnerDashboard,
        children : [
          {
            path : 'add-vehicle',
            component : AddVehicleComponent
          },
          {
            path : 'vehicle-detail/:vehicleId',
            component : VehicleDetailComponent
          },
          {
            path : 'vehicle-update/:vehicleId',
            component : EditVehicleComponent
          },
          {
            path : 'trips',
            component : OwnerTripComponent
          },
          {
            path : 'transactions',
            component : TransactionComponent
          },
          {
            path : 'transactions/paid',
            component : TransactionComponent
          },
          {
            path : 'profile',
            component : OwnerProfileComponent
          },
          {
            path : '',
            component : DataChartComponent
          }
        ]
      },
      {
        path : '',
        redirectTo : 'login',
        pathMatch : 'full'
      }
    ]
  },
  {
    path:"",
    component : HomeComponent
  },
  {
    path:"home",
    component : UserComponent,
    children : [
      {
        path : '',
        component : UserHomeComponent
      },
      {
        path : 'aboutUs',
        component : AboutUsComponent
      },
      {
        path : 'catalogue',
        component : CatalogueComponent
      },
      {
        path : 'trips',
        component : TripComponent
      },
      {
        path : 'profile',
        component : ProfileComponent
      }
    ]
  },
  {
    path : '**',
    pathMatch : 'full',
    component:NoElementComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
