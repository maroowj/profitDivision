package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum InvestmentSearchType {
    WHOLE("전체"),
    MEMBERSHIP_NUMBER("회원번호"),
    USER_ID("아이디"),
    USER_NAME("이름");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static InvestmentSearchType find(String searchType) {
        return Arrays.stream(values()).filter((investmentSearchType) -> investmentSearchType.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
