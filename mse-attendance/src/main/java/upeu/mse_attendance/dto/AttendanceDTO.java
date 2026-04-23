package upeu.mse_attendance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upeu.mse_attendance.enums.AttendanceStatus;
import upeu.mse_attendance.enums.CheckInMethod;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Long idAttendance;
    private LocalDateTime timestamp;
    private AttendanceStatus status;
    private CheckInMethod checkInMethod;
    private String observations;

    private AuthUserDTO authUserDTO;
    private EventDTO eventDTO;
    private ParticipantDTO participantDTO;
}
