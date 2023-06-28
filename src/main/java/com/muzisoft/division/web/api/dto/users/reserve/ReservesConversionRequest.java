package com.muzisoft.division.web.api.dto.users.reserve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservesConversionRequest {

    private String bankName;
    private String accountNumber;
    private long amount;
}
