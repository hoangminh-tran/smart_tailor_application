package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayOSRequest {
    private Integer orderCode;          // Mã đơn hàng
    private Integer amount;             // Số tiền thanh toán
    private String description;         // Mô tả thanh toán
    private String buyerName;           // Tên của người mua hàng
    private String buyerEmail;          // Email của người mua hàng
    private String buyerPhone;          // Số điện thoại người mua hàng
    private String buyerAddress;        // Địa chỉ của người mua hàng
    private List<PayOSItem> items;      // Danh sách các sản phẩm thanh toán
    private String cancelUrl;           // URL nhận dữ liệu khi người dùng chọn Huỷ đơn hàng.
    private String returnUrl;           // URL nhận dữ liệu khi đơn hàng thanh toán thành công
    private Integer expiredAt;          // Thời gian hết hạn của link thanh toán, là Unix Timestamp và kiểu Int32
    private String signature;           // Chữ ký kiểm tra thông tin.
}