import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ParticipantService } from '../../../providers/services/participant/participant.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CreateParticipantDTO } from '../../../models/createParticipantDTO';

@Component({
  selector: 'app-participants-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './participants-create.component.html'
})
export class ParticipantsCreateComponent implements OnInit {

  isEdit = false;
  id!: number;

  form = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private service: ParticipantService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (this.id) {
      this.isEdit = true;
      this.loadParticipant();
    }
  }

  loadParticipant() {
    this.service.getParticipantById(this.id).subscribe(p => {
      this.form.patchValue({
        firstName: p.firstName,
        lastName: p.lastName,
        email: p.email,
        phone: p.phone
      });
    });
  }

  save() {
    if (this.form.invalid) return;

    const dto: CreateParticipantDTO = {
      firstName: this.form.value.firstName!,
      lastName: this.form.value.lastName!,
      email: this.form.value.email!,
      phone: this.form.value.phone!
    };

    if (this.isEdit) {
      this.service.updateParticipant(this.id, dto).subscribe(() => {
        this.router.navigate(['/participants']);
      });
    } else {
      this.service.createParticipant(dto).subscribe(() => {
        this.router.navigate(['/participants']);
      });
    }
  }
}
