package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeExpertTailoringResponse {
    private String expertTailoringID;

    private String expertTailoringName;

    private String sizeID;

    private String sizeName;

    private Double ratio;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
