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
public class PartOfDesignResponse {
    private String partOfDesignID;

    private String partOfDesignName;

    private String imageUrl;

    private String successImageUrl;

    private String realPartImageUrl;

    private Integer width;

    private Integer height;

    private MaterialResponse material;

    private List<ItemMaskResponse> itemMasks;

    private String createDate;

    private String lastModifiedDate;
}
