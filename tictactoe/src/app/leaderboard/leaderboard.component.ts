import { Component } from '@angular/core';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.scss'],
})
export class LeaderboardComponent {
  leaderboardData = [
    { name: 'User 1', weightLoss: 10 },
    { name: 'User 2', weightLoss: 8 },
    { name: 'User 3', weightLoss: 15 },
    { name: 'User 4', weightLoss: 15 },
    
  ];
}
