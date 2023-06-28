package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProfitStatusFilter {

    WHOLE("전체"),
    DEPOSIT("수익"),
    WITHDRAWAL("출금");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static ProfitStatusFilter find(String searchType) {
        return Arrays.stream(values()).filter((profitStatusFilter) -> profitStatusFilter.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
