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
public class FeeResponse {
    private Boolean success;
    private int fee;
    private String message;

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Fee {
//        private String name;
//        private int fee;
//        private int insurance_fee;
//        private int include_vat;
//        private int cost_id;
//        private String delivery_type;
//        private int a;
//        private String dt;
//        private List<Object> extFees; // Assuming extFees can be of any type or empty
//        private String promotion_key;
//        private boolean delivery;
//        private int ship_fee_only;
//        private int distance;
//        private Options options;
//
//        @Data
//        @AllArgsConstructor
//        @NoArgsConstructor
//        @Builder
//        public static class Options {
//            private String name;
//            private String title;
//            private int shipMoney;
//            private String shipMoneyText;
//            private String vatText;
//            private String desc;
//            private String coupon;
//            private int maxUses;
//            private int maxDates;
//            private String maxDateString;
//            private String content;
//            private String activatedDate;
//            private String couponTitle;
//            private String discount;
//        }
//    }
}