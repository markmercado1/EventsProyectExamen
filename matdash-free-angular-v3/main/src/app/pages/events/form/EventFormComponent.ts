// src/app/components/event-form/event-form.component.ts

import { Component, Inject, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {EventModality} from "../../../models/events/EventModality";
import {EventStatus} from "../../../models/events/EventStatus";
import {EventType} from "../../../models/events/EventType";
import {EventService} from "../../../providers/services/Event/event.service";
import {EventResponseDTO} from "../../../models/events/EventResponseDTO";
import {EventCreateDTO} from "../../../models/events/EventCreateDTO";
import {EventUpdateDTO} from "../../../models/events/EventUpdateDTO";
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,

    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    MatProgressSpinnerModule,
  ]
})
export class EventFormComponent implements OnInit {
  eventForm: FormGroup;
  isEditMode = false;
  loading = false;

  eventModalities = Object.values(EventModality);
  eventTypes = Object.values(EventType);
  eventStatuses = Object.values(EventStatus);

  minDate = new Date();

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private dialogRef: MatDialogRef<EventFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { event?: EventResponseDTO; organizerId: number }
  ) {
    this.eventForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.data.event) {
      this.isEditMode = true;
      this.populateForm(this.data.event);
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(150)]],
      description: ['', [Validators.maxLength(5000)]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      modality: ['', [Validators.required]],
      eventType: ['', [Validators.required]],
      maxCapacity: [0, [Validators.min(0)]],
      address: ['', [Validators.maxLength(255)]],
      status: [EventStatus.ACTIVE]
    });
  }

  populateForm(event: EventResponseDTO): void {
    this.eventForm.patchValue({
      name: event.name,
      description: event.description,
      startDate: event.startDate,
      endDate: event.endDate,
      modality: event.modality,
      eventType: event.eventType,
      maxCapacity: event.maxCapacity,
      address: event.address,
      status: event.status
    });
  }

  onSubmit(): void {
    if (this.eventForm.invalid) {
      this.eventForm.markAllAsTouched();
      return;
    }

    this.loading = true;

    if (this.isEditMode) {
      this.updateEvent();
    } else {
      this.createEvent();
    }
  }

  createEvent(): void {
    const formValue = this.eventForm.value;
    const createDTO: EventCreateDTO = {
      name: formValue.name,
      description: formValue.description,
      startDate: this.formatDate(formValue.startDate),
      endDate: this.formatDate(formValue.endDate),
      modality: formValue.modality,
      eventType: formValue.eventType,
      maxCapacity: formValue.maxCapacity || 0,
      organizerId: this.data.organizerId,
      address: formValue.address
    };

    this.eventService.createEvent(createDTO).subscribe({
      next: (response) => {
        this.loading = false;
        this.dialogRef.close(response);
      },
      error: (error) => {
        this.loading = false;
        console.error('Error creating event:', error);
        alert('Error al crear el evento. Por favor, intente nuevamente.');
      }
    });
  }

  updateEvent(): void {
    const formValue = this.eventForm.value;
    const updateDTO: EventUpdateDTO = {
      name: formValue.name,
      description: formValue.description,
      startDate: this.formatDate(formValue.startDate),
      endDate: this.formatDate(formValue.endDate),
      modality: formValue.modality,
      eventType: formValue.eventType,
      maxCapacity: formValue.maxCapacity,
      address: formValue.address,
      status: formValue.status
    };

    this.eventService.updateEvent(this.data.event!.eventId, updateDTO, this.data.organizerId)
      .subscribe({
        next: (response) => {
          this.loading = false;
          this.dialogRef.close(response);
        },
        error: (error) => {
          this.loading = false;
          console.error('Error updating event:', error);
          alert('Error al actualizar el evento. Por favor, intente nuevamente.');
        }
      });
  }

  formatDate(date: Date | string): string {
    if (typeof date === 'string') {
      return date;
    }
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  get f() {
    return this.eventForm.controls;
  }

  getErrorMessage(fieldName: string): string {
    const control = this.eventForm.get(fieldName);

    if (control?.hasError('required')) {
      return 'Este campo es requerido';
    }

    if (control?.hasError('maxlength')) {
      const maxLength = control.errors?.['maxlength'].requiredLength;
      return `Máximo ${maxLength} caracteres`;
    }

    if (control?.hasError('min')) {
      return 'El valor debe ser mayor o igual a 0';
    }

    return '';
  }
}
