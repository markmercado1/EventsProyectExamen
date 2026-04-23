package upeu.mse_attendance.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import upeu.mse_attendance.dto.AuthUserDTO;

@FeignClient(name="ms-auth-service", path = "/auth")
public interface AuthUserFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "authListarPorIdCB", fallbackMethod = "fallbackAuthUser")
    AuthUserDTO buscarPorId(@PathVariable int id);

    default AuthUserDTO fallbackAuthUser(int id, Exception e) {
        AuthUserDTO userDTO = new AuthUserDTO();
        userDTO.setId(id);  // Mantener el mismo id solicitado
        userDTO.setUserName("Usuario no disponible"); // Mensaje claro
        return userDTO;
    }
}


//package upeu.mse_attendance.feign;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import upeu.mse_attendance.dto.AuthUserDTO;
//
//@FeignClient(name="ms-auth-service", path = "/auth")
//public interface AuthUserFeign {
//    @GetMapping("/{id}")
//    public AuthUserDTO buscarPorId(@PathVariable int id);
//}