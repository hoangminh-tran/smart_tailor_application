package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    private String accountNumber;
    private Integer amount;
    private String counterAccountBankId;
    private String counterAccountBankName;
    private String counterAccountName;
    private String counterAccountNumber;
    private String description;
    private String reference;
    private String transactionDateTime;
    private String virtualAccountName;
    private String virtualAccountNumber;
}
