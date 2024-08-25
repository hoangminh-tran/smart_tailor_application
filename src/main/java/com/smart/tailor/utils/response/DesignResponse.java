package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignResponse {
    private String designID;

    private UserResponse user;

    private ExpertTailoringResponse expertTailoring;

    private String titleDesign;

    private Boolean publicStatus;

    private Float minWeight;

    private Float maxWeight;

    private String imageUrl;

    private String color;

    private List<PartOfDesignResponse> partOfDesign;

    private List<DesignMaterialDetail> materialDetail;

    private String createDate;

    private String lastModifiedDate;
}
