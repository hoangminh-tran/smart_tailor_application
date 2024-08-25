package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippingResponse {
    private Boolean success;

    private String message;

    private String partner_id; // Mã đơn hàng thuộc hệ thống của đối tác

    private String label; // Mã đơn hàng của hệ thống GHTK

    private Integer fee; // Cước vận chuyển tính theo VNĐ

    private String estimated_pick_time;

    private String estimated_deliver_time;

    private Integer status_id; //  Mã trạng thái đơn hàng

    private String warning_message;
}