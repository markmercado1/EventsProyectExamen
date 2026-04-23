package com.emm.msregistrations.controller;



import com.emm.msregistrations.dtos.CreateRegistrationDTO;
import com.emm.msregistrations.dtos.RegistrationResponseDTO;
import com.emm.msregistrations.dtos.UpdateRegistrationDTO;
import com.emm.msregistrations.enums.RegistrationStatus;
import com.emm.msregistrations.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponseDTO> createRegistration(
            @Valid @RequestBody CreateRegistrationDTO dto) {
        RegistrationResponseDTO response = registrationService.createRegistration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationResponseDTO> updateRegistration(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRegistrationDTO dto) {
        RegistrationResponseDTO response = registrationService.updateRegistration(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationResponseDTO> getRegistrationById(@PathVariable Long id) {
        RegistrationResponseDTO response = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponseDTO>> getAllRegistrations() {
        List<RegistrationResponseDTO> response = registrationService.getAllRegistrations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<RegistrationResponseDTO>> getRegistrationsByEventId(
            @PathVariable Long eventId) {
        List<RegistrationResponseDTO> response = registrationService.getRegistrationsByEventId(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<RegistrationResponseDTO>> getRegistrationsByParticipantId(
            @PathVariable Long participantId) {
        List<RegistrationResponseDTO> response = registrationService.getRegistrationsByParticipantId(participantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RegistrationResponseDTO>> getRegistrationsByStatus(
            @PathVariable RegistrationStatus status) {
        List<RegistrationResponseDTO> response = registrationService.getRegistrationsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/generate-qr")
    public ResponseEntity<Map<String, String>> generateQRCode(@PathVariable Long id) {
        String qrCode = registrationService.generateQRCode(id);
        return ResponseEntity.ok(Map.of("qrCode", qrCode));
    }
}