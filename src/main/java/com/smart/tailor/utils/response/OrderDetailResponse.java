package com.smart.tailor.utils.response;

import com.smart.tailor.utils.request.DesignDetailSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    List<DesignDetailSize> sizeList;
    String orderID;
    String employeeID;
}

