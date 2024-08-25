package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignRequest {
    @NotBlank(message = "Part Of Design Name is required")
    @Size(max = 50, message = "Part Of Design Name must not exceed 50 characters")
    private String partOfDesignName;

    @NotBlank(message = "Part Of Design Image URL is required")
    private String imageUrl;

    private String materialID;

    private String successImageUrl;

    private String realPartImageUrl;

    @NotNull(message = "Width is required")
    @Min(value = 1, message = "Width must be greater than 0")
    private Integer width;

    @NotNull(message = "Height is required")
    @Min(value = 1, message = "Height must be greater than 0")
    private Integer height;

    @Valid
    private List<@Valid ItemMaskRequest> itemMask;
}
