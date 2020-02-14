import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminVehicleComponent } from './admin-vehicle.component';

describe('AdminVehicleComponent', () => {
  let component: AdminVehicleComponent;
  let fixture: ComponentFixture<AdminVehicleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminVehicleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminVehicleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
