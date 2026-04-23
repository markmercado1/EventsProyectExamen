import {EventModality} from "./EventModality";
import {EventType} from "./EventType";
import {EventStatus} from "./EventStatus";

export interface EventUpdateDTO {
  name?: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  modality?: EventModality;
  eventType?: EventType;
  maxCapacity?: number;
  address?: string;
  status?: EventStatus;
}
