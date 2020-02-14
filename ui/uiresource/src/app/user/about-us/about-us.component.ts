import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-about-us',
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.css']
})
export class AboutUsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    this.checkWindowSize();
    this.countDown();
  }

  shortWindow:boolean=true;

  moto: string[] = ["Emphasized", "Gentle", "Zen", "Zappy"];
  stand: string = this.moto[0];
  motoIndex: number = 0;

  countDown() {
    setInterval(() => {
      // Moto Slide...
      if (this.motoIndex >= this.moto.length - 1)
        this.motoIndex = 0;
      else
        this.motoIndex = this.motoIndex + 1;
      this.stand = this.moto[this.motoIndex];
    }, 2000);
  }

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
}
