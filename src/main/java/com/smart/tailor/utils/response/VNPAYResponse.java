package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VNPAYResponse {
    private String vnp_ResponseId;
    private String vnp_Command;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private Integer vnp_Amount;
    private Integer vnp_OrderInfo;
    private Integer vnp_ResponseCode;
    private String vnp_Message;
    private String vnp_BankCode;
    private Integer vnp_PayDate;
    private Integer vnp_TransactionNo;
    private Integer vnp_TransactionType;
    private Integer vnp_TransactionStatus;
    private String vnp_SecureHash;
}
