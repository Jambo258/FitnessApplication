import { TestBed } from '@angular/core/testing';

import { MyApiServiceService } from './my-api-service.service';

describe('MyApiServiceService', () => {
  let service: MyApiServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MyApiServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
