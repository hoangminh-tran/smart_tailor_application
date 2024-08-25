package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CellErrorResponse {
    private int rowIndex;

    private int cellIndex;

    private String cellName;

    private Object data;

    private String message;
}
