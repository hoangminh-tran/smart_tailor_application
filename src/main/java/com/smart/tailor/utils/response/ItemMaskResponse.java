package com.smart.tailor.utils.response;

import com.smart.tailor.enums.PrintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskResponse {
    private String itemMaskID;

    private String itemMaskName;

    private String typeOfItem;

    private MaterialResponse material;

    private Boolean isSystemItem;

    private Float positionX;

    private Float positionY;

    private Float scaleX;

    private Float scaleY;

    private Integer indexZ;

    private Float rotate;

    private Float topLeftRadius;

    private Float topRightRadius;

    private Float bottomLeftRadius;

    private Float bottomRightRadius;

    private String imageUrl;

    private Boolean status;

    private PrintType printType;

    private String createDate;

    private String lastModifiedDate;
}
