import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoElementComponent } from './no-element.component';

describe('NoElementComponent', () => {
  let component: NoElementComponent;
  let fixture: ComponentFixture<NoElementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoElementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
