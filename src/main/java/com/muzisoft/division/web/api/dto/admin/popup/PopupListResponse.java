package com.muzisoft.division.web.api.dto.admin.popup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupListResponse {

    private String popupSeq;
    private String mainTitle;
    private String exposureStart;
    private String exposureEnd;
    private String createdAt;
    private boolean exposure;
}
