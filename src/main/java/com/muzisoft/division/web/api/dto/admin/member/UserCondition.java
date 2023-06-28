package com.muzisoft.division.web.api.dto.admin.member;

import com.muzisoft.division.utils.validation.Enum;
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
public class UserCondition {

    @NotEmpty
    @Enum(enumClass = UserSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디 중에 하나이어야 합니다.")
    private String queryType;

    public UserSearchType getQueryType() {
        return UserSearchType.find(queryType);
    }

    @NotEmpty
    @Enum(enumClass = UserWithdrawalType.class, ignoreCase = true, message = "전체, 가입, 탈퇴 중에 하나이어야 합니다.")
    private String withdrawal;

    public UserWithdrawalType getWithdrawal() {
        return UserWithdrawalType.find(withdrawal);
    }
}
