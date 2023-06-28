package com.muzisoft.division.web.api.dto.users.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithdrawalListResponse {

    private int rowNum;
    private String seq;
    private String createdAt;
    private String lastModifiedAt;
    private String requestedAt;
    private String acceptedAt;
    private long amount;
    private long totalBalance;
    private int accepted;
    private String comment;
}
