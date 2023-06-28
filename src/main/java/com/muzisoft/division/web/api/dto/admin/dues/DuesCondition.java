package com.muzisoft.division.web.api.dto.admin.dues;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.DuesPaidCheck;
import com.muzisoft.division.web.api.dto.common.enums.DuesSearchType;
import com.muzisoft.division.web.api.dto.common.enums.UserSearchType;
import com.muzisoft.division.web.api.dto.common.enums.UserWithdrawalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuesCondition {

    @Enum(enumClass = DuesPaidCheck.class, ignoreCase = true, message = "전체, 미납, 확인대기, 납부 중에 하나이어야 합니다.")
    private String paidStatus;

    public DuesPaidCheck getPaidStatus() {
        return DuesPaidCheck.find(paidStatus);
    }

    @NotEmpty
    @Enum(enumClass = DuesSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디, 회원번호 중에 하나이어야 합니다.")
    private String queryType;

    public DuesSearchType getQueryType() {
        return DuesSearchType.find(queryType);
    }

}
