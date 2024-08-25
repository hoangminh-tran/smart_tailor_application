package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailShippingResponse {
    private boolean success;
    private String message;
    private String label_id;
    private String partner_id;
    private int status;
    private String status_text;
    private String created;
    private String modified;
    private String pick_date;
    private String deliver_date;
    private String customer_fullName;
    private String customer_tel;
    private String address;
    private int ship_money;
    private int weight;
}
