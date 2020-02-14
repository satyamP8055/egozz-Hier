import { TestBed } from '@angular/core/testing';

import { AddVehicleService } from './add-vehicle.service';

describe('AddVehicleService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AddVehicleService = TestBed.get(AddVehicleService);
    expect(service).toBeTruthy();
  });
});
