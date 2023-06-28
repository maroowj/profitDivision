package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NoticeSearchFilter {

    WHOLE("전체"),
    NOTICE("공지"),
    NORMAL("일반");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static NoticeSearchFilter find(String searchType) {
        return Arrays.stream(values()).filter((noticeSearchFilter) -> noticeSearchFilter.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
