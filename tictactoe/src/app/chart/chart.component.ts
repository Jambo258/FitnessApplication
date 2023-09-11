import { Component, OnDestroy, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { MyApiServiceService } from '../my-api-service.service';
import { AuthenticationService } from '../authentication.service';
import { Subscription, catchError, mergeMap, switchMap, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { CompletedTrainingComponent } from '../completed-training/completed-training.component';
import { Router } from '@angular/router';

interface HealthData {
  height: Number;
  weight: Number;
  bmi: Number;
  creator: string;
  created_at: Number;
  targetCalories: Number;
  targetSteps: Number;
  targetWeight: string;
}

interface TrainingData {
  id: string;
  creator: string;
  created_at: Number;
  daily_calories: Number;
  daily_steps: Number;
  current_weight: string;
}

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss'],
})
export class ChartComponent implements OnInit, OnDestroy {
  public chart1: any;
  public chart2: any;
  public chart3: any;
  isAnyEqual: boolean = false;

  private userHealthDataSubscription!: Subscription;
  private trainingDataSubscription!: Subscription;

  healthData: HealthData = {
    height: 0,
    weight: 0,
    bmi: 0,
    creator: '',
    created_at: 0,
    targetCalories: 0,
    targetSteps: 0,
    targetWeight: '',
  };

  trainingData: TrainingData[] = [];

  Id: string = '';

  constructor(
    private apiService: MyApiServiceService,
    private authService: AuthenticationService,
    private dialog: MatDialog,
    private router: Router ,
  ) {}

