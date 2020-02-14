import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import {LoginService} from './login.service';
import {Router} from '@angular/router';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  adminLogin = this.fb.group({
    userName : [''],
    password : ['']
  });
  
  constructor(private fb: FormBuilder, private service:EgozzService, private loginService: LoginService, private router:Router) { }

  logoLocation:string = this.service.host+"/files/egozz_ORG.png";


  adminLoginOperation(): void{
    var formData:any = new FormData();
    formData.append("userName",this.adminLogin.get("userName").value);
    formData.append("password",this.adminLogin.get("password").value);
    this.loginService.doAdminLogin(formData).subscribe(
      response=> this.validateLogin(response)
    );
  }

  validateLogin(response):void{
    if(response["statusCode"]==200){
      if(response["status"]=="success"){
        localStorage.setItem("ownerToken",response["token"]);
        localStorage.setItem("ownerFullName", response["data"].fullName);
        this.router.navigateByUrl("/owner/dashboard");
      }
      else{
        this.failed("Invalid Credentials ");
      }
    }
    else
      this.failed("Internal Server Error !"); 
  }

  goToRegister(){
    this.router.navigateByUrl("/owner/register");
  }

  ngOnInit() {

  }

  
  fail:boolean=false;
  msg:boolean=true;
  success:boolean=false;
  message:string;

  invokeAlert():void{
    
    let modalBtn :HTMLElement= document.getElementById("alertInvoker") as HTMLElement;
    modalBtn.click();
  }

  alert(message:string):void{
    this.message=message;
    this.msg=true;
    this.success=false;
    this.fail=false;
    this.invokeAlert();
  }

  succeed(message:string):void{
    this.message=message;
    this.msg=false;
    this.success=true;
    this.fail=false;
    this.invokeAlert();
  }

  failed(message:string):void{
    this.message=message;
    this.msg=false;
    this.success=false;
    this.fail=true;
    this.invokeAlert();
  }


}
