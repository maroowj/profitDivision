package com.muzisoft.division.web.api.dto.admin.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentApprovalRequest {

    private int status;
    private int amount;
    private String comment;
}
