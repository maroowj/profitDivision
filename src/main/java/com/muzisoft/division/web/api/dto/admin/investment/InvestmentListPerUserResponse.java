package com.muzisoft.division.web.api.dto.admin.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentListPerUserResponse {

    private String investmentSeq;
    private String depositedAt;
    private long total;
    private int amount;
    private int status;
}
