package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeResponse {
    private String sizeID;

    private String sizeName;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
