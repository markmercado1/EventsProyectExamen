package upeu.mse_attendance.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import upeu.mse_attendance.dto.ParticipantDTO;

@FeignClient(name="ms-participants-service", path = "/participants")
public interface ParticipantFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "participanteListarPorIdCB", fallbackMethod = "fallbackParticipant")
    ParticipantDTO buscarPorId(@PathVariable Long id);

    default ParticipantDTO fallbackParticipant(Long id, Exception e) {
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setIdParticipant(id); // Mantener el id solicitado
        participantDTO.setFirstName("Participante no disponible");
        participantDTO.setLastName("");
        participantDTO.setEmail("");
        participantDTO.setPhone("");
        participantDTO.setRegistrationDate(null);
        return participantDTO;
    }
}


//package upeu.mse_attendance.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import upeu.mse_attendance.dto.ParticipantDTO;
//
//@FeignClient(name="ms-participants-service", path = "/participants")
//public interface ParticipantFeign {
//    @GetMapping("/{id}")
//    public ParticipantDTO buscarPorId(@PathVariable Long id);
//}
