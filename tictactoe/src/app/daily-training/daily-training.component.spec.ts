import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DailyTrainingComponent } from './daily-training.component';

describe('DailyTrainingComponent', () => {
  let component: DailyTrainingComponent;
  let fixture: ComponentFixture<DailyTrainingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DailyTrainingComponent]
    });
    fixture = TestBed.createComponent(DailyTrainingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
