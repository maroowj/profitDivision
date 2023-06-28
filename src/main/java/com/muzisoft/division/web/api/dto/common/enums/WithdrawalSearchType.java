package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WithdrawalSearchType {

    WHOLE("전체"),
    USER_NAME("이름"),
    USER_ID("아이디"),
    MEMBERSHIP_NUMBER("회원번호");

    private String value;
//    private String memo;

    @Override
    public String toString() {
        return value;
    }

    public static WithdrawalSearchType find(String value) {
        return Arrays.stream(values()).filter((withdrawalSearchType) -> withdrawalSearchType.value.equals(value)).findAny().orElseThrow(RuntimeException::new);
    }
}
