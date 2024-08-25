package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PayOSItem implements Serializable {
    private String name;
    private Integer quantity;
    private Integer price;
}