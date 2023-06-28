package com.muzisoft.division.domain.board;

import com.muzisoft.division.web.api.dto.admin.popup.PopupCondition;
import com.muzisoft.division.web.api.dto.admin.popup.PopupListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.PopupExposureType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PopupQueryRepository {

    Page<PopupListResponse> popupList(Pageable pageable, PopupCondition popupCondition, CommonCondition condition);
}
