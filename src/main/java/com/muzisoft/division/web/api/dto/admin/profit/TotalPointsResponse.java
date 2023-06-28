package com.muzisoft.division.web.api.dto.admin.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalPointsResponse {

    private int savedAmount;
    private int usedAmount;
}
