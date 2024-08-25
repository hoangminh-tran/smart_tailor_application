package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ReportRequest {
    @NotBlank(message = "User ID is required")
    private String userID;

    @NotBlank(message = "Type Of Report is required")
    @Size(max = 50, message = "Type Of Report must not exceed 50 characters")
    private String typeOfReport;

    @NotBlank(message = "Order ID is required")
    private String orderID;

    @NotBlank(message = "Content is required")
    private String content;

    private List<@Valid ReportImageRequest> reportImageList;
}
