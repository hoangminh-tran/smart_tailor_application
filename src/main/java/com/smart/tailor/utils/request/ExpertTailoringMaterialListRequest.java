package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class ExpertTailoringMaterialListRequest {
    @NotBlank(message = "categoryName can not be blank")
    @NotNull(message = "categoryName can not be null")
    private String categoryName;

    @NotBlank(message = "materialName can not be blank")
    @NotNull(message = "materialName can not be null")
    private String materialName;

    @NotEmpty(message = "expertTailoringNames is not empty")
    private List<String> expertTailoringNames;
}
