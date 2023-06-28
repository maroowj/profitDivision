package com.muzisoft.division.web.api.dto.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PopupExposureType {

    WHOLE("전체"),
    SHOW("노출"),
    HIDE("미노출");

    private String value;

    @Override
    public String toString() {
        return value;
    }

    public static PopupExposureType find(String searchType) {
        return Arrays.stream(values()).filter((popupExposureType) -> popupExposureType.value.equals(searchType)).findAny().orElseThrow(RuntimeException::new);
    }
}
