package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DuesPaidCheck {

    WHOLE("전체"),
    WAIT("확인대기"),
    PAID("납부"),
    UNPAID("미납");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static DuesPaidCheck find(String searchType) {
        return Arrays.stream(values()).filter((duesPaidCheck) -> duesPaidCheck.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
