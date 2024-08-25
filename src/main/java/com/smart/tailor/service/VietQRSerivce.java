package com.smart.tailor.service;

import com.smart.tailor.utils.response.VietQRResponse;
import reactor.core.publisher.Mono;

public interface VietQRSerivce {
    Mono<VietQRResponse> getBusinessInfo(String taxCode);
}
