import { TestBed } from '@angular/core/testing';

import { OwnerTripService } from './owner-trip.service';

describe('OwnerTripService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OwnerTripService = TestBed.get(OwnerTripService);
    expect(service).toBeTruthy();
  });
});
