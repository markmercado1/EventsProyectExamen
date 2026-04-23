package upeu.mse_attendance.feign;

import upeu.mse_attendance.dto.EventDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="mse-event", path = "/events", contextId = "eventFeignClient")
public interface EventFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "eventoListarPorIdCB", fallbackMethod = "fallbackEvent")
    EventDTO buscarPorId(@PathVariable Long id);

    default EventDTO fallbackEvent(Long id, Exception e) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setIdEvento(id);
        eventDTO.setName("Evento no disponible");
        eventDTO.setDescription("No se pudo recuperar la información del evento");
        // Opcionalmente puedes inicializar otros campos mínimos
        return eventDTO;
    }
}


//package upeu.mse_attendance.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import upeu.mse_attendance.dto.EventDTO;
//
//@FeignClient(name="ms-events-service", path = "/events")
//public interface EventFeign {
//    @GetMapping("/{id}")
//    public EventDTO buscarPorId(@PathVariable Long id);
//}
