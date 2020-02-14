import { Component, OnInit } from '@angular/core';
import { EgozzService } from '../egozz.service';

@Component({
  selector: 'app-no-element',
  templateUrl: './no-element.component.html',
  styleUrls: ['./no-element.component.css']
})
export class NoElementComponent implements OnInit {

  constructor(private service:EgozzService) { }

  logoLocation: string = this.service.host+"/files/egozz_ORG.png";

  ngOnInit() {
  }

}
