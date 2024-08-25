package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusDetailResponse {
    private List<Pair<String, Long>> orderStatusDetailList;
}
