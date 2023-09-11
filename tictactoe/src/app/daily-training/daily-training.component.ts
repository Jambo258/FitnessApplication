import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyApiServiceService } from '../my-api-service.service';
import { AuthenticationService } from '../authentication.service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-daily-training',
  templateUrl: './daily-training.component.html',
  styleUrls: ['./daily-training.component.scss'],
})
export class DailyTrainingComponent {
  dailyInputForm!: FormGroup;
  Id: string = '';
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private trainingDataService: MyApiServiceService,
    private authService: AuthenticationService
  ) {}

  

  ngOnInit() {
    this.dailyInputForm = this.formBuilder.group({
      currentWeight: [
        null,
        [Validators.required, Validators.min(0), Validators.max(500)],
      ],
      dailyCalories: [
        null,
        [Validators.required, Validators.min(0), Validators.max(3500)],
      ],
      dailySteps: [
        null,
        [Validators.required, Validators.min(0), Validators.max(100000)],
      ],
    });
  }

  submitTraining() {
    if (this.dailyInputForm.valid) {
      this.Id = this.authService.getId();

      const formData = {
        currentWeight: parseFloat(
          this.dailyInputForm.get('currentWeight')?.value
        ).toFixed(1),
        dailyCalories: this.dailyInputForm.get('dailyCalories')?.value,
        dailySteps: this.dailyInputForm.get('dailySteps')?.value
      };

      console.log(formData)


      this.trainingDataService
        .addUserTrainingDay(this.Id, formData)
        .pipe(
          catchError((error) => {
            // Handle the error here
            this.errorMessage = error.error.error;
            this.dailyInputForm.reset({});
            return throwError(() => error);
          })
        )
        .subscribe((response) => {
          // Handle success or errors
          this.dailyInputForm.reset({});
          console.log(response);
        });
    }
  }
}
