package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportImageResponse {
    private String reportImageID;

    private String reportImageName;

    private String reportImageUrl;

    private String createDate;

    private String lastModifiedDate;
}
