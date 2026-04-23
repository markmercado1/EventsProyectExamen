package com.emm.mspayment.feign;



import com.emm.mspayment.dto.RegistrationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-registration-service", path = "/registrations")
public interface RegistrationFeignClient {

    @GetMapping("/{id}")
    RegistrationResponseDTO getRegistrationById(@PathVariable("id") Long id);
}