package com.smart.tailor.enums;

public enum OrderStatus {
    NOT_VERIFY, // ch xac thuc design
    PENDING,    // cho brand pick
    DEPOSIT,    // cho dat coc
    PREPARING,
    PROCESSING, // cho may
    SUSPENDED,
    CANCEL,     // huy
    COMPLETED,   // hoan thanh nhung chua giao
    FINAL_CHECKING,
    DELIVERED,  // da giao
    RECEIVED,
    REFUND_REQUEST,

    /**
     * SUB ORDER
     */
    CHECKING_SAMPLE_DATA,
    APPROVED,
    REJECTED,
    START_PRODUCING,
    FINISH_FIRST_STAGE,
    FINISH_SECOND_STAGE,
}
