package upeu.mse_attendance.enums;

public enum CheckInMethod {
    QR,            // Escaneo de código QR
    MANUAL,        // Marcado manual por un operador
    NFC,           // Mediante tarjeta NFC o RFID
    GEOLOCATION,   // Validación por ubicación GPS
    IMPORTED,      // Asistencia cargada desde archivo o integración
    FACIAL         // Reconocimiento facial
}