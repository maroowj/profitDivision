package com.muzisoft.division.domain.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PointSupplier {

    INVESTMENT(2, "투자금"),
    DONATION(3, "후원금"),
    PROCEEDS(0, "수익금"),
    INTEREST(1, "이자");
//    Referral(0, "추천인포인트"),


    private int value;
    private String memo;

    @Override
    public String toString() {
        return memo;
    }

    public static PointSupplier find(int value) {
        return Arrays.stream(values()).filter((pointSupplier) -> pointSupplier.value == value).findAny().orElseThrow(RuntimeException::new);
    }
}
