package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSResponseData {
    private String id;
    private Integer orderCode; //Mã đơn hàng từ cửa hàng
    private Integer amount;
    private Integer amountPaid;
    private Integer amountRemaining;
    private String status; //Trạng thái link thanh toán
    private List<Transactions> transactions;
    private String createdAt;
    private String canceledAt;
    private String cancellationReason;
    private String checkoutUrl; //Link thanh toán
    private String qrCode; //Mã VietQR dạng text
}
