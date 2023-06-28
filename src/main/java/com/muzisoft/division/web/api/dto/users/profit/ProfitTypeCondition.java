package com.muzisoft.division.web.api.dto.users.profit;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitStatusFilter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfitTypeCondition {

    @NotEmpty
    @Enum(enumClass = ProfitStatusFilter.class, ignoreCase = true, message = "전체, 수익, 출금 중에 하나이어야 합니다.")
    private String type;

    public ProfitStatusFilter getType() {
        return ProfitStatusFilter.find(type);
    }
}
