package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandLaborQuantityListRequest {
    @NotNull(message = "brandID is not null")
    @NotBlank(message = "brandID is not blank")
    private String brandID;

    @Valid
    private List<@Valid BrandLaborQuantityRequest> brandLaborQuantity;
}
