import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletedTrainingComponent } from './completed-training.component';

describe('CompletedTrainingComponent', () => {
  let component: CompletedTrainingComponent;
  let fixture: ComponentFixture<CompletedTrainingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompletedTrainingComponent]
    });
    fixture = TestBed.createComponent(CompletedTrainingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
