import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogConfig,
} from '@angular/material/dialog';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss'],
})
export class DeleteDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { userId: string }
  ) {
    const dialogConfig = new MatDialogConfig();
    //const centerX = window.innerWidth / 2;
    //const centerY = window.innerHeight / 2;
    dialogConfig.autoFocus = false;
    //dialogConfig.position = {};
    dialogConfig.position = {
      //top: '50%',
      //left: '50%',
      //transform: 'translate(-50%, -50%)',
    };
    dialogRef.updatePosition(dialogConfig.position);
  }
}
