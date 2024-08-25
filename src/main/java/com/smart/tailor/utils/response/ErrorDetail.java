package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetail {
    private Object errorData;

    private Object errorMessage;

    public ErrorDetail(Object errorMessage) {
        this.errorMessage = errorMessage;
    }
}
