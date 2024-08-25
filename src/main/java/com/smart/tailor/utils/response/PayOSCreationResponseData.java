package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayOSCreationResponseData {
    private String bin; //Mã định danh ngân hàng (thường gọi là BIN)
    private String accountNumber; //Số tài khoản ngân hàng thụ hưởng
    private String accountName; //Tên tài khoản ngân hàng
    private String currency; //Đơn vị tiền tệ
    private String paymentLinkId; //Mã link thanh toán
    private Integer amount; //Số tiền thanh toán
    private String description; //Mô tả thanh toán
    private Integer orderCode; //Mã đơn hàng từ cửa hàng
    private String status; //Trạng thái link thanh toán
    private String checkoutUrl; //Link thanh toán
    private String qrCode; //Mã VietQR dạng text
}
