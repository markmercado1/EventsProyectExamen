import {EventModality} from "./EventModality";
import {EventStatus} from "./EventStatus";

export interface EventSummaryDTO {
  eventId: number;
  name: string;
  startDate: string;
  endDate: string;
  modality: EventModality;
  status: EventStatus;
}
