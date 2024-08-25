package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTimeLineResponse {
    private String estimatedDateStartDepositStage;

    private Integer estimatedQuantityFinishFirstStage;

    private String estimatedDateFinishFirstStage;

    private Integer estimatedQuantityFinishSecondStage;

    private String estimatedDateFinishSecondStage;

    private Integer estimatedQuantityFinishCompleteStage;

    private String estimatedDateFinishCompleteStage;
}
