package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse {
    private Integer status;
    private String message;
    private Object data;
}
