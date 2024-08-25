package com.smart.tailor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.service.PayOSDataService;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {
    private final PayOSDataService payOSDataService;
    private final Logger logger = LoggerFactory.getLogger(PayOSServiceImpl.class);
    @Value("${PAYOS_CREATE_PAYMENT_LINK_URL}")
    private String createPaymentLinkUrl;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;
    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;

    @Value("${BRAND_PAYOS_CLIENT_ID}")
    private String brandClientId;
    @Value("${BRAND_PAYOS_API_KEY}")
    private String brandApiKey;
    @Value("${BRAND_PAYOS_CHECKSUM_KEY}")
    private String brandChecksumKey;

    @Value("${REFUND_PAYOS_CLIENT_ID}")
    private String refundClientId;
    @Value("${REFUND_PAYOS_API_KEY}")
    private String refundApiKey;
    @Value("${REFUND_PAYOS_CHECKSUM_KEY}")
    private String refundChecksumKey;

    @Value("${CLIENT_URL}")
    private String clientURL;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static String convertObjToQueryStr(JsonNode object) {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        object.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            String valueAsString = value.isTextual() ? value.asText() : value.toString();

            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('&');
            }
            stringBuilder.append(key).append('=').append(valueAsString);
        });

        return stringBuilder.toString();
    }

    private static JsonNode sortObjDataByKey(JsonNode object) {
        if (!object.isObject()) {
            return object;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode orderedObject = objectMapper.createObjectNode();

        Iterator<Entry<String, JsonNode>> fieldsIterator = object.fields();
        TreeMap<String, JsonNode> sortedMap = new TreeMap<>();

        while (fieldsIterator.hasNext()) {
            Entry<String, JsonNode> field = fieldsIterator.next();
            sortedMap.put(field.getKey(), field.getValue());
        }

        sortedMap.forEach(orderedObject::set);

        return orderedObject;
    }

    private static String generateHmacSHA256(String dataStr, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));

        // Chuyển byte array sang chuỗi hex
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hmacBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }

    public static String createSignatureFromObj(JsonNode data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        JsonNode sortedDataByKey = sortObjDataByKey(data);
        String dataQueryStr = convertObjToQueryStr(sortedDataByKey);
        return generateHmacSHA256(dataQueryStr, key);
    }

    public static String createSignatureOfPaymentRequest(PayOSRequest data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        int amount = data.getAmount();
        String cancelUrl = data.getCancelUrl();
        String description = data.getDescription();
        int orderCode = data.getOrderCode();
        String returnUrl = data.getReturnUrl();
        String dataStr = "amount=" + amount + "&cancelUrl=" + cancelUrl + "&description=" + description
                + "&orderCode=" + orderCode + "&returnUrl=" + returnUrl;
        // Sử dụng HMAC-SHA-256 để tính toán chữ ký
        return generateHmacSHA256(dataStr, key);
    }

    @Override
    public PayOSCreationResponse createPaymentLink(PayOSRequest paymentRequest) throws Exception {
        try {
            String cancelUrl = "http://cancel";
            Integer amount = paymentRequest.getAmount();

            String currentTimeString = String.valueOf(LocalDateTime.now());
            Integer orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            String status = "PENDING";

            paymentRequest.setOrderCode(orderCode);
            String returnUrl = paymentRequest.getReturnUrl() != null ? paymentRequest.getReturnUrl() : clientURL;
            paymentRequest.setAmount(amount);
            paymentRequest.setReturnUrl(returnUrl);
            paymentRequest.setCancelUrl(cancelUrl);
            paymentRequest.setDescription(paymentRequest.getDescription());

            String bodyToSignature = createSignatureOfPaymentRequest(paymentRequest, checksumKey);
            paymentRequest.setSignature(bodyToSignature);

            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", clientId);
            headers.set("x-api-key", apiKey);
            // Gửi yêu cầu POST

            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(createPaymentLinkUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(paymentRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2)) // Retry indefinitely with exponential backoff
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()) // Throw error after exhaustion
                    )
                    .onErrorResume(error -> {
                        // Xử lý lỗi nếu cần
                        System.err.println("Error after retries: " + error.getMessage());
                        return Mono.empty();
                    });
            String responseBody = response.block();
            JsonNode res = objectMapper.readTree(responseBody);
            System.out.println("ORDER CODE: " + orderCode);
            System.out.println(res);
            if (!Objects.equals(res.get("code").asText(), "00")) {
                throw new Exception("Fail");
            }
            logger.info("Create PaymentLink Successfully");

            /**
             * GET RESPONSE
             */
            String code = res.get("code").asText();
            String desc = res.get("desc").asText();
            String signature = res.get("signature").asText();

            /**
             * GET RESPONSE.DATA
             */
            String bin = res.get("data").get("bin").asText();
            String accountNumber = res.get("data").get("accountNumber").asText();
            String accountName = res.get("data").get("accountName").asText();
            String description = res.get("data").get("description").asText();
            String currency = res.get("data").get("currency").asText();
            String paymentLinkId = res.get("data").get("paymentLinkId").asText();
            status = res.get("data").get("status").asText();
            String checkoutUrl = res.get("data").get("checkoutUrl").asText();
            String qrCode = res.get("data").get("qrCode").asText();

            //Kiểm tra dữ liệu có đúng không
            String paymentLinkResSignature = createSignatureFromObj(res.get("data"), checksumKey);
            if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
                throw new Exception("Signature is not compatible");
            }
            PayOSCreationResponseData responseData = PayOSCreationResponseData.builder()
                    .accountNumber(accountNumber)
                    .bin(bin)
                    .accountName(accountName)
                    .amount(amount)
                    .description(description)
                    .orderCode(orderCode)
                    .currency(currency)
                    .paymentLinkId(paymentLinkId)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();

            PayOSData payOSData = PayOSData
                    .builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();
            try {
                payOSDataService.save(payOSData);
            } catch (Exception ex) {
                logger.error("SOMETHING WENT WRONG!!!!!");
                ex.printStackTrace();
            }
            PayOSCreationResponse payOSCreationResponse = PayOSCreationResponse
                    .builder()
                    .code(code)
                    .desc(desc)
                    .data(responseData)
                    .signature(signature)
                    .build();
            return payOSCreationResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PayOSCreationResponse createBrandPaymentLink(PayOSRequest paymentRequest) throws Exception {
        try {
            String cancelUrl = "http://cancel";
            Integer amount = paymentRequest.getAmount();

            String currentTimeString = String.valueOf(LocalDateTime.now());
            Integer orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            String status = "PENDING";

            paymentRequest.setOrderCode(orderCode);
            String returnUrl = paymentRequest.getReturnUrl() != null ? paymentRequest.getReturnUrl() : clientURL;
            paymentRequest.setAmount(amount);
            paymentRequest.setReturnUrl(returnUrl);
            paymentRequest.setCancelUrl(cancelUrl);
            paymentRequest.setDescription(paymentRequest.getDescription());

            String bodyToSignature = createSignatureOfPaymentRequest(paymentRequest, brandChecksumKey);
            paymentRequest.setSignature(bodyToSignature);

            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", brandClientId);
            headers.set("x-api-key", brandApiKey);
            // Gửi yêu cầu POST

            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(createPaymentLinkUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(paymentRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2)) // Retry indefinitely with exponential backoff
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()) // Throw error after exhaustion
                    )
                    .onErrorResume(error -> {
                        // Xử lý lỗi nếu cần
                        System.err.println("Error after retries: " + error.getMessage());
                        return Mono.empty();
                    });
            String responseBody = response.block();
            JsonNode res = objectMapper.readTree(responseBody);
            System.out.println("ORDER CODE: " + orderCode);
            System.out.println(res);
            if (!Objects.equals(res.get("code").asText(), "00")) {
                throw new Exception("Fail");
            }
            logger.info("Create PaymentLink Successfully");

            /**
             * GET RESPONSE
             */
            String code = res.get("code").asText();
            String desc = res.get("desc").asText();
            String signature = res.get("signature").asText();

            /**
             * GET RESPONSE.DATA
             */
            String bin = res.get("data").get("bin").asText();
            String accountNumber = res.get("data").get("accountNumber").asText();
            String accountName = res.get("data").get("accountName").asText();
            String description = res.get("data").get("description").asText();
            String currency = res.get("data").get("currency").asText();
            String paymentLinkId = res.get("data").get("paymentLinkId").asText();
            status = res.get("data").get("status").asText();
            String checkoutUrl = res.get("data").get("checkoutUrl").asText();
            String qrCode = res.get("data").get("qrCode").asText();

            //Kiểm tra dữ liệu có đúng không
            String paymentLinkResSignature = createSignatureFromObj(res.get("data"), brandChecksumKey);
            if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
                throw new Exception("Signature is not compatible");
            }
            PayOSCreationResponseData responseData = PayOSCreationResponseData.builder()
                    .accountNumber(accountNumber)
                    .bin(bin)
                    .accountName(accountName)
                    .amount(amount)
                    .description(description)
                    .orderCode(orderCode)
                    .currency(currency)
                    .paymentLinkId(paymentLinkId)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();

            PayOSData payOSData = PayOSData
                    .builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();
            try {
                payOSDataService.save(payOSData);
            } catch (Exception ex) {
                logger.error("SOMETHING WENT WRONG!!!!!");
                ex.printStackTrace();
            }
            PayOSCreationResponse payOSCreationResponse = PayOSCreationResponse
                    .builder()
                    .code(code)
                    .desc(desc)
                    .data(responseData)
                    .signature(signature)
                    .build();
            return payOSCreationResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PayOSCreationResponse createRefundPaymentLink(PayOSRequest paymentRequest) throws Exception {
        try {
            String cancelUrl = "http://cancel";
            Integer amount = paymentRequest.getAmount();

            String currentTimeString = String.valueOf(LocalDateTime.now());
            Integer orderCode = Integer.parseInt(currentTimeString.substring(currentTimeString.length() - 6));

            String status = "PENDING";

            paymentRequest.setOrderCode(orderCode);
            String returnUrl = paymentRequest.getReturnUrl() != null ? paymentRequest.getReturnUrl() : clientURL;
            paymentRequest.setAmount(amount);
            paymentRequest.setReturnUrl(returnUrl);
            paymentRequest.setCancelUrl(cancelUrl);
            paymentRequest.setDescription(paymentRequest.getDescription());

            String bodyToSignature = createSignatureOfPaymentRequest(paymentRequest, refundChecksumKey);
            paymentRequest.setSignature(bodyToSignature);

            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-client-id", refundClientId);
            headers.set("x-api-key", refundApiKey);
            // Gửi yêu cầu POST

            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(createPaymentLinkUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(paymentRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2)) // Retry indefinitely with exponential backoff
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()) // Throw error after exhaustion
                    )
                    .onErrorResume(error -> {
                        // Xử lý lỗi nếu cần
                        System.err.println("Error after retries: " + error.getMessage());
                        return Mono.empty();
                    });
            String responseBody = response.block();
            JsonNode res = objectMapper.readTree(responseBody);
            System.out.println("ORDER CODE: " + orderCode);
            System.out.println(res);
            if (!Objects.equals(res.get("code").asText(), "00")) {
                throw new Exception("Fail");
            }
            logger.info("Create PaymentLink Successfully");

            /**
             * GET RESPONSE
             */
            String code = res.get("code").asText();
            String desc = res.get("desc").asText();
            String signature = res.get("signature").asText();

            /**
             * GET RESPONSE.DATA
             */
            String bin = res.get("data").get("bin").asText();
            String accountNumber = res.get("data").get("accountNumber").asText();
            String accountName = res.get("data").get("accountName").asText();
            String description = res.get("data").get("description").asText();
            String currency = res.get("data").get("currency").asText();
            String paymentLinkId = res.get("data").get("paymentLinkId").asText();
            status = res.get("data").get("status").asText();
            String checkoutUrl = res.get("data").get("checkoutUrl").asText();
            String qrCode = res.get("data").get("qrCode").asText();

            //Kiểm tra dữ liệu có đúng không
            String paymentLinkResSignature = createSignatureFromObj(res.get("data"), refundChecksumKey);
            if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
                throw new Exception("Signature is not compatible");
            }
            PayOSCreationResponseData responseData = PayOSCreationResponseData.builder()
                    .accountNumber(accountNumber)
                    .bin(bin)
                    .accountName(accountName)
                    .amount(amount)
                    .description(description)
                    .orderCode(orderCode)
                    .currency(currency)
                    .paymentLinkId(paymentLinkId)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();

            PayOSData payOSData = PayOSData
                    .builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .status(status)
                    .checkoutUrl(checkoutUrl)
                    .qrCode(qrCode)
                    .build();
            try {
                payOSDataService.save(payOSData);
            } catch (Exception ex) {
                logger.error("SOMETHING WENT WRONG!!!!!");
                ex.printStackTrace();
            }
            PayOSCreationResponse payOSCreationResponse = PayOSCreationResponse
                    .builder()
                    .code(code)
                    .desc(desc)
                    .data(responseData)
                    .signature(signature)
                    .build();
            return payOSCreationResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PayOSResponse getPaymentInfo(Integer paymentID) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.get()
                .uri(createPaymentLinkUrl + "/" + paymentID)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))  // Thử lại với độ trễ gia tăng
                .onErrorResume(e -> {
                    logger.error("Request failed", e);
                    return Mono.empty();
                });
        String responseBody = response.block();
        JsonNode res = null;
        try {
            res = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!Objects.equals(res.get("code").asText(), "00")) {
            logger.error("GET PAYOS RESPONSE FAIL!");
            return null;
        }

        String code = res.get("code").asText();
        String desc = res.get("desc").asText();

        String id = res.get("data").get("id").asText();
        Integer orderCode = Integer.valueOf(res.get("data").get("orderCode").asText()); //Mã đơn hàng từ cửa hàng
        Integer amount = Integer.valueOf(res.get("data").get("amount").asText());
        Integer amountPaid = Integer.valueOf(res.get("data").get("amountPaid").asText());
        Integer amountRemaining = Integer.valueOf(res.get("data").get("amountRemaining").asText());
        String status = res.get("data").get("status").asText();
        List<Transactions> transactions = new ArrayList<>();
        JsonNode jsonArrayNode = res.get("data").get("transactions");
        for (JsonNode jsonNode : jsonArrayNode) {
            Transactions transactionsObject = objectMapper.convertValue(jsonNode, Transactions.class);
            transactions.add(transactionsObject);
        }
        String createdAt = res.get("data").get("createdAt").asText();
        String canceledAt = res.get("data").get("canceledAt").asText();
        String cancellationReason = res.get("data").get("cancellationReason").asText();

        PayOSResponseData data = PayOSResponseData
                .builder()
                .id(id)
                .orderCode(orderCode)
                .amount(amount)
                .amountPaid(amountPaid)
                .amountRemaining(amountRemaining)
                .status(status)
                .transactions(transactions)
                .createdAt(createdAt)
                .canceledAt(canceledAt)
                .cancellationReason(cancellationReason)
                .build();

        String signature = res.get("signature").asText();

        var checkPayOSEntity = payOSDataService.findByOrderCode(orderCode);
        if (checkPayOSEntity.isPresent()) {
            var payOSEntity = checkPayOSEntity.get();
            payOSEntity.setStatus(status);
            payOSDataService.save(payOSEntity);
            data.setQrCode(payOSEntity.getQrCode());
            data.setCheckoutUrl(payOSEntity.getCheckoutUrl());
        }

        PayOSResponse payOSResponse = PayOSResponse
                .builder()
                .code(code)
                .data(data)
                .signature(signature)
                .desc(desc)
                .build();
        return payOSResponse;
    }

    @Override
    public PayOSResponse getBrandPaymentInfo(Integer paymentID) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", brandClientId);
        headers.set("x-api-key", brandApiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.get()
                .uri(createPaymentLinkUrl + "/" + paymentID)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))  // Thử lại với độ trễ gia tăng
                .onErrorResume(e -> {
                    logger.error("Request failed", e);
                    return Mono.empty();
                });
        String responseBody = response.block();
        JsonNode res = null;
        try {
            res = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!Objects.equals(res.get("code").asText(), "00")) {
            logger.error("GET PAYOS RESPONSE FAIL!");
            return null;
        }

        String code = res.get("code").asText();
        String desc = res.get("desc").asText();

        String id = res.get("data").get("id").asText();
        Integer orderCode = Integer.valueOf(res.get("data").get("orderCode").asText()); //Mã đơn hàng từ cửa hàng
        Integer amount = Integer.valueOf(res.get("data").get("amount").asText());
        Integer amountPaid = Integer.valueOf(res.get("data").get("amountPaid").asText());
        Integer amountRemaining = Integer.valueOf(res.get("data").get("amountRemaining").asText());
        String status = res.get("data").get("status").asText();
        List<Transactions> transactions = new ArrayList<>();
        JsonNode jsonArrayNode = res.get("data").get("transactions");
        for (JsonNode jsonNode : jsonArrayNode) {
            Transactions transactionsObject = objectMapper.convertValue(jsonNode, Transactions.class);
            transactions.add(transactionsObject);
        }
        String createdAt = res.get("data").get("createdAt").asText();
        String canceledAt = res.get("data").get("canceledAt").asText();
        String cancellationReason = res.get("data").get("cancellationReason").asText();

        PayOSResponseData data = PayOSResponseData
                .builder()
                .id(id)
                .orderCode(orderCode)
                .amount(amount)
                .amountPaid(amountPaid)
                .amountRemaining(amountRemaining)
                .status(status)
                .transactions(transactions)
                .createdAt(createdAt)
                .canceledAt(canceledAt)
                .cancellationReason(cancellationReason)
                .build();

        String signature = res.get("signature").asText();

        var checkPayOSEntity = payOSDataService.findByOrderCode(orderCode);
        if (checkPayOSEntity.isPresent()) {
            var payOSEntity = checkPayOSEntity.get();
            payOSEntity.setStatus(status);
            payOSDataService.save(payOSEntity);
            data.setQrCode(payOSEntity.getQrCode());
            data.setCheckoutUrl(payOSEntity.getCheckoutUrl());
        }

        PayOSResponse payOSResponse = PayOSResponse
                .builder()
                .code(code)
                .data(data)
                .signature(signature)
                .desc(desc)
                .build();
        return payOSResponse;
    }

    @Override
    public PayOSResponse getRefundPaymentInfo(Integer paymentID) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", refundClientId);
        headers.set("x-api-key", refundApiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.get()
                .uri(createPaymentLinkUrl + "/" + paymentID)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))  // Thử lại với độ trễ gia tăng
                .onErrorResume(e -> {
                    logger.error("Request failed", e);
                    return Mono.empty();
                });
        String responseBody = response.block();
        JsonNode res = null;
        try {
            res = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!Objects.equals(res.get("code").asText(), "00")) {
            logger.error("GET PAYOS RESPONSE FAIL!");
            return null;
        }

        String code = res.get("code").asText();
        String desc = res.get("desc").asText();

        String id = res.get("data").get("id").asText();
        Integer orderCode = Integer.valueOf(res.get("data").get("orderCode").asText()); //Mã đơn hàng từ cửa hàng
        Integer amount = Integer.valueOf(res.get("data").get("amount").asText());
        Integer amountPaid = Integer.valueOf(res.get("data").get("amountPaid").asText());
        Integer amountRemaining = Integer.valueOf(res.get("data").get("amountRemaining").asText());
        String status = res.get("data").get("status").asText();
        List<Transactions> transactions = new ArrayList<>();
        JsonNode jsonArrayNode = res.get("data").get("transactions");
        for (JsonNode jsonNode : jsonArrayNode) {
            Transactions transactionsObject = objectMapper.convertValue(jsonNode, Transactions.class);
            transactions.add(transactionsObject);
        }
        String createdAt = res.get("data").get("createdAt").asText();
        String canceledAt = res.get("data").get("canceledAt").asText();
        String cancellationReason = res.get("data").get("cancellationReason").asText();

        PayOSResponseData data = PayOSResponseData
                .builder()
                .id(id)
                .orderCode(orderCode)
                .amount(amount)
                .amountPaid(amountPaid)
                .amountRemaining(amountRemaining)
                .status(status)
                .transactions(transactions)
                .createdAt(createdAt)
                .canceledAt(canceledAt)
                .cancellationReason(cancellationReason)
                .build();

        String signature = res.get("signature").asText();

        var checkPayOSEntity = payOSDataService.findByOrderCode(orderCode);
        if (checkPayOSEntity.isPresent()) {
            var payOSEntity = checkPayOSEntity.get();
            payOSEntity.setStatus(status);
            payOSDataService.save(payOSEntity);
            data.setQrCode(payOSEntity.getQrCode());
            data.setCheckoutUrl(payOSEntity.getCheckoutUrl());
        }

        PayOSResponse payOSResponse = PayOSResponse
                .builder()
                .code(code)
                .data(data)
                .signature(signature)
                .desc(desc)
                .build();
        return payOSResponse;
    }

    @Override
    public void confirmPayment(Integer orderCode) throws JsonProcessingException {
        var onlinePayOS = getPaymentInfo(orderCode).getData();
        var payOSData = payOSDataService.findByOrderCode(orderCode).get();
        payOSData.setStatus(onlinePayOS.getStatus());
        payOSDataService.save(payOSData);
    }
}