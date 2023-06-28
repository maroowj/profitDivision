package com.muzisoft.division.web.api.dto.users.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveOverviewResponse {

    private String name;
    private String grade;
    private int level;
    private String recommendCode;
    private int reserves;
    private long investment;
    private int conversionRate;
    private String bankName;
    private String accountNumber;
}
