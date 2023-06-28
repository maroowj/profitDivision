package com.muzisoft.division.web.api.dto.admin.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalListResponse {

    private String userDetailsSeq;
    private String withdrawalSeq;
    private String membershipNumber;
    private String userId;
    private String name;
    private long totalBalance;
    private long amount;
    private String bankName;
    private String accountNumber;
    private String createdAt;
    private int accepted;
    private int type;
    private String source;
}
