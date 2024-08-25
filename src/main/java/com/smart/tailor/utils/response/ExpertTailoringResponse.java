package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertTailoringResponse {
    private String expertTailoringID;

    private String expertTailoringName;

    private String sizeImageUrl;

    private String modelImageUrl;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
