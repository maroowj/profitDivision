package com.muzisoft.division.web.api.dto.admin.profit;

import com.muzisoft.division.domain.profit.ExpectedProfit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyExpectedProfitsResponse {

    private String createdAt;
    private long amount;

/*    public MonthlyExpectedProfitsResponse(ExpectedProfit expectedProfit) {
        this.setYear(expectedProfit.getReferenceYear());
        this.setMonth(expectedProfit.getReferenceMonth());
        this.setTotalProfits(expectedProfit.getAmount());
    }*/
}
