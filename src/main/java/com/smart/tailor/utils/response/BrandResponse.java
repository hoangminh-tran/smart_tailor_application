package com.smart.tailor.utils.response;

import com.smart.tailor.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private String brandID;

    private UserResponse user;

    private String brandName;

    private BrandStatus brandStatus;

    private Float rating;

    private Integer numberOfRatings;

    private Float totalRatingScore;

    private String bankName;

    private String accountNumber;

    private String accountName;

    private String QR_Payment;

    private String address;

    private String province;

    private String district;

    private String ward;

    private String taxCode;

    private Integer numberOfViolations;

    private String createDate;

    private String lastModifiedDate;

    private List<BrandImageResponse> images;
}
