import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ParticipantsListComponent} from "./list/participants-list.component";
import {ParticipantsCreateComponent} from "./form/participants-create.component";

const routes: Routes = [
  { path: '', component: ParticipantsListComponent },
  { path: 'create', component: ParticipantsCreateComponent },
  { path: 'edit/:id', component: ParticipantsCreateComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ParticipantsRoutingModule {}
