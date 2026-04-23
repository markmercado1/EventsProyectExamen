import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


import {environment} from "../../../../environments/environment";
import {EventCreateDTO} from "../../../models/events/EventCreateDTO";
import {EventResponseDTO} from "../../../models/events/EventResponseDTO";
import {EventUpdateDTO} from "../../../models/events/EventUpdateDTO";
import {PageResponse} from "../../../models/events/PageResponseDTO";
import {EventStatus} from "../../../models/events/EventStatus";
import {EventModality} from "../../../models/events/EventModality";
import {EventSummaryDTO} from "../../../models/events/EventSummaryDTO";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = `${environment.url}events`;

  constructor(private http: HttpClient) {}

  createEvent(event: EventCreateDTO): Observable<EventResponseDTO> {
    return this.http.post<EventResponseDTO>(this.apiUrl, event);
  }

  updateEvent(eventId: number, event: EventUpdateDTO, organizerId: number): Observable<EventResponseDTO> {
    const headers = new HttpHeaders().set('X-Organizer-Id', organizerId.toString());
    return this.http.put<EventResponseDTO>(`${this.apiUrl}/${eventId}`, event, { headers });
  }

  getEventById(eventId: number): Observable<EventResponseDTO> {
    return this.http.get<EventResponseDTO>(`${this.apiUrl}/${eventId}`);
  }

  getEventByIdWithOrganizer(eventId: number): Observable<EventResponseDTO> {
    return this.http.get<EventResponseDTO>(`${this.apiUrl}/${eventId}/with-organizer`);
  }

  getAllEvents(page: number = 0, size: number = 10, sortBy: string = 'eventId', sortDirection: string = 'DESC'): Observable<PageResponse<EventResponseDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<PageResponse<EventResponseDTO>>(this.apiUrl, { params });
  }

  getEventsByStatus(status: EventStatus, page: number = 0, size: number = 10): Observable<PageResponse<EventResponseDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<EventResponseDTO>>(`${this.apiUrl}/status/${status}`, { params });
  }

  getEventsByOrganizerId(organizerId: number): Observable<EventResponseDTO[]> {
    return this.http.get<EventResponseDTO[]>(`${this.apiUrl}/organizer/${organizerId}`);
  }

  getEventsByOrganizerIdAndStatus(organizerId: number, status: EventStatus, page: number = 0, size: number = 10): Observable<PageResponse<EventResponseDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<EventResponseDTO>>(`${this.apiUrl}/organizer/${organizerId}/status/${status}`, { params });
  }

  getEventsByModality(modality: EventModality): Observable<EventSummaryDTO[]> {
    return this.http.get<EventSummaryDTO[]>(`${this.apiUrl}/modality/${modality}`);
  }

  getEventsBetweenDates(startDate: string, endDate: string): Observable<EventSummaryDTO[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get<EventSummaryDTO[]>(`${this.apiUrl}/between-dates`, { params });
  }

  getUpcomingEvents(page: number = 0, size: number = 10): Observable<PageResponse<EventSummaryDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<EventSummaryDTO>>(`${this.apiUrl}/upcoming`, { params });
  }

  deleteEvent(eventId: number, organizerId: number): Observable<void> {
    const headers = new HttpHeaders().set('X-Organizer-Id', organizerId.toString());
    return this.http.delete<void>(`${this.apiUrl}/${eventId}`, { headers });
  }

  changeEventStatus(eventId: number, status: EventStatus, organizerId: number): Observable<EventResponseDTO> {
    const headers = new HttpHeaders().set('X-Organizer-Id', organizerId.toString());
    const params = new HttpParams().set('status', status);

    return this.http.patch<EventResponseDTO>(`${this.apiUrl}/${eventId}/status`, null, { headers, params });
  }

  isOrganizerOwner(eventId: number, organizerId: number): Observable<boolean> {
    const params = new HttpParams().set('organizerId', organizerId.toString());
    return this.http.get<boolean>(`${this.apiUrl}/${eventId}/is-owner`, { params });
  }
}
