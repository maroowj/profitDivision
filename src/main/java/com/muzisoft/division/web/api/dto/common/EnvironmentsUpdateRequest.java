package com.muzisoft.division.web.api.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentsUpdateRequest {

    private int duesDate;
    private int duesAmount;
    private float interestRate;
    private int conversionRate;
    private int recommenderDividendRate;
    private int cuttingRate;
    private long totalBalance;
}
