package com.muzisoft.division.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProfitLogType {

    DEPOSIT(0, "입금"),
    WITHDRAWAL(1, "출금"),
    ;

    private int value;
    private String memo;

    @Override
    public String toString() {
        return memo;
    }

    public static ProfitLogType find(int value) {
        return Arrays.stream(values()).filter((profitLogType) -> profitLogType.value == value).findAny().orElseThrow(RuntimeException::new);
    }
}
