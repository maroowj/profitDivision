package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserWithdrawalType {
    WHOLE("전체"),
    REGISTER("가입"),
    WITHDRAWAL("탈퇴");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static UserWithdrawalType find(String searchType) {
        return Arrays.stream(values()).filter((withdrawalType) -> withdrawalType.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
