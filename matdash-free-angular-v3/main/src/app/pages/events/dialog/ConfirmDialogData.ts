// src/app/components/confirm-dialog/confirm-dialog.component.ts

import { Component, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef} from '@angular/material/dialog';

export interface ConfirmDialogData {
  title: string;
  message: string;
}

@Component({
  selector: 'app-confirm-dialog',
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <p>{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancelar</button>
      <button mat-raised-button color="warn" (click)="onConfirm()">Confirmar</button>
    </mat-dialog-actions>
  `,
  imports: [
    MatDialogContent,
    MatDialogActions
  ],
  styles: [`
    mat-dialog-content {
      min-width: 300px;
      padding: 20px 0;
    }

    p {
      margin: 0;
      color: rgba(0, 0, 0, 0.7);
    }
  `]
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
