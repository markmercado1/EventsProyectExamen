package com.emm.msregistrations.feign;

import com.emm.msregistrations.dtos.ParticipantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-participants-service", path = "/participants")
public interface ParticipantFeign {

    @GetMapping("/{id}")
    ParticipantDTO getParticipantById(@PathVariable("id") Long id);
}