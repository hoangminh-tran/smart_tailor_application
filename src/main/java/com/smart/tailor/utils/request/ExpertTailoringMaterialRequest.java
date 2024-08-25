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
public class ExpertTailoringMaterialRequest {
    @NotBlank(message = "categoryName can not be blank")
    @NotNull(message = "categoryName can not be null")
    private String categoryName;

    @NotBlank(message = "materialName can not be blank")
    @NotNull(message = "materialName can not be null")
    private String materialName;

    @NotBlank(message = "expertTailoringName can not be blank")
    @NotNull(message = "expertTailoringName can not be null")
    private String expertTailoringName;
}
