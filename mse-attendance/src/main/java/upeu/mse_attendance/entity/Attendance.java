package upeu.mse_attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import upeu.mse_attendance.enums.AttendanceStatus;
import upeu.mse_attendance.enums.CheckInMethod;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAttendance;

    @Column(name = "auth_user_id", nullable = false)
    private int authUserId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    // Momento en que se registró la asistencia
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_method", length = 50)
    private CheckInMethod checkInMethod;

    // Comentarios u observaciones opcionales
    @Column(length = 255)
    private String observations;

}
