package com.smart.tailor.utils.request;

import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
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
    private PaymentType paymentType;

    private String orderID;
    private List<PayOSItem> itemList;
}
