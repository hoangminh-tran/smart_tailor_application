package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskInformation {
    private String itemMaskID;

    private String itemMaskName;

    private Float scaleX;

    private Float scaleY;

    private String materialID;

    private String materialName;
}
