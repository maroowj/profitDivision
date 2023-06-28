package com.muzisoft.division.web.api.dto.admin.withdrawal;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalDisposalRequest {

//    private String withdrawalSeq;
//    private long amount;

    private String comment;

    private int accepted;  // 승인 , 거절

}
