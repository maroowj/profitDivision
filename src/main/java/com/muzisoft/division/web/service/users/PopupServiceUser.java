package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.board.Popup;
import com.muzisoft.division.domain.board.PopupRepository;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.users.popup.PopupListResponseForUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopupServiceUser {

    private final PopupRepository popupRepository;
    private final FileHandler fileHandler;

    public List<PopupListResponseForUser> popupListResponseForUser() {
        List<PopupListResponseForUser> result = new ArrayList<>();
        List<Popup> popupList = popupRepository.findAllByExposureOrderByCreatedAtDesc(true);

        for(Popup popup : popupList) {
            String imgUrl = null;
            if(!ObjectUtils.isEmpty(popup.getPopupImage())) {
                imgUrl = fileHandler.fileUrl(popup.getPopupImage());
            }

            PopupListResponseForUser response = new PopupListResponseForUser(popup, imgUrl);
            result.add(response);
        }

        return result;
    }
}
