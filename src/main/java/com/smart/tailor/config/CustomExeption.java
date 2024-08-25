package com.smart.tailor.config;

import com.smart.tailor.constant.ErrorConstant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomExeption extends Exception {
    private final ErrorConstant errorConstant;
}
