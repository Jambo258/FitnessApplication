import { Component, OnInit } from '@angular/core';
import { MyApiServiceService } from '../my-api-service.service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.scss'],
})
export class LeaderboardComponent implements OnInit {
  userDataArray: any;
  errorMessage: string = '';
  constructor(
    private leaderboardService: MyApiServiceService
  ) {}

  fetchUserData() {
    this.leaderboardService
      .getAllUserDataCombined()
      .pipe(
        catchError((error) => {
          console.error('Error fetching data:', error);
          this.errorMessage = error.error.message;

          return throwError(() => error);
        })
      )
      .subscribe((response) => {
        console.log('fetched data:', response);

        this.userDataArray = response;
        console.log(this.userDataArray)
        this.userDataArray.sort((a:any, b:any) => (a.weight_difference) - (b.weight_difference));
        console.log(this.userDataArray)

      });
  }


  ngOnInit() {
    this.fetchUserData();
  }
}

