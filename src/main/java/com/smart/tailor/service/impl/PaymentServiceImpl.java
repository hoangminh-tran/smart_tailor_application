package com.smart.tailor.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tailor.config.VNPayConfig;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Order;
import com.smart.tailor.entities.Payment;
import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.PaymentMapper;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.repository.PaymentRepository;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.PayOSItem;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.request.PaymentRequest;
import com.smart.tailor.utils.response.GrowthPercentageResponse;
import com.smart.tailor.utils.response.PayOSCreationResponse;
import com.smart.tailor.utils.response.PaymentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PayOSService payOSService;
    private final UserService userService;
    private final PaymentMapper paymentMapper;
    private final SystemPropertiesService systemPropertiesService;
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${CLIENT_URL}")
    private String clientURL;

    @Value("${VNPAY_URL}")
    private String vnp_PayUrl;

    @Value("${VNPAY_CHECKSUM_KEY}")
    private String vnp_HashSecret;

    @Value("${VNPAY_TMN_CODE}")
    private String vnp_TmnCode;

    @Value("${VNPAY_API_URL}")
    private String vnp_apiUrl;

    @Override
    public PaymentResponse createPayOSPayment(PaymentRequest paymentRequest) throws Exception {
        try {
            /**
             * TODO
             * Validate payment request
             */
            String paymentSenderID = paymentRequest.getPaymentSenderID();
            String paymentSenderName = paymentRequest.getPaymentSenderName();
            String paymentSenderBankCode = paymentRequest.getPaymentSenderBankCode();
            String paymentSenderBankNumber = paymentRequest.getPaymentSenderBankNumber();

            String paymentRecipientID = paymentRequest.getPaymentRecipientID();
            String paymentRecipientName = paymentRequest.getPaymentRecipientName();
            String paymentRecipientBankCode = paymentRequest.getPaymentRecipientBankCode();
            String paymentRecipientBankNumber = paymentRequest.getPaymentRecipientBankNumber();

//            Integer paymentAmount = paymentRequest.getPaymentAmount();
            Integer paymentAmount = 10000;
            PaymentMethod paymentMethod = paymentRequest.getPaymentMethod();
            Boolean paymentStatus = false;
            PaymentType paymentType = paymentRequest.getPaymentType();

            if (paymentType.equals(PaymentType.CUSTOMER_UPGRADE) || paymentType.equals(PaymentType.BRAND_REGISTRATION)) {
                var checkSender = userService.getUserByUserID(paymentSenderID);
                if (checkSender.isEmpty()) {
                    throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
                }
                var sender = checkSender.get();

                var checkRecipient = userService.getUserByUserID(paymentRecipientID);
                if (checkRecipient.isEmpty()) {
                    throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
                }
                var recipient = checkRecipient.get();

                String senderEmail = sender.getEmail();
                String senderPhone = sender.getPhoneNumber();
                String senderAddress = "";
                if (sender.getRoles().getRoleName().equals("BRAND")) {
                    var brand = sender.getBrand();
                    senderAddress = brand.getProvince() + " " + brand.getDistrict() + " " + brand.getWard() + " " + brand.getAddress();
                } else if (sender.getRoles().getRoleName().equals("CUSTOMER")) {
                    var customer = sender.getCustomer();
                    senderAddress = customer.getProvince() + " " + customer.getDistrict() + " " + customer.getWard() + " " + customer.getAddress();
                }
                var storedPayment = paymentRepository.save(
                        Payment.builder()
                                .paymentSender(null)
                                .paymentSenderName("")
                                .paymentSenderBankCode("")
                                .paymentSenderBankNumber("")

                                .paymentRecipient(null)
                                .paymentRecipientName("")
                                .paymentRecipientBankCode("")
                                .paymentRecipientBankNumber("")

                                .paymentMethod(paymentMethod)
                                .paymentAmount(paymentAmount)
                                .paymentStatus(paymentStatus)
                                .paymentType(paymentType)
//                                .order(paymentRequest.getOrder())
                                .order(null)
                                .paymentCode(null)
                                .build()
                );
                return paymentMapper.mapperToPaymentResponse(storedPayment);
            } else {

                /**
                 * TODO
                 * Load item from paymentRequest to itemList
                 */
                List<PayOSItem> itemList = paymentRequest.getItemList();
                String cancelUrl = "";
                String returnUrl = "";
                String description = "";
                var orderID = paymentRequest.getOrderID();
                PayOSCreationResponse creationPayOS = null;
                if (paymentType.equals(PaymentType.BRAND_INVOICE)) {
                    var checkSender = userService.getUserByUserID(paymentSenderID);
                    if (checkSender.isEmpty()) {
                        throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
                    }
                    var sender = checkSender.get();

                    var checkRecipient = userService.getUserByUserID(paymentRecipientID);
                    if (checkRecipient.isEmpty()) {
                        throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
                    }
                    var recipient = checkRecipient.get();

                    description = "Brand Invoice";
                    creationPayOS = payOSService.createBrandPaymentLink(
                            PayOSRequest
                                    .builder()
                                    .amount(paymentAmount)
                                    .description(description)
                                    .buyerName(recipient.getName())
                                    .buyerEmail(recipient.getEmail())
                                    .buyerPhone(recipient.getPhoneNumber())
                                    .buyerAddress("")
                                    .returnUrl(clientURL + "/accountant")
                                    .build()
                    );
                    if (creationPayOS == null) {
                        throw new Exception("Create PayOS Fail!");
                    }
                    Integer orderCode = creationPayOS.getData().getOrderCode();

                    var order = orderRepository.findById(orderID)
                            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderID));

                    var storedPayment = paymentRepository.save(
                            Payment.builder()
                                    .paymentSender(sender)
                                    .paymentSenderName(paymentSenderName)
                                    .paymentSenderBankCode(paymentSenderBankCode)
                                    .paymentSenderBankNumber(paymentSenderBankNumber)

                                    .paymentRecipient(recipient)
                                    .paymentRecipientName(recipient.getName())
                                    .paymentRecipientBankCode(paymentRecipientBankCode)
                                    .paymentRecipientBankNumber(paymentRecipientBankNumber)

                                    .paymentMethod(paymentMethod)
                                    .paymentAmount(paymentAmount)
                                    .paymentStatus(paymentStatus)
                                    .paymentType(paymentType)
                                    .order(order)  // Use the retrieved order here
                                    .paymentCode(orderCode)
                                    .build()
                    );

                    return paymentMapper.mapperToPaymentResponse(storedPayment);
                } else {
                    if (paymentType.equals(PaymentType.ORDER_REFUND)) {
                        var checkRecipient = userService.getUserByUserID(paymentRecipientID);
                        if (checkRecipient.isEmpty()) {
                            throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
                        }
                        var recipient = checkRecipient.get();

                        var checkSender = userService.getUserByUserID(paymentSenderID);
                        if (checkSender.isEmpty()) {
                            throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
                        }
                        var sender = checkSender.get();

                        description = "Refund Invoice";
                        creationPayOS = payOSService.createRefundPaymentLink(
                                PayOSRequest
                                        .builder()
                                        .amount(paymentAmount)
                                        .description(description)
                                        .buyerName(paymentRecipientName)
                                        .buyerEmail(recipient.getEmail())
                                        .buyerPhone(recipient.getPhoneNumber())
                                        .buyerAddress("")
                                        .returnUrl(clientURL + "/accountant")
                                        .build()
                        );
                        if (creationPayOS == null) {
                            throw new Exception("Create PayOS Fail!");
                        }
                        logger.error("CREATE CUS REFUND SUCCESSFULLY");
                        Integer orderCode = creationPayOS.getData().getOrderCode();

                        var order = orderRepository.findById(orderID)
                                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderID));

                        var storedPayment = paymentRepository.save(
                                Payment.builder()
                                        .paymentSender(sender)
                                        .paymentSenderName(paymentSenderName)
                                        .paymentSenderBankCode(paymentSenderBankCode)
                                        .paymentSenderBankNumber(paymentSenderBankNumber)

                                        .paymentRecipient(recipient)
                                        .paymentRecipientName(paymentRecipientName)
                                        .paymentRecipientBankCode(paymentRecipientBankCode)
                                        .paymentRecipientBankNumber(paymentRecipientBankNumber)

                                        .paymentMethod(paymentMethod)
                                        .paymentAmount(paymentAmount)
                                        .paymentStatus(paymentStatus)
                                        .paymentType(paymentType)
                                        .order(order)  // Use the retrieved order here
                                        .paymentCode(orderCode)
                                        .build()
                        );
                        logger.error("AFTER SAVE PAYMENT: {}", storedPayment);
                        return paymentMapper.mapperToPaymentResponse(storedPayment);
                    } else {
                        if (paymentType.equals(PaymentType.DEPOSIT)) {
                            description = "DEPOSIT ORDER";
                        } else if (paymentType.equals(PaymentType.STAGE_1)) {
                            description = "STAGE 1";
                        } else if (paymentType.equals(PaymentType.STAGE_2)) {
                            description = "STAGE 2";
                        } else if (paymentType.equals(PaymentType.COMPLETED_ORDER)) {
                            description = "COMPLETED ORDER";
                        } else {
                            description = "FINED";
                        }

                        creationPayOS = payOSService.createPaymentLink(
                                PayOSRequest
                                        .builder()
                                        .amount(paymentAmount)
                                        .description(description)
                                        .buyerName("")
                                        .buyerEmail("")
                                        .buyerPhone("")
                                        .buyerAddress("")
                                        .returnUrl(clientURL + "/order_detail/" + orderID)
                                        .build()
                        );
                        logger.info("CREATE PayOS SUCCESSFULLY!");

                        var checkSender = userService.getUserByUserID(paymentSenderID);
                        if (checkSender.isEmpty()) {
                            throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
                        }
                        var sender = checkSender.get();

                        var checkRecipient = userService.getUserByUserID(paymentRecipientID);
                        if (checkRecipient.isEmpty()) {
                            throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
                        }
                        var recipient = checkRecipient.get();

                        if (creationPayOS == null) {
                            throw new Exception("Create PayOS Fail!");
                        }
                        Integer orderCode = creationPayOS.getData().getOrderCode();

                        var order = orderRepository.findById(orderID)
                                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderID));

                        var storedPayment = paymentRepository.save(
                                Payment.builder()
                                        .paymentSender(sender)
                                        .paymentSenderName(paymentSenderName)
                                        .paymentSenderBankCode(paymentSenderBankCode)
                                        .paymentSenderBankNumber(paymentSenderBankNumber)

                                        .paymentRecipient(recipient)
                                        .paymentRecipientName("SMART TAILOR")
                                        .paymentRecipientBankCode("OCB")
                                        .paymentRecipientBankNumber("0163100007285002")

                                        .paymentMethod(paymentMethod)
                                        .paymentAmount(paymentAmount)
                                        .paymentStatus(paymentStatus)
                                        .paymentType(paymentType)
                                        .order(order)  // Use the retrieved order here
                                        .paymentCode(orderCode)
                                        .build()
                        );

                        return paymentMapper.mapperToPaymentResponse(storedPayment);
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public PaymentResponse createManualPayment(PaymentRequest paymentRequest) throws Exception {
        try {

            if (paymentRequest == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }

            if (paymentRequest.getPaymentSenderID() == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            String paymentSenderID = paymentRequest.getPaymentSenderID();
            String paymentSenderName = paymentRequest.getPaymentSenderName() != null ? paymentRequest.getPaymentSenderName() : "";
            String paymentSenderBankCode = paymentRequest.getPaymentSenderBankCode() != null ? paymentRequest.getPaymentSenderBankCode() : "";
            String paymentSenderBankNumber = paymentRequest.getPaymentSenderBankNumber() != null ? paymentRequest.getPaymentSenderBankNumber() : "";

            if (paymentRequest.getPaymentRecipientID() == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            String paymentRecipientID = paymentRequest.getPaymentRecipientID();
            String paymentRecipientName = paymentRequest.getPaymentRecipientName() != null ? paymentRequest.getPaymentRecipientName() : "";
            String paymentRecipientBankCode = paymentRequest.getPaymentRecipientBankCode() != null ? paymentRequest.getPaymentRecipientBankCode() : "";
            String paymentRecipientBankNumber = paymentRequest.getPaymentRecipientBankNumber() != null ? paymentRequest.getPaymentRecipientBankNumber() : "";

            Integer paymentAmount = paymentRequest.getPaymentAmount() > 0 ? paymentRequest.getPaymentAmount() : 0;
            PaymentMethod paymentMethod = paymentRequest.getPaymentMethod();
            PaymentType paymentType = paymentRequest.getPaymentType();


            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String orderType = "order-type";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(paymentAmount * 100));
            vnp_Params.put("vnp_CurrCode", "VND");

            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", paymentType.name() + " " + paymentRecipientID);
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = "vn";
            vnp_Params.put("vnp_Locale", locate);

            String urlReturn = clientURL;
            vnp_Params.put("vnp_ReturnUrl", urlReturn);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MONTH, 2);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    try {
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        //Build query
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append('=');
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnp_PayUrl + "?" + queryUrl;

            var checkSender = userService.getUserByUserID(paymentSenderID);
            if (checkSender.isEmpty()) {
                throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentSenderID);
            }
            var sender = checkSender.get();

            var checkRecipient = userService.getUserByUserID(paymentRecipientID);
            if (checkRecipient.isEmpty()) {
                throw new Exception(MessageConstant.USER_IS_NOT_FOUND + " with ID: " + paymentRecipientID);
            }
            var recipient = checkRecipient.get();

            String senderEmail = sender.getEmail();
            String senderPhone = sender.getPhoneNumber();
            String senderAddress = "";
            if (sender.getRoles().getRoleName().equals("BRAND")) {
                var brand = sender.getBrand();
                senderAddress = brand.getProvince() + " " + brand.getDistrict() + " " + brand.getWard() + " " + brand.getAddress();
            } else if (sender.getRoles().getRoleName().equals("CUSTOMER")) {
                var customer = sender.getCustomer();
                senderAddress = customer.getProvince() + " " + customer.getDistrict() + " " + customer.getWard() + " " + customer.getAddress();
            }
            var orderID = paymentRequest.getOrderID() != null ? paymentRequest.getOrderID() : null;
            Order order = null;
            if (orderID != null) {
                order = orderRepository.findById(orderID).isPresent() ? orderRepository.findById(orderID).get() : null;
            }
            var storedPayment = paymentRepository.save(
                    Payment.builder()
                            .paymentSender(sender)
                            .paymentSenderName(paymentSenderName)
                            .paymentSenderBankCode(paymentSenderBankCode)
                            .paymentSenderBankNumber(paymentSenderBankNumber)

                            .paymentRecipient(recipient)
                            .paymentRecipientName(paymentRecipientName)
                            .paymentRecipientBankCode(paymentRecipientBankCode)
                            .paymentRecipientBankNumber(paymentRecipientBankNumber)

                            .paymentMethod(paymentMethod)
                            .paymentAmount(paymentAmount)
                            .paymentStatus(false)
                            .paymentType(paymentType)
                            .order(order)
                            .paymentCode(null)
                            .paymentURl(paymentUrl)
                            .build()
            );
            return paymentMapper.mapperToPaymentResponse(storedPayment);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PaymentResponse getManualPaymentByID(String paymentID) throws Exception {
        try {

            var checkPayment = paymentRepository.findByPaymentID(paymentID);
            if (checkPayment.isEmpty()) {
                return null;
            }
            var payment = checkPayment.get();

            var paymentURL = payment.getPaymentURl();
            // Tạo đối tượng URL
            URL urlObj = new URL(paymentURL);

            // Lấy phần chuỗi truy vấn của URL
            String query = urlObj.getQuery();

            // Phân tích chuỗi truy vấn
            Map<String, String> params = new HashMap<>();
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    params.put(URLDecoder.decode(pair[0], "UTF-8"), URLDecoder.decode(pair[1], "UTF-8"));
                }
            }

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_RequestId = formatter.format(cld.getTime());
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_RequestId", vnp_RequestId);
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", params.get("vnp_OrderInfo"));
            vnp_Params.put("vnp_TransactionNo", params.get("vnp_TransactionNo"));
            vnp_Params.put("vnp_TransactionDate", params.get("vnp_CreateDate"));
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            String data = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" + vnp_TmnCode + "|" + vnp_TxnRef + "|" + params.get("vnp_CreateDate") + "|" + vnp_CreateDate + "|" + vnp_IpAddr + "|" + params.get("vnp_OrderInfo");

            String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, data);
            vnp_Params.put("vnp_SecureHash", vnp_SecureHash);


            ObjectMapper objectMapper = new ObjectMapper();
            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Gửi yêu cầu POST

            WebClient client = WebClient.create();
            Mono<String> response = client.post()
                    .uri(vnp_apiUrl)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .body(BodyInserters.fromValue(vnp_Params))
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
            System.out.println(res);
            return paymentMapper.mapperToPaymentResponse(payment);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public PaymentResponse getPaymentByID(String paymentID) throws Exception {
        try {
            if (paymentID == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            var checkPayment = paymentRepository.findByPaymentID(paymentID);
            if (checkPayment.isPresent()) {
                var payment = checkPayment.get();
                return paymentMapper.mapperToPaymentResponse(payment);
            }
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Payment> findAllByOrderID(String orderID) {
        return paymentRepository.findAllByOrderID(orderID).stream().sorted(Comparator.comparing(Payment::getCreateDate).reversed()).toList();
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayment() {
        return paymentRepository.findAll().stream().sorted(Comparator.comparing(Payment::getCreateDate).reversed()).toList();
    }

    @Override
    public List<PaymentResponse> getAllPaymentResponse() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::mapperToPaymentResponse)
                .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentByUserID(String userID) {
        try {
            if (userID == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }

            var user = userService.getUserByUserID(userID).orElseThrow(() -> {
                return new ItemNotFoundException(MessageConstant.USER_IS_NOT_FOUND);
            });

            return paymentRepository.findAll()
                    .stream()
                    .filter(p -> p.getPaymentRecipient().getUserID().equals(userID)
                            ||
                            p.getPaymentSender().getUserID().equals(userID))
                    .map(paymentMapper::mapperToPaymentResponse)
                    .sorted(Comparator.comparing(PaymentResponse::getCreateDate).reversed())
                    .toList();
        } catch (Exception ex) {
            logger.error("ERROR: {}", ex.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public GrowthPercentageResponse calculatePaymentGrowthPercentageForCurrentAndPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentWeek = now;

        LocalDateTime startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);
        LocalDateTime endOfPreviousWeek = startOfCurrentWeek.minusSeconds(1);

        BigDecimal currentWeekPayment = BigDecimal.ZERO;
        BigDecimal previousWeekPayment = BigDecimal.ZERO;

        List<Payment> payments = paymentRepository.findAll();
        for (Payment payment : payments) {
            LocalDateTime createDate = payment.getCreateDate();

            if (!createDate.isBefore(startOfCurrentWeek) && !createDate.isAfter(endOfCurrentWeek)) {
                currentWeekPayment = currentWeekPayment.add(Utilities.roundToNearestThousand(BigDecimal.valueOf(payment.getPaymentAmount())));
            }

            if (!createDate.isBefore(startOfPreviousWeek) && !createDate.isAfter(endOfPreviousWeek)) {
                previousWeekPayment = previousWeekPayment.add(Utilities.roundToNearestThousand(BigDecimal.valueOf(payment.getPaymentAmount())));
            }
        }

        if (previousWeekPayment.compareTo(BigDecimal.ZERO) == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentWeekPayment.toString())
                    .previousData(previousWeekPayment.toString())
                    .growthPercentage(currentWeekPayment.compareTo(BigDecimal.valueOf(0)) > 0 ? 100.0f : 0.0f)
                    .build();
        }

        BigDecimal growthPercentage = currentWeekPayment
                .subtract(previousWeekPayment)
                .divide(previousWeekPayment)
                .multiply(BigDecimal.valueOf(100.0f));

        var roundGrowthPercentage = growthPercentage
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentWeekPayment.toString())
                .previousData(previousWeekPayment.toString())
                .growthPercentage(roundGrowthPercentage)
                .build();
    }

    @Override
    public GrowthPercentageResponse calculateIncomeGrowthPercentageForCurrentAndPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentWeek = now;

        LocalDateTime startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);
        LocalDateTime endOfPreviousWeek = startOfCurrentWeek.minusSeconds(1);

        var orderFeePercentage = Integer.parseInt(systemPropertiesService.getByName("ORDER_FEE_PERCENTAGE").getPropertyValue());
        BigDecimal currentWeekIncomePayment = BigDecimal.ZERO;
        BigDecimal previousWeekIncomePayment = BigDecimal.ZERO;

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {
            if (payment.getOrder().getOrderType().equalsIgnoreCase("PARENT_ORDER") &&
                    payment.getPaymentType().name().equals(PaymentType.DEPOSIT.name())) {

                LocalDateTime createDate = payment.getCreateDate();

                if (!createDate.isBefore(startOfCurrentWeek) && !createDate.isAfter(endOfCurrentWeek)) {
                    currentWeekIncomePayment = currentWeekIncomePayment.add(
                            Utilities.roundToNearestThousand(
                                    BigDecimal.valueOf(payment.getOrder().getTotalPrice())
                                            .multiply(BigDecimal.valueOf(orderFeePercentage))
                                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP)
                            ));
                }

                if (!createDate.isBefore(startOfPreviousWeek) && !createDate.isAfter(endOfPreviousWeek)) {
                    previousWeekIncomePayment = previousWeekIncomePayment.add(Utilities.roundToNearestThousand(
                            BigDecimal.valueOf(payment.getOrder().getTotalPrice())
                                    .multiply(BigDecimal.valueOf(orderFeePercentage))
                                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP)
                    ));
                }
            }
        }

        if (previousWeekIncomePayment.compareTo(BigDecimal.ZERO) == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentWeekIncomePayment.toString())
                    .previousData(previousWeekIncomePayment.toString())
                    .growthPercentage(currentWeekIncomePayment.compareTo(BigDecimal.valueOf(0)) > 0 ? 100.0f : 0.0f)
                    .build();
        }

        BigDecimal growthPercentage = currentWeekIncomePayment
                .subtract(previousWeekIncomePayment)
                .divide(previousWeekIncomePayment)
                .multiply(BigDecimal.valueOf(100.0f));

        var roundGrowthPercentage = growthPercentage
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentWeekIncomePayment.toString())
                .previousData(previousWeekIncomePayment.toString())
                .growthPercentage(roundGrowthPercentage)
                .build();

    }

    @Override
    public GrowthPercentageResponse calculateRefundGrowthPercentageForCurrentAndPreviousMonth() {
        LocalDateTime now = LocalDateTime.now();

        YearMonth currentMonth = YearMonth.from(now);
        LocalDateTime startOfCurrentMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfCurrentMonth = now;

        YearMonth previousMonth = currentMonth.minusMonths(1);
        LocalDateTime startOfPreviousMonth = previousMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfPreviousMonth = previousMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        BigDecimal currentWeekRefundPayment = BigDecimal.ZERO;
        BigDecimal previousWeekRefundPayment = BigDecimal.ZERO;

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {
            LocalDateTime createDate = payment.getCreateDate();
            if (payment.getPaymentType().name().equals(PaymentType.ORDER_REFUND.name())) {
                if (!createDate.isBefore(startOfCurrentMonth) && !createDate.isAfter(endOfCurrentMonth)) {
                    currentWeekRefundPayment = currentWeekRefundPayment.add(Utilities.roundToNearestThousand(BigDecimal.valueOf(payment.getPaymentAmount())));
                }

                if (!createDate.isBefore(startOfPreviousMonth) && !createDate.isAfter(endOfPreviousMonth)) {
                    previousWeekRefundPayment = previousWeekRefundPayment.add(Utilities.roundToNearestThousand(BigDecimal.valueOf(payment.getPaymentAmount())));
                }
            }
        }

        if (previousWeekRefundPayment.compareTo(BigDecimal.ZERO) == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentWeekRefundPayment.toString())
                    .previousData(previousWeekRefundPayment.toString())
                    .growthPercentage(currentWeekRefundPayment.compareTo(BigDecimal.valueOf(0)) > 0 ? 100.0f : 0.0f)
                    .build();
        }

        BigDecimal growthPercentage = currentWeekRefundPayment
                .subtract(previousWeekRefundPayment)
                .divide(previousWeekRefundPayment)
                .multiply(BigDecimal.valueOf(100.0f));

        var roundGrowthPercentage = growthPercentage
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentWeekRefundPayment.toString())
                .previousData(previousWeekRefundPayment.toString())
                .growthPercentage(roundGrowthPercentage)
                .build();
    }

    @Override
    public List<Pair<String, String>> getTotalPaymentOfEachMonth() {
        var listPayment = paymentRepository.findAll();
        Map<Integer, BigDecimal> map = new HashMap<>();
        for (Payment payment : listPayment) {
            int month = payment.getCreateDate().getMonthValue();

            BigDecimal paymentAmount = BigDecimal.valueOf(payment.getPaymentAmount());
            map.merge(month, paymentAmount, BigDecimal::add);
        }

        List<Pair<String, String>> listPaymentDetail = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            String monthName = Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            BigDecimal total = map.getOrDefault(i, BigDecimal.ZERO);
            listPaymentDetail.add(Pair.of(monthName, total.toString()));
        }

        return listPaymentDetail;
    }

    @Override
    public List<Pair<String, String>> getTotalRefundPaymentOfEachMonth() {
        var listPayment = paymentRepository.findAll();
        Map<Integer, BigDecimal> map = new HashMap<>();
        for (Payment payment : listPayment) {
            if (payment.getPaymentType().name().equals(PaymentType.ORDER_REFUND.name())) {
                int month = payment.getCreateDate().getMonthValue();

                BigDecimal paymentAmount = BigDecimal.valueOf(payment.getPaymentAmount());
                map.merge(month, paymentAmount, BigDecimal::add);
            }
        }

        List<Pair<String, String>> listPaymentDetail = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            String monthName = Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            BigDecimal total = map.getOrDefault(i, BigDecimal.ZERO);
            listPaymentDetail.add(Pair.of(monthName, total.toString()));
        }

        return listPaymentDetail;
    }

    @Override
    public List<Pair<String, String>> getTotalIncomePaymentOfEachMonth() {
        var listPayment = paymentRepository.findAll();
        Map<Integer, BigDecimal> map = new HashMap<>();
        var orderFeePercentage = Integer.parseInt(systemPropertiesService.getByName("ORDER_FEE_PERCENTAGE").getPropertyValue());
        for (Payment payment : listPayment) {
            if (payment.getOrder().getOrderType().equalsIgnoreCase("PARENT_ORDER") &&
                    payment.getPaymentType().name().equals(PaymentType.DEPOSIT.name())) {

                int month = payment.getCreateDate().getMonthValue();

                BigDecimal paymentAmount = Utilities.roundToNearestThousand(BigDecimal
                        .valueOf(payment.getPaymentAmount())
                        .multiply(BigDecimal.valueOf(orderFeePercentage))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));

                map.merge(month, paymentAmount, BigDecimal::add);
            }
        }

        List<Pair<String, String>> listPaymentDetail = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            String monthName = Month.of(i).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            BigDecimal total = map.getOrDefault(i, BigDecimal.ZERO);
            listPaymentDetail.add(Pair.of(monthName, total.toString()));
        }

        return listPaymentDetail;
    }


}
