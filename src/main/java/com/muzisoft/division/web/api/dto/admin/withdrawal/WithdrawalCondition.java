package com.muzisoft.division.web.api.dto.admin.withdrawal;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheck;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalSearchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalCondition {

    @Enum(enumClass = WithdrawalStatusCheck.class, ignoreCase = true, message = "전체, 대기, 승인, 거절 중에 하나이어야 합니다.")
    private String withdrawalStatus;

    public WithdrawalStatusCheck getWithdrawalStatusCheck() {
        return WithdrawalStatusCheck.find(withdrawalStatus);
    }

    @NotEmpty
    @Enum(enumClass = WithdrawalSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디, 회원번호 중에 하나이어야 합니다.")
    private String queryType;

    public WithdrawalSearchType getQueryType() {
        return WithdrawalSearchType.find(queryType);
    }
}
