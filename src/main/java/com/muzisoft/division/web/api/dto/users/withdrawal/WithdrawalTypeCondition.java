package com.muzisoft.division.web.api.dto.users.withdrawal;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.ProfitStatusFilter;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheckUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalTypeCondition {

    @NotEmpty
    @Enum(enumClass = WithdrawalStatusCheckUser.class, ignoreCase = true, message = "전체, 승인, 미승인 중에 하나이어야 합니다.")
    private String type;

    public WithdrawalStatusCheckUser getType() {
        return WithdrawalStatusCheckUser.find(type);
    }
}
