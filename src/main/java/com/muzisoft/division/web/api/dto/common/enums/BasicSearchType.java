package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BasicSearchType {

    WHOLE("전체"),
    USER_ID("아이디"),
    USER_NAME("이름"),
    MEMBERSHIP_NUMBER("회원번호");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static BasicSearchType find(String searchType) {
        return Arrays.stream(values()).filter((basicSearchType) -> basicSearchType.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
