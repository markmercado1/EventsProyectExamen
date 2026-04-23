package upeu.mse_attendance.service;

import upeu.mse_attendance.dto.AttendanceDTO;
import upeu.mse_attendance.dto.AttendanceGroupDTO;
import upeu.mse_attendance.entity.Attendance;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    // Listar todas las asistencias
    List<AttendanceDTO> listarAsistencias();

    // Obtener una asistencia por su ID
    Optional<AttendanceDTO> obtenerAsistenciaPorId(Long idAttendance);

    // Registrar una nueva asistencia
    AttendanceDTO registrarAsistencia(AttendanceDTO attendanceDTO);

    // Actualizar una asistencia existente
    AttendanceDTO actualizarAsistencia(Long idAttendance, AttendanceDTO attendanceDTO);

    // Eliminar una asistencia por ID
    void eliminarAsistencia(Long idAttendance);


    List<AttendanceDTO> registrarAsistenciaGrupo(AttendanceGroupDTO attendanceGroupDTO);
}
