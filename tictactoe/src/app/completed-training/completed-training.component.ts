import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-completed-training',
  templateUrl: './completed-training.component.html',
  styleUrls: ['./completed-training.component.scss'],
})
export class CompletedTrainingComponent {
  constructor(
    public dialogRef: MatDialogRef<CompletedTrainingComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    
  }
}
