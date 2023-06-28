package com.muzisoft.division.web.api.dto.admin.profit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpectedProfitRequest {

    private int year;
    private int month;
}
