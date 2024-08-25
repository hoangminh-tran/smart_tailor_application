package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String productID;
    private String designID;
    private String brandID;
    private String productName;
    private Double pricePerProduct;
    private Boolean gender;
    private Integer rating;
    private String description;
    private Boolean publicStatus;
    private String size;
    private Boolean productStatus;
}
