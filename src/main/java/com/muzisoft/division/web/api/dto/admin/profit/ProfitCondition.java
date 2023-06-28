package com.muzisoft.division.web.api.dto.admin.profit;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.DuesSearchType;
import com.muzisoft.division.web.api.dto.common.enums.ProfitSearchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfitCondition {

    @NotEmpty
    @Enum(enumClass = ProfitSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디, 회원번호 중에 하나이어야 합니다.")
    private String queryType;

    public ProfitSearchType getQueryType() {
        return ProfitSearchType.find(queryType);
    }
}
