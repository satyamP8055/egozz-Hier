import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnerTripComponent } from './owner-trip.component';

describe('OwnerTripComponent', () => {
  let component: OwnerTripComponent;
  let fixture: ComponentFixture<OwnerTripComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OwnerTripComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnerTripComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
