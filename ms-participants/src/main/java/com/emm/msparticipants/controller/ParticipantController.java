package com.emm.msparticipants.controller;


import com.emm.msparticipants.dtos.CreateParticipantDTO;
import com.emm.msparticipants.dtos.ParticipantDTO;
import com.emm.msparticipants.services.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody CreateParticipantDTO dto) {
        ParticipantDTO created = participantService.createParticipant(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        ParticipantDTO participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDTO> updateParticipant(
            @PathVariable Long id,
            @RequestBody CreateParticipantDTO dto) {
        ParticipantDTO updated = participantService.updateParticipant(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
}
