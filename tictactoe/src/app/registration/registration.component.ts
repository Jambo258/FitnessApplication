import { Component, OnInit } from '@angular/core';
import { MyApiServiceService } from '../my-api-service.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { catchError, throwError } from 'rxjs';
import { TokenCheckService } from '../token-check.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private registrationService: MyApiServiceService,
    private router: Router,
    private jwtHelper: JwtHelperService,
    private tokenExpirationService: TokenCheckService
  ) {}

  ngOnInit() {
    this.registrationForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, Validators.minLength(5)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(5)]],
        retypepassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const retypepassword = control.get('retypepassword')?.value;
    return password === retypepassword ? null : { passwordMismatch: true };
  }

  register() {
    if (this.registrationForm.valid) {
      //const formData = this.registrationForm.value;
      const formData = {
        username: this.registrationForm.get('username')?.value || '',
        email: this.registrationForm.get('email')?.value || '',
        password: this.registrationForm.get('password')?.value || '',
        role: 'guest',
      };

      //console.log(formData);
      this.registrationService
        .registerUser(formData)
        .pipe(
          catchError((error) => {
            // Handle the error here
            this.errorMessage = error.error.error;
            return throwError(() => error);
          })
        )
        .subscribe((response) => {
          // Handle success or errors
          //console.log(response.token);
          const decodedToken = this.jwtHelper.decodeToken(response.token);

          if (decodedToken) {
            //console.log('Decoded token:', decodedToken);
          }
          localStorage.setItem('token', response.token);
          localStorage.setItem('healthdata', decodedToken.healthdata);
          localStorage.setItem('tokenExpiration', decodedToken.exp.toString());
          this.tokenExpirationService.startTokenExpirationCheck();
          this.router.navigate(['/home']);
        });
    }
  }
}
