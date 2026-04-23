import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ParticipantsListComponent } from './list/participants-list.component';
import { ParticipantsCreateComponent } from './form/participants-create.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import {ParticipantsRoutingModule} from "./ParticipantsRoutingModule";

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ParticipantsRoutingModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatCardModule,
    ParticipantsListComponent,
    ParticipantsCreateComponent
  ]
})
export class ParticipantsModule {}
