package upeu.mse_attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upeu.mse_attendance.enums.AttendanceStatus;
import upeu.mse_attendance.enums.CheckInMethod;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceGroupDTO {
    private AuthUserDTO authUserDTO;
    private EventDTO eventDTO;
    private List<ParticipantDTO> participantDTOs;
    private AttendanceStatus status;
    private CheckInMethod checkInMethod;
    private String observations;
}
