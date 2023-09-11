import { Component, OnDestroy, OnInit } from '@angular/core';
import { MyApiServiceService } from '../my-api-service.service';
import { AuthenticationService } from '../authentication.service';
import { Subscription, catchError, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { MatDialog } from '@angular/material/dialog';

interface UserData {
  password: string,
  role: string,
  id: string,
  email: string,
  username: string
}

@Component({
  selector: 'app-profilepage',
  templateUrl: './profilepage.component.html',
  styleUrls: ['./profilepage.component.scss'],
})
export class ProfilepageComponent implements OnInit, OnDestroy {
  activeEditingSection: string | null = null;
  Editing: { [key: string]: boolean } = {
    username: false,
    email: false,
    password: false
  };
  changeForm!: FormGroup;
  Id: string = '';
  username: string = '';
  email: string = '';
  password: string = '';
  newPassword: string = '';
  confirmPassword: string = '';

  profileData: UserData = {
    password: '',
    role: '',
    id: '',
    email: '',
    username: '',
  };
  errorMessage: string = '';

  private userDataSubscription!: Subscription;

  constructor(
    private apiService: MyApiServiceService,
    private authService: AuthenticationService, 
    private router: Router,
    private formBuilder: FormBuilder,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadProfileData();
  }

  loadProfileData() {
    this.Id = this.authService.getId();
    this.userDataSubscription = this.apiService
      .getUserData(this.Id)
      .subscribe((userData) => {
        console.log(userData);
        this.profileData = userData;
        this.initializeForm();

      });
  }
  initializeForm() {
    this.changeForm = this.formBuilder.group(
      {
        username: [
          this.profileData.username,
          [Validators.required, Validators.minLength(5)],
        ],
        email: [
          this.profileData.email,
          [Validators.required, Validators.email],
        ],
        password: [
          this.profileData.password,
          [Validators.required, Validators.minLength(5)],
        ],
        newPassword: ['', [Validators.required, Validators.minLength(5)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  startEditing(field: string) {
    this.Editing[field] = true;
    this.activeEditingSection = field;

  }

  saveEditing(field: string, userId: string) {
    // Handle form submission for the specific control
    const formControl = this.changeForm.get(field);

    if(field === 'password'){
      const newPasswordControl = this.changeForm.get('newPassword');
      const confirmPasswordControl = this.changeForm.get('confirmPassword');

      const newPasswordValue = this.changeForm.get('newPassword')?.value;
      const confirmPasswordValue = this.changeForm.get('confirmPassword')?.value;



      this.changeForm.get('newPassword')?.setValidators([Validators.required, Validators.minLength(5)]);
      this.changeForm
        .get('confirmPassword')
        ?.setValidators([
          Validators.required,
          this.passwordMatchValidator.bind(this.changeForm),
        ]);

      this.changeForm.get('newPassword')?.updateValueAndValidity();
      this.changeForm.get('confirmPassword')?.updateValueAndValidity();

      if (!newPasswordValue || !confirmPasswordValue) {
        console.log('Values are empty');
        return;
      }

      if(!newPasswordControl?.valid || !confirmPasswordControl?.valid){
        console.log('valid?')
        //this.Editing[field] = true;
        //this.activeEditingSection = null;
        return;
      }
    }

    console.log(this.changeForm.value, 'value')

    if (formControl && formControl.valid) {
      this.updateUser(userId);
      this.Editing[field] = false;
      this.activeEditingSection = null;
    }

  }

  passwordMatchValidator(control: AbstractControl) {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
  }

  ngOnDestroy() {
    this.userDataSubscription.unsubscribe();
  }

  updateUser(userId: string) {
    console.log(this.activeEditingSection + 'section');

    console.log(this.changeForm.value);


    if (
      this.changeForm.value.newPassword === '' &&
      this.changeForm.value.confirmPassword === '' && this.activeEditingSection !== 'password'
    ) {
      console.log('show up in here?');
      this.changeForm.get('newPassword')?.clearValidators();
      this.changeForm.get('newPassword')?.updateValueAndValidity();
      this.changeForm.get('confirmPassword')?.clearValidators();
      this.changeForm.get('confirmPassword')?.updateValueAndValidity();
      console.log(this.changeForm.valid, 'is valid?');
    }


    console.log(this.changeForm.valid, 'isvalid?');
    if (this.changeForm.valid) {
      //const changedData = this.changeForm.value;
      let updatedUserData;
      console.log(this.changeForm.value);
      console.log(this.profileData.password);
      if (

        this.activeEditingSection !== 'password'
      ) {
        updatedUserData = {
          username: this.changeForm.get('username')?.value || '',
          email: this.changeForm.get('email')?.value || '',
          password: this.profileData.password,
          role: 'guest',
        };
      } else {
        updatedUserData = {
          username: this.changeForm.get('username')?.value || '',
          email: this.changeForm.get('email')?.value || '',
          password: this.changeForm.get('confirmPassword')?.value || '',
          role: 'guest',
        };
      }

      console.log(updatedUserData.password, 'password');


      this.apiService
        .UpdateUser(userId, updatedUserData)
        .pipe(
          catchError((error) => {
            console.error('Error updating user:', error);
            this.errorMessage = error.error.message;
            // Handle error, e.g., show an error message
            return throwError(() => error);
          })
        )
        .subscribe((response) => {
          console.log('User updated:', response);
          // Handle success, e.g., show a success message
          this.profileData.username = response.username;
          this.profileData.email = response.email;
          this.profileData.password = response.password;
          //this.changeForm.reset();
        });
    }
  }

  deleteUser(userId: string) {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      height: '200px',
      width: '400px',
      data: { userId },
    });

    dialogRef
      .afterClosed()
      .pipe(
        switchMap((result) => {
          if (result === true) {
            return this.apiService.DeleteUser(userId);
          } else {
            return throwError(() => 'User deletion canceled.');
          }
        }),
        catchError((error) => {
          console.error('Error deleting user:', error);
          // Handle error, e.g., show an error message
          return throwError(() => error);
        })
      )
      .subscribe((response) => {
        console.log('User deleted:', response);
        // Handle success, e.g., show a success message
        this.authService.logout();
        this.router.navigate(['/home']);
        // Handle success, e.g., show a success message
      });
  }
}
