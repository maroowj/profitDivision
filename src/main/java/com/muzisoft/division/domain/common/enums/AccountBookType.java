package com.muzisoft.division.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AccountBookType {

    DEPOSIT(0, "수익"),
    WITHDRAWAL(1, "지출"),
    ;

    private int value;
    private String memo;

    @Override
    public String toString() {
        return memo;
    }

    public static AccountBookType find(int value) {
        return Arrays.stream(values()).filter((accountBookType) -> accountBookType.value == value).findAny().orElseThrow(RuntimeException::new);
    }
}
