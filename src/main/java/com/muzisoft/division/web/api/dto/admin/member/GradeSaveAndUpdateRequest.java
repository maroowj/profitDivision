package com.muzisoft.division.web.api.dto.admin.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GradeSaveAndUpdateRequest {

    private String title;
    private float percent;
    private String comment;
}
