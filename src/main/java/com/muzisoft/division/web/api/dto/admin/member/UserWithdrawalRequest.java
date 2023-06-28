package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithdrawalRequest {

    private String withdrawalReason;
}
