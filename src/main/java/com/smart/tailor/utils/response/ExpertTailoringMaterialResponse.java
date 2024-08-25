package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertTailoringMaterialResponse {
    private String categoryID;

    private String categoryName;

    private String materialID;

    private String materialName;

    private String expertTailoringID;

    private String expertTailoringName;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
