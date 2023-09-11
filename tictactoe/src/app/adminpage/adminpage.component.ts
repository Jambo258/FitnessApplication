import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MyApiServiceService } from '../my-api-service.service';
import { catchError, switchMap, throwError } from 'rxjs';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { UpdateDialogComponent } from '../update-dialog/update-dialog.component';

@Component({
  selector: 'app-adminpage',
  templateUrl: './adminpage.component.html',
  styleUrls: ['./adminpage.component.scss'],
})
export class AdminpageComponent implements OnInit {
  displayedColumns: string[] = ['id', 'username', 'email', 'role', 'actions'];
  dataSource: any;
  errorMessage: string = '';
  isDialogOpen: boolean = false;

  constructor(
    public dialog: MatDialog,
    private apiService: MyApiServiceService
  ) {}

  ngOnInit(): void {
    this.apiService.getAllUserData().subscribe((data: any) => {
      this.dataSource = data;
    });
  }

  updateUser(user: any) {
    if (!this.isDialogOpen) {
      this.isDialogOpen = true;
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '600px'; // Set your desired width
      dialogConfig.height = 'auto'; // Set auto or a specific height
      dialogConfig.panelClass = 'center-dialog';

      const dialogRef = this.dialog.open(UpdateDialogComponent, {
        width: '600px',
        data: user,

      });

      dialogRef.componentInstance.saveClicked.subscribe((response: any) => {
        // Handle the response data here
        console.log('Received response:', response);

        this.dataSource = this.dataSource.map((dataSourceUser: any) => {
          console.log(response.id + '||' + dataSourceUser.id);
          if (dataSourceUser.id === response.id) {
            return { ...dataSourceUser, ...response };
          }
          return dataSourceUser;
        });
        //dialogRef.close();
        //this.isDialogOpen = false;
      });

      dialogRef.afterClosed().subscribe((result) => {
        this.isDialogOpen = false;
        console.log(`Dialog result: ${result}`);
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
        this.dataSource = this.dataSource.filter(
          (user: any) => user.id !== userId
        );
        // Handle success, e.g., show a success message
      });
  }
}
