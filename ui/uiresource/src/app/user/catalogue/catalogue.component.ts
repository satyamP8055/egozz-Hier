import { Component, OnInit, HostListener } from '@angular/core';
import {HomeService} from '../../home/home.service';
import { EgozzService } from 'src/app/egozz.service';

@Component({
  selector: 'app-catalogue',
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent implements OnInit {

  constructor(private homeService: HomeService, private service:EgozzService) { }

  ngOnInit() {
    this.initialiseClass();
  }

  shortWindow:boolean=true;
  vClasses: any[] = [];
  host:string=this.service.host;
  @HostListener("window:resize", [])
  onWindowResize() {
    this.checkWindowSize();
  }

  checkWindowSize() {
    var width = window.innerWidth;
    if (width < 700)
      this.shortWindow = true;
    else
      this.shortWindow = false;
  }

  initialiseClass() {
    this.homeService.getClassList(response => {
      if (response["status"] == "success") {
        this.vClasses = response["data"];
      }
    });
  }

}
