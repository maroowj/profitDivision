package com.muzisoft.division.web.api.dto.common;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.BasicSearchType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicCondition {

    @NotEmpty
    @Enum(enumClass = BasicSearchType.class, ignoreCase = true, message = "전체, 이름, 아이디, 회원번호 중에 하나이어야 합니다.")
    private String queryType;

    public BasicSearchType getQueryType() {
        return BasicSearchType.find(queryType);
    }
}
