package com.emm.msregistrations.feign;

import com.emm.msregistrations.dtos.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-events-service", path = "/events")
public interface EventFeign {

    @GetMapping("/{id}")
    EventDTO getEventById(@PathVariable("id") Long id);
}