import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyApiServiceService } from '../my-api-service.service';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { catchError, throwError } from 'rxjs';
import { TokenCheckService } from '../token-check.service';

interface LoginResponse {
  token: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private registrationService: MyApiServiceService,
    private router: Router,
    private jwtHelper: JwtHelperService,
    private tokenExpirationService: TokenCheckService
  ) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]],
    });
  }
  login() {
    if (this.loginForm.valid) {
      const formData = this.loginForm.value;
      console.log(formData);
      this.registrationService
        .loginUser(formData)
        .pipe(
          catchError((error) => {
            // Handle the error here
            this.errorMessage = error.error.message;
            return throwError(() => error);
          })
        )
        .subscribe((response) => {
          const loginResponse = response as LoginResponse;
          // Handle success
          console.log(loginResponse);
          console.log(loginResponse.token);
          const decodedToken = this.jwtHelper.decodeToken(loginResponse.token);

          if (decodedToken) {
            console.log('Decoded token:', decodedToken);
          }
          console.log(decodedToken.exp + 'logatessa aika');
          localStorage.setItem('token', loginResponse.token);
          localStorage.setItem('healthdata', decodedToken.healthdata);
          localStorage.setItem('tokenExpiration', decodedToken.exp.toString());
          this.tokenExpirationService.startTokenExpirationCheck();
          this.router.navigate(['/home']);
        });
    }
  }
}
