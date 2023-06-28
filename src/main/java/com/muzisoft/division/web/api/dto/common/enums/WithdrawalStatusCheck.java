package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WithdrawalStatusCheck {

    WHOLE("전체"),
    WAIT("대기"),
    ACCEPT("승인"),
    REFUSE("거절");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static WithdrawalStatusCheck find(String status) {
        return Arrays.stream(values()).filter((withdrawalStatusCheck) -> withdrawalStatusCheck.value.equals(status)).findAny().orElseThrow(RuntimeException::new);
    }
}
