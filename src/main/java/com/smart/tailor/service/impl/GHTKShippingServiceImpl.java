package com.smart.tailor.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tailor.service.GHTKShippingService;
import com.smart.tailor.utils.request.OrderShippingRequest;
import com.smart.tailor.utils.response.FeeResponse;
import com.smart.tailor.utils.response.OrderDetailShippingResponse;
import com.smart.tailor.utils.response.OrderShippingResponse;
import com.smart.tailor.utils.response.VietQRResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GHTKShippingServiceImpl implements GHTKShippingService {
    @Value("${GHTK_SHIPPING_API_URL}")
    private String baseShippingApiUrl;

    @Value("${GHTK_SHPPING_API_TOKEN_KEY}")
    private String shippingApiTokenKey;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(GHTKShippingServiceImpl.class);

    @Override
    public OrderShippingResponse createShippingOrder(OrderShippingRequest orderShippingRequest) {
        try {
            String shippingApiUrl = baseShippingApiUrl +  "/order/?ver=1.5";

            logger.info("Shipping API Token {}", shippingApiTokenKey);
            logger.info("Shipping API Url {}", shippingApiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", shippingApiTokenKey);

            WebClient client = WebClient.create();
            Mono<String> response = client
                    .post()
                    .uri(shippingApiUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(orderShippingRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // Retry up to 3 times with exponential backoff
                    .onErrorResume(error -> {
                        logger.error("Error after retries: {}", error.getMessage());
                        return Mono.empty();
                    });

            String responseBody = response.block();
            if (responseBody == null) {
                throw new Exception("Failed to get response from the service");
            }

            JsonNode res = objectMapper.readTree(responseBody);

            logger.info("Response Body From Request URL {}", responseBody);
            if (!Objects.equals(res.get("success").asText(), "true")) {
                throw new Exception("Fail");
            }

            Boolean success = Boolean.parseBoolean(res.get("success").asText());
            String message = res.get("message").asText();
            String partner_id = res.get("order").get("partner_id").asText();
            String label = res.get("order").get("label").asText();
            Integer fee = Integer.parseInt(res.get("order").get("fee").asText());
            String estimated_pick_time = res.get("order").get("estimated_pick_time").asText();
            String estimated_deliver_time = res.get("order").get("estimated_deliver_time").asText();
            Integer status_id =  Integer.parseInt(res.get("order").get("status_id").asText());
            String warning_message = res.get("warning_message").asText();

            return OrderShippingResponse.builder()
                    .success(success)
                    .message(message)
                    .partner_id(partner_id)
                    .label(label)
                    .fee(fee)
                    .estimated_pick_time(estimated_pick_time)
                    .estimated_deliver_time(estimated_deliver_time)
                    .status_id(status_id)
                    .warning_message(warning_message)
                    .build();

        } catch (Exception ex) {
            logger.error("Exception occurred: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public FeeResponse calculateShippingFee(OrderShippingRequest orderShippingRequest) {
        try {
            String shippingApiUrl = baseShippingApiUrl + "/fee";

            // Build the URI with parameters
            shippingApiUrl = UriComponentsBuilder
                    .fromHttpUrl(shippingApiUrl)
                    .queryParam("pick_address", orderShippingRequest.getOrder().getPick_address())
                    .queryParam("pick_province", orderShippingRequest.getOrder().getProvince())
                    .queryParam("pick_district", orderShippingRequest.getOrder().getPick_district())
                    .queryParam("pick_ward", orderShippingRequest.getOrder().getPick_ward())
                    .queryParam("address", orderShippingRequest.getOrder().getAddress())
                    .queryParam("province", orderShippingRequest.getOrder().getProvince())
                    .queryParam("district", orderShippingRequest.getOrder().getDistrict())
                    .queryParam("ward", orderShippingRequest.getOrder().getWard())
                    .queryParam("weight", orderShippingRequest.getOrder().getWeight() * 1000)
                    .build()
                    .toUriString();

            logger.info("Shipping API Token {}", shippingApiTokenKey);
            logger.info("Shipping API Url {}", shippingApiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", shippingApiTokenKey);

            WebClient client = WebClient.create();
            Mono<String> response = client
                    .get()
                    .uri(shippingApiUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // Retry up to 3 times with exponential backoff
                    .onErrorResume(error -> {
                        logger.error("Error after retries: {}", error.getMessage());
                        return Mono.empty();
                    });

            String responseBody = response.block();
            if (responseBody == null) {
                throw new Exception("Failed to get response from the service");
            }

            JsonNode res = objectMapper.readTree(responseBody);

            logger.info("Response Body From Request URL {}", responseBody);
            if (!Objects.equals(res.get("success").asText(), "true")) {
                throw new Exception("Fail");
            }

            return FeeResponse
                    .builder()
                    .success(Boolean.parseBoolean(res.get("success").asText()))
                    .fee(Integer.parseInt(res.get("fee").get("fee").asText()))
                    .message(res.get("message").asText())
                    .build();

        } catch (Exception ex) {
            logger.error("Exception occurred: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public OrderDetailShippingResponse getOrderDetailShippingResponseByLabelID(String labelID) {
        try {
            String shippingApiUrl = baseShippingApiUrl + "/v2/" + labelID;

            logger.info("Shipping API Token {}", shippingApiTokenKey);
            logger.info("Shipping API Url {}", shippingApiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", shippingApiTokenKey);

            WebClient client = WebClient.create();
            Mono<String> response = client
                    .get()
                    .uri(shippingApiUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // Retry up to 3 times with exponential backoff
                    .onErrorResume(error -> {
                        logger.error("Error after retries: {}", error.getMessage());
                        return Mono.empty();
                    });

            String responseBody = response.block();
            if (responseBody == null) {
                throw new Exception("Failed to get response from the service");
            }

            JsonNode res = objectMapper.readTree(responseBody);

            logger.info("Response Body From Request URL {}", responseBody);
            if (!Objects.equals(res.get("success").asText(), "true")) {
                return OrderDetailShippingResponse
                        .builder()
                        .success(Boolean.parseBoolean(res.get("success").asText()))
                        .message(res.get("message").asText())
                        .build();
            }

            Boolean success = Boolean.parseBoolean(res.get("success").asText());
            String message = res.get("message").asText();
            String partner_id = res.get("order").get("partner_id").asText();
            String label_id = res.get("order").get("label_id").asText();
            Integer status = Integer.parseInt(res.get("order").get("status").asText());
            String status_text = res.get("order").get("status_text").asText();
            String created = res.get("order").get("created").asText();
            String modified = res.get("order").get("modified").asText();
            String pick_date = res.get("order").get("pick_date").asText();
            String deliver_date = res.get("order").get("deliver_date").asText();
            String customer_fullName = res.get("order").get("customer_fullname").asText();
            String customer_tel = res.get("order").get("customer_tel").asText();
            String address = res.get("order").get("address").asText();
            Integer ship_money =  Integer.parseInt(res.get("order").get("ship_money").asText());
            Integer weight =  Integer.parseInt(res.get("order").get("weight").asText());

            return OrderDetailShippingResponse
                    .builder()
                    .success(success)
                    .message(message)
                    .label_id(label_id)
                    .partner_id(partner_id)
                    .status(status)
                    .status_text(status_text)
                    .created(created)
                    .modified(modified)
                    .pick_date(pick_date)
                    .deliver_date(deliver_date)
                    .customer_fullName(customer_fullName)
                    .customer_tel(customer_tel)
                    .address(address)
                    .ship_money(ship_money)
                    .weight(weight)
                    .build();

        } catch (Exception ex) {
            logger.error("Exception occurred: {}", ex.getMessage());
            return null;
        }
    }
}
