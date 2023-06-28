package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NoticeSearchType {

    WHOLE("전체"),
    TITLE("제목"),
    CONTENTS("내용");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static NoticeSearchType find(String searchType) {
        return Arrays.stream(values()).filter((noticeSearchType) -> noticeSearchType.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
