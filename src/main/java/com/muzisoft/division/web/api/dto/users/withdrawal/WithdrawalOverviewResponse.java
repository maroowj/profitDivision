package com.muzisoft.division.web.api.dto.users.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalOverviewResponse {

    private String name;
    private String bankName;
    private String accountNumber;
    private long total;
    private long available;
    private long amountPerYear;
}
