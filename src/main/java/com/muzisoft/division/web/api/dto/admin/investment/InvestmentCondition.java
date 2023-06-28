package com.muzisoft.division.web.api.dto.admin.investment;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.InvestmentSearchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentCondition {

    @NotEmpty
    @Enum(enumClass = InvestmentSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디, 회원번호 중에 하나이어야 합니다.")
    private String queryType;

    public InvestmentSearchType getQueryType() {
        return InvestmentSearchType.find(queryType);
    }
}
