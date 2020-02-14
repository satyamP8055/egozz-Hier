import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import {RegisterService} from './register.service';
import {Router} from '@angular/router';
import { EgozzService } from 'src/app/egozz.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private fb: FormBuilder, private service:EgozzService, private registerService: RegisterService, private router:Router) { }

  logoLocation:string = this.service.host+"/files/egozz_ORG.png";

  form=this.fb.group({
    userName: [''],
    mail:[''],
    fullName: [''],
    password : [''],
    confirmPassword : [''],
    phone:['']
  });

  ngOnInit() {
  }

  loading=false;

  formSubmit():void{
    var formData:any=new FormData();
    formData.append("userName",this.form.get("userName").value);
    formData.append("mail",this.form.get("mail").value);
    formData.append("fullName",this.form.get("fullName").value);
    formData.append("password",this.form.get("password").value);
    formData.append("phone",this.form.get("phone").value);
    if(this.form.get("password").value==this.form.get("confirmPassword").value){
      this.loading=true;
      this.registerService.doRegister(formData).subscribe(response=>{
        if(response["status"]=="success"){
          localStorage.setItem("ownerToken",response["token"]);
          this.router.navigateByUrl("/owner/dashboard");
        }
        else{
        }
      });
    }
    else
      alert("Password & Confirm Password should be equal !");
  }

}
