import { Component, OnInit } from '@angular/core';
import { MyApiServiceService } from '../my-api-service.service';
import { catchError, throwError } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.scss'],
})
export class LeaderboardComponent implements OnInit {
  userDataArray: any;
  usersData: any;
  arrayFiltered: any [] = [];
  errorMessage: string = '';
  constructor(private leaderboardService: MyApiServiceService) {}

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
        console.log(this.userDataArray);

        this.userDataArray.forEach((obj: any) => {
          const item = this.arrayFiltered.find(
            (o:any) => o.id === obj.id && o.username === obj.username
          );
          if (item) {
            
            if (item.weight_difference > obj.weight_difference) {
              item.weight_difference = obj.weight_difference;
            }
            return;
          }

          this.arrayFiltered.push(obj);
        });

        this.arrayFiltered.sort(
          (a: any, b: any) => a.weight_difference - b.weight_difference
        );

        console.log(this.arrayFiltered);
        console.log(this.userDataArray);
        this.usersData = new MatTableDataSource(this.arrayFiltered);
      });
  }

  ngOnInit() {
    this.fetchUserData();
  }
}

