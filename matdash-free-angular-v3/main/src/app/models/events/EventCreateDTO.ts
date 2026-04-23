import {EventModality} from "./EventModality";
import {EventType} from "./EventType";

export interface EventCreateDTO {
  name: string;
  description?: string;
  startDate: string; // formato: yyyy-MM-dd
  endDate: string;   // formato: yyyy-MM-dd
  modality: EventModality;
  eventType: EventType;
  maxCapacity?: number;
  organizerId: number;
  address?: string;
}
