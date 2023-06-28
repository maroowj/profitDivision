package com.muzisoft.division.web.api.dto.users.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalSaveRequest {

    private String bankName;
    private String accountNumber;
    private long amount;
}
