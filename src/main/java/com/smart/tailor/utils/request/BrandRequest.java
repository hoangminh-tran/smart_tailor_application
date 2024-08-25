package com.smart.tailor.utils.request;

import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandRequest {
    private String brandName;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String qrPayment;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String taxCode;
    private Float rating;
    private Integer numberOfViolations;
    private BrandStatus brandStatus;
    private List<BrandImageRequest> brandImages;
}
