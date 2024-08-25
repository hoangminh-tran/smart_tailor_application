package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSDataResponse {
    private Integer payOSID;

    private Integer orderCode; // Mã đơn hàng từ cửa hàng

    private Integer amount;

    private String status;

    private String checkoutUrl; // Link thanh toán

    private String qrCode; // Mã VietQR dạng text

    private String createDate;

    private String lastModifiedDate;
}
