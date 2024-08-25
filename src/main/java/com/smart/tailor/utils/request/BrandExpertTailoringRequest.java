package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandExpertTailoringRequest {
    @NotBlank(message = "Brand ID is required")
    private String brandID;

    @NotEmpty(message = "Expert Tailoring ID List is required")
    private List<String> expertTailoringIDList;
}
