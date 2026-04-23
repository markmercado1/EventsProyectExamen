package com.emm.msregistrations.feign;


import com.emm.msregistrations.dtos.PaymentOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-payment-service", path = "/payment-orders")
public interface PaymentFeign {
    @GetMapping("/{id}")
    PaymentOrderDTO getPaymentOrderById(@PathVariable("id") Long id);
}
