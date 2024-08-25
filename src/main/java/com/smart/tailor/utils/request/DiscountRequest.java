package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    private String discountName;
    private Double discountPercent;
    private Integer quantity;
    private LocalDateTime startDateTime;
    private LocalDateTime expiredDateTime;
}
