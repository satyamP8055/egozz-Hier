import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminOwnerComponent } from './admin-owner.component';

describe('AdminOwnerComponent', () => {
  let component: AdminOwnerComponent;
  let fixture: ComponentFixture<AdminOwnerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminOwnerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
