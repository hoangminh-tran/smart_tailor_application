package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborQuantityRequestList {
    @Valid
    @NotEmpty(message = "laborQuantityRequests is not empty")
    private List<@Valid LaborQuantityRequest> laborQuantityRequests;
}
