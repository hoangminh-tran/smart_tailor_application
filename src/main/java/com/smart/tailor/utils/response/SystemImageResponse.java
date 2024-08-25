package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemImageResponse {
    private String imageID;
    private String imageName;
    private String imageURL;
    private Boolean imageStatus;
    private String imageType;
    private Boolean isPremium;
}
