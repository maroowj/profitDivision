package com.muzisoft.division.web.api.dto.admin.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentListResponse {

    private String membershipNumber;
    private String investmentSeq;
    private String userDetailsSeq;
    private String userId;
    private String name;
    private String mobile;
    private String bankName;
    private String accountNumber;
    private int amount;
    private String depositedAt;
    private int status;

}
