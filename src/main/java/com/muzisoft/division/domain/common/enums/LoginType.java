package com.muzisoft.division.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoginType {

    NORMAL("n"),
    KAKAO("k")
    ;

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static LoginType find(String value) {
        return Arrays.stream(values()).filter((loginType) -> loginType.value.equals(value)).findAny().orElseThrow(RuntimeException::new);
    }
}
