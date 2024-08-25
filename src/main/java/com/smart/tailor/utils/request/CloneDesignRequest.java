package com.smart.tailor.utils.request;

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
public class CloneDesignRequest {
    @NotBlank(message = "userID can not be blank")
    @NotNull(message = "userID can not be null")
    private String userID;

    @NotBlank(message = "designID can not be blank")
    @NotNull(message = "designID can not be null")
    private String designID;

    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
