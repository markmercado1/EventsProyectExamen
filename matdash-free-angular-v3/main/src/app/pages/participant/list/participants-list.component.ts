import { Component, OnInit } from '@angular/core';
import { ParticipantService } from "../../../providers/services/participant/participant.service";
import { MatTableModule } from "@angular/material/table";
import { RouterLink } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";
import {ParticipantDTO} from "../../../models/ParticipantDTO ";
import {MatCard} from "@angular/material/card";
import {CommonModule} from "@angular/common";
import { MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-participants-list',
  standalone: true,
  imports: [
    MatTableModule,
    RouterLink,
    MatIconModule,
    MatCard,
    CommonModule,
    MatPaginatorModule,
  ],
  templateUrl: './participants-list.component.html'
})
export class ParticipantsListComponent implements OnInit {

  participants: ParticipantDTO[] = [];
  displayedColumns = ['participantId', 'name', 'email', 'phone', 'actions'];

  constructor(private participantService: ParticipantService) {}

  ngOnInit(): void {
    this.loadParticipants();
  }

  loadParticipants() {
    this.participantService.getAllParticipants().subscribe({
      next: data => this.participants = data
    });
  }

  delete(id: number) {
    if (confirm('Delete this participant?')) {
      this.participantService.deleteParticipant(id).subscribe(() => {
        this.loadParticipants();
      });
    }
  }
  getRandomColor(seed: string): string {
    const colors = [
      '#3f51b5', '#2196f3', '#00bcd4', '#4caf50',
      '#8bc34a', '#ff9800', '#f44336', '#9c27b0',
      '#673ab7', '#e91e63'
    ];
    let hash = 0;
    for (let i = 0; i < seed.length; i++) {
      hash = seed.charCodeAt(i) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash % colors.length)];
  }
}
