import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, Subscription, interval } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class TokenCheckService {
  private tokenCheckInterval!: Subscription;

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}


  startTokenExpirationCheck(): void {

    this.tokenCheckInterval = interval(60000).subscribe(() => {
      //console.log('check between')
      if (this.authService.isTokenExpired()) {
        //console.log('Token has expired. Logging out...');
        this.authService.logout();
        this.router.navigate(['/home']);
        this.stopTokenExpirationCheck();
      }
    });
  }

  stopTokenExpirationCheck(): void {
    if (this.tokenCheckInterval) {
      this.tokenCheckInterval.unsubscribe();
    }
  }

}
