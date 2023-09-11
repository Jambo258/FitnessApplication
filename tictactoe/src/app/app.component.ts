import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { AuthenticationService } from './authentication.service';
import { Router } from '@angular/router';
import { TokenCheckService } from './token-check.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],

})
export class AppComponent implements OnInit {
  title = 'tictactoe';

  

  constructor(
    private tokenExpirationService: TokenCheckService,
    private authService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit() {

  }

  isLoggedIn(): boolean {

    return this.authService.isLoggedIn();
  }

  isRole(): string {

    return this.authService.getRole();
  }

  hasHealthData(): string {
    return this.authService.getHealthData();
  }

  logout(): void {
    this.authService.logout();
    this.tokenExpirationService.stopTokenExpirationCheck();
    this.router.navigate(['/home']);
  }
}
