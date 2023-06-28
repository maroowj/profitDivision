package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.Popup;
import com.muzisoft.division.domain.board.PopupRepository;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.popup.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRepository popupRepository;
    private final FileHandler fileHandler;

    @Transactional
    public Page<PopupListResponse> popupList(Pageable pageable, PopupCondition popupCondition, CommonCondition condition) {
        return popupRepository.popupList(pageable, popupCondition, condition);
    }

    @Transactional
    public void createPopup(PopupSaveRequest request, EFileManager popupImage) {
        popupRepository.save(
                Popup.create(
                        request.getMainTitle(),
                        request.getWidth(),
                        request.getHeight(),
                        request.getX(),
                        request.getY(),
                        request.getPopupTitle(),
                        request.getPopupContents(),
                        request.getLink(),
                        request.getExposure(),
                        request.getClosingOption(),
                        request.getExposureStart(),
                        request.getExposureEnd(),
                        popupImage
                )
        );
    }

    @Transactional
    public PopupDetailsResponse popupDetails(String popupSeq) {
        Popup popup = EntityUtils.popupThrowable(popupRepository, popupSeq);
        String imgUrl = null;
        if(!ObjectUtils.isEmpty(popup.getPopupImage())) {
            imgUrl = fileHandler.fileUrl(popup.getPopupImage());
        }
        return new PopupDetailsResponse(popup, imgUrl);
    }

    @Transactional
    public void updatePopup(String popupSeq, PopupUpdateRequest request, EFileManager popupImage) {
        Popup foundPopup = EntityUtils.popupThrowable(popupRepository, popupSeq);

        foundPopup.update(
                request.getMainTitle(),
                request.getWidth(),
                request.getHeight(),
                request.getX(),
                request.getY(),
                request.getPopupTitle(),
                request.getPopupContents(),
                request.getLink(),
                request.getExposure(),
                request.getClosingOption(),
                request.getExposureStart(),
                request.getExposureEnd(),
                popupImage
        );
    }

    @Transactional
    public void deletePopup(PopupDeleteRequest request) {
        for(String popupSeq : request.getPopupSeq()) {
            Popup foundPopup = EntityUtils.popupThrowable(popupRepository, popupSeq);
            popupRepository.delete(foundPopup);
        }
    }

    @Transactional
    public void updateExposure(String popupSeq, PopupUpdateRequest request) {
        Popup foundPopup = EntityUtils.popupThrowable(popupRepository, popupSeq);
        foundPopup.updateExposure(request.getExposure());
    }
}
