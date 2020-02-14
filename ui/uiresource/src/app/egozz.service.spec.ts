import { TestBed } from '@angular/core/testing';

import { EgozzService } from './egozz.service';

describe('EgozzService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EgozzService = TestBed.get(EgozzService);
    expect(service).toBeTruthy();
  });
});
