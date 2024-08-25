package com.smart.tailor.utils.response;

import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String paymentID;

    private String paymentSenderID;
    private String paymentSenderName;
    private String paymentSenderBankCode;
    private String paymentSenderBankNumber;

    private String paymentRecipientID;
    private String paymentRecipientName;
    private String paymentRecipientBankCode;
    private String paymentRecipientBankNumber;

    private Integer paymentAmount;
    private PaymentMethod paymentMethod;
    private Boolean paymentStatus;
    private PaymentType paymentType;

    private String orderID;
    private String paymentURl;
    private PayOSResponse payOSResponse;
    private PayOSDataResponse payOSData;
    private String createDate;
}

