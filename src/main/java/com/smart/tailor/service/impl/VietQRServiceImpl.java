package com.smart.tailor.service.impl;

import com.smart.tailor.service.VietQRSerivce;
import com.smart.tailor.utils.response.VietQRResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class VietQRServiceImpl implements VietQRSerivce {

    @Value("${VIET_QR_TAX_CODE_API_URL}")
    private String taxCodeAPIUrl;

    public Mono<VietQRResponse> getBusinessInfo(String taxCode) {
        WebClient client = WebClient.create();
        String requestUrl = taxCodeAPIUrl + taxCode;
        System.out.println(requestUrl);

        return client
                .get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(VietQRResponse.class)
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure())
                );
    }
}
