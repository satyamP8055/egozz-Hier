import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTripsComponent } from './admin-trips.component';

describe('AdminTripsComponent', () => {
  let component: AdminTripsComponent;
  let fixture: ComponentFixture<AdminTripsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminTripsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminTripsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
