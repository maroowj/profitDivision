package com.muzisoft.division.web.api.dto.admin.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitListResponse {

    private String userDetailsSeq;
    private String membershipNumber;
    private String name;
    private String userId;
    private int userLevel;
    private long total;
    private int amount;
}
