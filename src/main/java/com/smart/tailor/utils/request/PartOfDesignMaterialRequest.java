package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignMaterialRequest {
    @NotNull(message = "materialName is not null")
    @NotBlank(message = "materialName is not blank")
    private String materialName;
}
