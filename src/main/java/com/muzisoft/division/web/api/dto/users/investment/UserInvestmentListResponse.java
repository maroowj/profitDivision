package com.muzisoft.division.web.api.dto.users.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInvestmentListResponse {

    private String createdAt;
    private String depositedAt;
    private int amount;
    private int status;
    private String comment;
    private int rowNum;
}
