package com.muzisoft.division.web.api.dto.users.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitSummaryResponse {

    private long total;
    private long monthlyAmount;
    private long myMonthlyAmount;
    private long weeklyAmount;
    private long myWeeklyAmount;
    private long dailyAmount;
    private long myDailyAmount;
}
