package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignInformation {
    private String partOfDesignName;

    private Integer width;

    private Integer height;

    private String materialID;

    private String materialName;
}
