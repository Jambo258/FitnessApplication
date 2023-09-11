import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuardService implements CanActivate{
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    if (
      this.authService.isLoggedIn() &&
      this.authService.getRole() == 'guest'
    ) {
      return true;
    }

    
    return this.router.createUrlTree(['/home']);
  }
}
