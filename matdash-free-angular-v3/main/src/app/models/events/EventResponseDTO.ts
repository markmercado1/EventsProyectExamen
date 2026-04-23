import {EventModality} from "./EventModality";
import {EventType} from "./EventType";
import {EventStatus} from "./EventStatus";

export interface EventResponseDTO {
  eventId: number;
  name: string;
  description?: string;
  startDate: string;
  endDate: string;
  modality: EventModality;
  eventType: EventType;
  maxCapacity?: number;
  organizerId: number;
  organizer?: string;
  address?: string;
  status: EventStatus;
}
