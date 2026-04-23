import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {CreateParticipantDTO} from "../../../models/createParticipantDTO";
import {ParticipantDTO} from "../../../models/ParticipantDTO ";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ParticipantService {

  private readonly baseUrl =  `${environment.url}participants`;

  constructor(private http: HttpClient) {}

  createParticipant(dto: CreateParticipantDTO): Observable<ParticipantDTO> {
    return this.http.post<ParticipantDTO>(this.baseUrl, dto);
  }

  getAllParticipants(): Observable<ParticipantDTO[]> {
    return this.http.get<ParticipantDTO[]>(this.baseUrl);
  }

  getParticipantById(id: number): Observable<ParticipantDTO> {
    return this.http.get<ParticipantDTO>(`${this.baseUrl}/${id}`);
  }

  updateParticipant(id: number, dto: CreateParticipantDTO): Observable<ParticipantDTO> {
    return this.http.put<ParticipantDTO>(`${this.baseUrl}/${id}`, dto);
  }

  deleteParticipant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
