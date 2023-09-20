import { Injectable } from '@angular/core';
import { TokenCheckService } from './token-check.service';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(
    private jwtHelper: JwtHelperService,

  ) {}


  isTokenExpired(): boolean {
    const token = localStorage.getItem('token');


    if (token && !this.jwtHelper.isTokenExpired(token)) {
      return false;
    }

    return true;
  }

  getHealthData(): string {
    const healthDataValue = localStorage.getItem('healthdata');
    //console.log(healthDataValue)
    if (healthDataValue === 'true') {
      return 'true';
    } else {
      return 'false';
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('healthdata');
    localStorage.removeItem('tokenExpiration');

  }
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');


    if (!token || this.jwtHelper.isTokenExpired(token)) {

      localStorage.removeItem('token');
      localStorage.removeItem('healthdata');
      localStorage.removeItem('tokenExpiration');

      return false;
    }

    return true;
  }

  getRole(): string {
    const token = localStorage.getItem('token');
    //console.log('token', token);

    if (token) {

      const decodedToken = this.jwtHelper.decodeToken(token);

      if (decodedToken) {
        //console.log('Decoded token:', decodedToken);
        return decodedToken.role || '';
      }
    }

    return '';
  }

  getId(): string {
    const token = localStorage.getItem('token');

    if (token) {

      const decodedToken = this.jwtHelper.decodeToken(token);

      if (decodedToken) {
        //console.log('Decoded token:', decodedToken);
        return decodedToken.id || '';
      }
    }

    return '';
  }

  getToken(): string {
    const token = localStorage.getItem('token');

    if (token) {
      return token;
    }

    return '';
  }
}
