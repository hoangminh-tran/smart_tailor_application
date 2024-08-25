package com.smart.tailor.controller;

import com.smart.tailor.service.VietQRSerivce;
import com.smart.tailor.utils.response.VietQRResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class VietQRController {
    private final VietQRSerivce vietQRService;

    @GetMapping("/api/vietqr/business/{taxCode}")
    public Mono<VietQRResponse> getBusinessInfo(@PathVariable String taxCode) {
        return vietQRService.getBusinessInfo(taxCode);
    }
}
