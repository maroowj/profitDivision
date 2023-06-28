package com.muzisoft.division.web.api.dto.admin.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalListPerUserResponse {

    private String withdrawalSeq;
    private String createdAt;
    private long totalBalance;
    private long amount;
    private int accepted;
    private int type;
    private String source;
}
