import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyApiServiceService } from '../my-api-service.service';
import { AuthenticationService } from '../authentication.service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-health-data',
  templateUrl: './health-data.component.html',
  styleUrls: ['./health-data.component.scss'],
})
export class HealthDataComponent {
  healthDataForm!: FormGroup;
  Id: string = '';
  errorMessage: string = '';
  weight: number | null = null;
  calories: number | null = null;
  targetcalories: number | null = null;
  targetweightvalue: number | null = null;
  height: number | null = null;
  loseWeightCalories: number | null = null;
  targetsteps: number | null = null;
  absoluteLoseWeightCalories: number | null = null;
  loseCaloriesDuration: number | null = null;

  displayedColumns: string[] = ['calories', 'duration', 'steps'];
  dataSource: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private healthDataService: MyApiServiceService,
    private authService: AuthenticationService
  ) {}

  submitHealthData() {
    if (this.healthDataForm.valid) {
      this.Id = this.authService.getId();

      const formData = {
        height: parseFloat(this.healthDataForm.get('height')?.value).toFixed(1),
        weight: parseFloat(this.healthDataForm.get('weight')?.value).toFixed(1),
        targetWeight: parseFloat(
          this.healthDataForm.get('targetWeight')?.value
        ).toFixed(1),
        targetCalories: this.healthDataForm.get('targetCalories')?.value,
        targetSteps: this.healthDataForm.get('targetSteps')?.value,
      };

      console.log(formData);

      this.healthDataService
        .addUserHealth(this.Id, formData)
        .pipe(
          catchError((error) => {
            // Handle the error here
            console.log(error);
            this.errorMessage = error.error.error;
            this.healthDataForm.reset({});
            return throwError(() => error);
          })
        )
        .subscribe((response) => {
          // Handle success or errors
          localStorage.setItem('healthdata', 'true');
          this.healthDataForm.reset({});
          console.log(response);
        });
    }
  }

  ngOnInit() {
    this.healthDataForm = this.formBuilder.group({
      height: [
        null,
        [Validators.required, Validators.min(0), Validators.max(2.5)],
      ],
      weight: [
        null,
        [Validators.required, Validators.min(0), Validators.max(500)],
      ],
      targetWeight: [
        null,
        [Validators.required, Validators.min(0), Validators.max(500)],
      ],
      targetCalories: [
        null,
        [Validators.required, Validators.min(0), Validators.max(3500)],
      ],
      targetSteps: [
        null,
        [Validators.required, Validators.min(0), Validators.max(100000)],
      ],
    });

    this.healthDataForm
      .get('weight')
      ?.valueChanges.subscribe(() => this.calculateCalories());
    this.healthDataForm
      .get('height')
      ?.valueChanges.subscribe(() => this.calculateCalories());
    this.healthDataForm
      .get('height')
      ?.valueChanges.subscribe(() => this.targetCaloriesAndSteps());
    this.healthDataForm
      .get('targetWeight')
      ?.valueChanges.subscribe(() => this.targetCaloriesAndSteps());

    this.healthDataForm
      .get('weight')
      ?.valueChanges.subscribe(() => this.targetCaloriesAndSteps());
  }

  calculateCalories() {
    const weightValue = this.healthDataForm.get('weight')?.value;
    const heightValue = this.healthDataForm.get('height')?.value;
    if (weightValue !== null && heightValue !== null) {
      // Men: BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) – (5.677 x age in years)
      // Calorie calculation only for male and age 30
      this.weight = weightValue;
      this.height = heightValue;
      this.calories = Math.round(
        88.362 + 13.397 * weightValue + 4.799 * (heightValue * 100) - 5.677 * 30
      );
    } else {
      this.calories = null;
    }
  }
  targetCaloriesAndSteps() {
    const targetWeightValue = this.healthDataForm.get('targetWeight')?.value;
    const heightValue = this.healthDataForm.get('height')?.value;
    const weightValue = this.healthDataForm.get('weight')?.value;
    if (targetWeightValue !== null && heightValue !== null) {
      // Men: BMR = 88.362 + (13.397 x weight in kg) + (4.799 x height in cm) – (5.677 x age in years)
      // Calorie calculation only for male and age 30
      this.targetweightvalue = targetWeightValue;
      this.height = heightValue;
      this.targetcalories = Math.round(
        88.362 +
          13.397 * targetWeightValue +
          4.799 * (heightValue * 100) -
          5.677 * 30
      );
      if (this.calories !== null && this.targetcalories !== null) {
        if (this.targetcalories >= this.calories) {
          //const caloriesDifference = this.calories  this.targetcalories;
          //this.calories = this.calories + this.targetcalories
          this.loseWeightCalories = -this.targetcalories + this.calories;
          this.absoluteLoseWeightCalories =
            this.loseWeightCalories !== null
              ? Math.abs(this.loseWeightCalories)
              : 0;
          this.targetsteps = 0;
          this.loseCaloriesDuration = Math.round(
            (7700 * (targetWeightValue - weightValue)) /
            this.loseWeightCalories);
            this.dataSource = [
              {
                caloriesData:
                  Math.round(Math.abs(this.loseWeightCalories)) +
                  this.calories,
                durationData: Math.round(Math.abs(this.loseCaloriesDuration)),
                stepsData: this.targetsteps,
              },
              {
                caloriesData:
                  Math.round(Math.abs(this.loseWeightCalories * 2)) +
                  this.calories,
                durationData: Math.round(
                  Math.abs(this.loseCaloriesDuration / 2)
                ),
                stepsData: this.targetsteps,
              },
              {
                caloriesData:
                  Math.round(Math.abs(this.loseWeightCalories * 3)) +
                  this.calories,
                durationData: Math.round(
                  Math.abs(this.loseCaloriesDuration / 3)
                ),
                stepsData: this.targetsteps,
              },
            ];
        } else {
          this.loseWeightCalories = this.calories - this.targetcalories;
          this.targetsteps = this.loseWeightCalories / 0.04;
          this.loseCaloriesDuration = Math.round(
            (7700 * (weightValue - targetWeightValue)) /
            this.loseWeightCalories);
          this.dataSource = [
            {
              caloriesData: this.calories - this.loseWeightCalories,
              durationData: this.loseCaloriesDuration,
              stepsData: this.targetsteps,
            },
            {
              caloriesData: this.calories - this.loseWeightCalories * 2,
              durationData: Math.round(this.loseCaloriesDuration / 2),
              stepsData: this.targetsteps * 2,
            },
            {
              caloriesData: this.calories - this.loseWeightCalories * 3,
              durationData: Math.round(this.loseCaloriesDuration / 3),
              stepsData: this.targetsteps * 3,
            },

          ];
        }
      }
    } else {
      this.targetcalories = null;
    }
  }
}
// 1 kg fat = 7700kCal
//one step 0.04kCal