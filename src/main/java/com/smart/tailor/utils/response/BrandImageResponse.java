package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandImageResponse {
    private String imageID;

    private String imageUrl;// Chuỗi Base64 của hình ảnh

    private String imageDescription;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
