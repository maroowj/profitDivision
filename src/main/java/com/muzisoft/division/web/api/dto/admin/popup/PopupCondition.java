package com.muzisoft.division.web.api.dto.admin.popup;

import com.muzisoft.division.utils.validation.Enum;
import com.muzisoft.division.web.api.dto.common.enums.PopupExposureType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupCondition {

    @NotEmpty
    @Enum(enumClass = PopupExposureType.class, message = "전체, 노출, 미노출 중에 하나이어야 합니다.")
    private String exposure;

    public PopupExposureType getExposure() {
        return PopupExposureType.find(exposure);
    }
}
