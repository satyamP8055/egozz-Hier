import { TestBed } from '@angular/core/testing';

import { VehicleDetailService } from './vehicle-detail.service';

describe('VehicleDetailService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: VehicleDetailService = TestBed.get(VehicleDetailService);
    expect(service).toBeTruthy();
  });
});