  createChart1() {
    console.log(this.healthData);
    console.log(this.trainingData);
    const dailyCaloriesValue = this.trainingData.map(
      (data) => data.daily_calories
    );
    console.log(dailyCaloriesValue);
    const targetCaloriesValue = this.healthData.targetCalories;
    const numDates = this.trainingData.length;

    const targetCaloriesData = new Array(numDates).fill(targetCaloriesValue);
    console.log(targetCaloriesData);
    console.log(targetCaloriesValue);
    const datesInMilliseconds = this.trainingData.map(
      (data) => data.created_at
    );

    const dates = datesInMilliseconds.map((data: any) => this.formatDate(data));

    console.log(dates);
    this.chart1 = new Chart('MyChart1', {
      type: 'line', //this denotes tha type of chart

      data: {
        // values on X-Axis
        labels: dates,
        datasets: [
          {
            label: 'Target Calories',
            data: targetCaloriesData,
            borderColor: 'red',
            backgroundColor: 'limegreen',
            pointBackgroundColor: 'red',
          },
          {
            label: 'Done Calories',
            data: dailyCaloriesValue,
            borderColor: 'limegreen',
            backgroundColor: 'blue',
            pointBackgroundColor: 'blue',
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Calories Progress Chart',
            font: {
              size: 18,
              weight: 'bold',
            },
          },
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Date dd/mm/yyyy',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },

          y: {
            title: {
              display: true,
              text: 'Calories kCal',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },
        },
        aspectRatio: 2.5,
        interaction: {
          mode: 'index', // Prevents the "nearest" mode that activates on hover
          intersect: false, // Disables hover interactions
        },
      },
    });
  }

  createChart2() {
    console.log(this.healthData);
    console.log(this.trainingData);
    const dailyStepsValue = this.trainingData.map((data) => data.daily_steps);
    console.log(dailyStepsValue);
    const targetStepsValue = this.healthData.targetSteps;
    const numDates = this.trainingData.length;

    const targetStepsData = new Array(numDates).fill(targetStepsValue);
    console.log(targetStepsData);
    console.log(targetStepsValue);
    const datesInMilliseconds = this.trainingData.map(
      (data) => data.created_at
    );

    const dates = datesInMilliseconds.map((data: any) => this.formatDate(data));

    console.log(dates);
    this.chart2 = new Chart('MyChart2', {
      type: 'line', //this denotes tha type of chart

      data: {
        // values on X-Axis
        labels: dates,
        datasets: [
          {
            label: 'Target Steps',
            data: targetStepsData,
            borderColor: 'red',
            backgroundColor: 'limegreen',
            pointBackgroundColor: 'red',
          },
          {
            label: 'Done Steps',
            data: dailyStepsValue,
            borderColor: 'limegreen',
            backgroundColor: 'blue',
            pointBackgroundColor: 'blue',
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Steps Progress Chart',
            font: {
              size: 18,
              weight: 'bold',
            },
          },
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Date dd/mm/yyyy',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },

          y: {
            title: {
              display: true,
              text: 'Daily Steps',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },
        },
        aspectRatio: 2.5,
        interaction: {
          mode: 'index', // Prevents the "nearest" mode that activates on hover
          intersect: false, // Disables hover interactions
        },
      },
    });
  }

  createChart3() {
    console.log(this.healthData);
    console.log(this.trainingData);
    const dailyWeightValue = this.trainingData.map(
      (data) => data.current_weight
    );
    console.log(dailyWeightValue);
    const targetWeightValue = this.healthData.targetWeight;
    const numDates = this.trainingData.length;

    const targetWeightData = new Array(numDates).fill(targetWeightValue);
    console.log(targetWeightData);
    console.log(targetWeightValue);
    const datesInMilliseconds = this.trainingData.map(
      (data) => data.created_at
    );

    const dates = datesInMilliseconds.map((data: any) => this.formatDate(data));


    this.isAnyEqual = dailyWeightValue.some(
      (weight) => weight === targetWeightValue
    );

    if (this.isAnyEqual) {
      console.log(
        'At least one element in dailyWeightValue is equal to targetWeightValue.'
      );
    }

    console.log(dates);
    this.chart3 = new Chart('MyChart3', {
      type: 'line', //this denotes tha type of chart

      data: {
        // values on X-Axis
        labels: dates,
        datasets: [
          {
            label: 'Target Weight',
            data: targetWeightData,
            borderColor: 'red',
            backgroundColor: 'limegreen',
            pointBackgroundColor: 'red',
          },
          {
            label: 'Current Weight',
            data: dailyWeightValue,
            borderColor: 'limegreen',
            backgroundColor: 'blue',
            pointBackgroundColor: 'blue',
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Weight Progress Chart',
            font: {
              size: 18,
              weight: 'bold',
            },
          },
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Date dd/mm/yyyy',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },

          y: {
            title: {
              display: true,
              text: 'Weight (kg)',
              font: {
                size: 14,
                weight: 'bold',
              },
            },
          },
        },
        aspectRatio: 2.5,
        interaction: {
          mode: 'index', // Prevents the "nearest" mode that activates on hover
          intersect: false, // Disables hover interactions
        },
      },
    });
  }

  ngOnInit(): void {
    this.Id = this.authService.getId();
    console.log(this.Id, 'Id');
    this.userHealthDataSubscription = this.apiService
      .getUserHealthData(this.Id)
      .subscribe((userHealthData) => {
        console.log(userHealthData);
        this.healthData = userHealthData[0];
        //this.initializeForm();
        this.fetchTrainingDayData();
        //this.createChart1();
        //this.createChart2();
      });
  }

  fetchTrainingDayData() {
    // Perform additional fetch here
    this.trainingDataSubscription = this.apiService
      .getUserTrainingDayData(this.Id)
      .subscribe((userTrainingData) => {
        console.log(userTrainingData);
        //this.healthData = userTrainingData;
        this.trainingData = userTrainingData;
        console.log(this.trainingData, 'is data here');
        //this.initializeForm();
        this.createChart1();
        this.createChart2();
        this.createChart3();
        //this.fetchAdditionalData();
      });
  }

  ngOnDestroy() {
    if (this.userHealthDataSubscription) {
      this.userHealthDataSubscription.unsubscribe();
    }

    if (this.trainingDataSubscription) {
      this.trainingDataSubscription.unsubscribe();
    }
  }

  formatDate(milliseconds: any) {
    const date = new Date(milliseconds);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  deleteUserTrainingAndHealthData(userId: string) {
    const dialogRef = this.dialog.open(CompletedTrainingComponent, {
      height: '200px',
      width: '400px',
      data: {userId},
    });

    dialogRef
      .afterClosed()
      .pipe(
        switchMap((result) => {
          if (result === true) {
            return this.apiService.deleteUsersAllTrainingDays(userId).pipe(
              mergeMap(() => {
                // After successfully deleting training data, delete health data
                return this.apiService.deleteUserHealthData(userId);
              })
            );
          } else {
            return throwError(
              () => 'deleting User Training and HealthData canceled.'
            );
          }
        }),
        catchError((error) => {
          console.error('Error deleting user training and healthdata:', error);
          // Handle error, e.g., show an error message
          return throwError(() => error);
        })
      )
      .subscribe((response) => {
        console.log('User Training and healthData deleted:', response);
        localStorage.setItem('healthdata', 'false');
        // Handle success, e.g., show a success message
        this.router.navigate(['/home']);
        // Handle success, e.g., show a success message
      });
  }
}
