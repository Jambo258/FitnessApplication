//import { Component } from '@angular/core';
import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyApiServiceService } from '../my-api-service.service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-update-dialog',
  templateUrl: './update-dialog.component.html',
  styleUrls: ['./update-dialog.component.scss'],
})
export class UpdateDialogComponent {
  dropdownOptions: string[] = ['admin', 'guest'];
  selectedOption: string = this.data.role;

  @Output() saveClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() dialogClosed: EventEmitter<void> = new EventEmitter<void>();

  isEditing: { [key: string]: boolean } = {
    username: false,
    email: false,
    password: false,
    role: false,
  };
  errorMessage: string = '';
  form: FormGroup;

  editingControlName: string = '';

  constructor(
    public dialogRef: MatDialogRef<UpdateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder,
    private apiService: MyApiServiceService,
    private dialog: MatDialog
  ) {
    this.form = this.fb.group({
      username: [data.username, [Validators.required, Validators.minLength(5)]],
      email: [data.email, [Validators.required, Validators.email]],
      password: [data.password, [Validators.required, Validators.minLength(5)]],
      role: [data.role, Validators.required],
      selectedOption: [this.selectedOption],
    });

  }

  startEditing(controlName: string) {
    this.isEditing[controlName] = true;
    this.editingControlName = controlName;
  }

  saveEditing(controlName: string, userId: string) {

    const formControl = this.form.get(controlName);

    if (formControl && formControl.valid) {
      this.onSubmit(userId);

    }
  }

  cancelEditing(controlName: string) {
    this.isEditing[controlName] = false;

    this.errorMessage = '';
    this.form.get(controlName)?.setValue(this.data[controlName]);
  }

  closeDialog() {
    this.dialogClosed.emit();

  }

  onSubmit(userId: string) {
    if (this.form.valid) {
      //const updatedUserData = this.form.value;
      this.selectedOption = this.form.get('selectedOption')?.value;
      const updatedUserData = {
        username: this.form.get('username')?.value || '',
        email: this.form.get('email')?.value || '',
        password: this.form.get('password')?.value || '',
        role: this.selectedOption,
      };


      this.apiService.UpdateUser(userId, updatedUserData).subscribe({
        next: (response) => {
          console.log('User updated on server:', response);
          //console.log('DataSource before update:', this.dataSource);
          console.log('UserData' + updatedUserData);
          this.data.username = response.username;
          this.data.email = response.email;
          this.data.password = response.password;
          this.data.role = response.role;

          this.saveClicked.emit(response);

          this.errorMessage = '';
          this.isEditing[this.editingControlName] = false;
        },
        error: (error) => {
          this.errorMessage = error.error.error;
          console.error('Error updating user on server:', error);

        },
      });
    }
  }
}



