// src/app/components/event-list/event-list.component.ts

import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {EventService} from "../../../providers/services/Event/event.service";
import {EventResponseDTO} from "../../../models/events/EventResponseDTO";
import {EventStatus} from "../../../models/events/EventStatus";
import {EventModality} from "../../../models/events/EventModality";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";


import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { FormsModule } from '@angular/forms';
import {CommonModule, DatePipe} from "@angular/common";
import {EventFormComponent} from "../form/EventFormComponent";
import {ConfirmDialogComponent} from "../dialog/ConfirmDialogData";
@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  imports: [
    CommonModule,
    FormsModule,

    // Angular Material modules
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatChipsModule,
    MatMenuModule,
    MatDividerModule,
    DatePipe
  ],
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  displayedColumns: string[] = ['eventId', 'name', 'startDate', 'endDate', 'modality', 'eventType', 'status', 'actions'];
  dataSource: MatTableDataSource<EventResponseDTO>;

  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  statusFilter: EventStatus | '' = '';
  modalityFilter: EventModality | '' = '';

  eventStatuses = Object.values(EventStatus);
  eventModalities = Object.values(EventModality);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  // Este valor debe venir del servicio de autenticación
  currentOrganizerId = 1; // TODO: Obtener del servicio de autenticación

  constructor(
    private eventService: EventService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.dataSource = new MatTableDataSource<EventResponseDTO>([]);
  }

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getAllEvents(this.pageIndex, this.pageSize, 'eventId', 'DESC')
      .subscribe({
        next: (response) => {
          this.dataSource.data = response.content;
          this.totalElements = response.totalElements;
        },
        error: (error) => {
          this.showMessage('Error al cargar eventos', 'error');
          console.error(error);
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadEvents();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  filterByStatus(): void {
    if (this.statusFilter) {
      this.eventService.getEventsByStatus(this.statusFilter, this.pageIndex, this.pageSize)
        .subscribe({
          next: (response) => {
            this.dataSource.data = response.content;
            this.totalElements = response.totalElements;
          },
          error: (error) => {
            this.showMessage('Error al filtrar por estado', 'error');
            console.error(error);
          }
        });
    } else {
      this.loadEvents();
    }
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(EventFormComponent, {
      width: '800px',
      data: { organizerId: this.currentOrganizerId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEvents();
        this.showMessage('Evento creado exitosamente', 'success');
      }
    });
  }

  openEditDialog(event: EventResponseDTO): void {
    const dialogRef = this.dialog.open(EventFormComponent, {
      width: '800px',
      data: { event, organizerId: this.currentOrganizerId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEvents();
        this.showMessage('Evento actualizado exitosamente', 'success');
      }
    });
  }

  deleteEvent(event: EventResponseDTO): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Confirmar eliminación',
        message: `¿Está seguro de que desea eliminar el evento "${event.name}"?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.eventService.deleteEvent(event.eventId, this.currentOrganizerId)
          .subscribe({
            next: () => {
              this.loadEvents();
              this.showMessage('Evento eliminado exitosamente', 'success');
            },
            error: (error) => {
              this.showMessage('Error al eliminar evento', 'error');
              console.error(error);
            }
          });
      }
    });
  }

  changeStatus(event: EventResponseDTO, newStatus: EventStatus): void {
    this.eventService.changeEventStatus(event.eventId, newStatus, this.currentOrganizerId)
      .subscribe({
        next: () => {
          this.loadEvents();
          this.showMessage('Estado actualizado exitosamente', 'success');
        },
        error: (error) => {
          this.showMessage('Error al cambiar estado', 'error');
          console.error(error);
        }
      });
  }

  getStatusColor(status: EventStatus): string {
    switch (status) {
      case EventStatus.ACTIVE:
        return 'warn';
      case EventStatus.INACTIVE:
        return 'accent';
      default:
        return 'basic';
    }
  }

  showMessage(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Cerrar', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: type === 'success' ? 'success-snackbar' : 'error-snackbar'
    });
  }
}
