package com.muzisoft.division.web.api.dto.users.popup;

import com.muzisoft.division.domain.board.Popup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupListResponseForUser {

    private String seq;
    private String mainTitle;
    private String popupTitle;
    private String popupContents;
    private String link;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean exposure;
    private String ExposureStart;
    private String ExposureEnd;
    private boolean closingOption;
    private String popupImageUrl;
    private String popupImageOriginalName;

    public PopupListResponseForUser(Popup popup, String imgUrl) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.setSeq(popup.getSeq());
        this.setMainTitle(popup.getMainTitle());
        this.setPopupTitle(popup.getPopupTitle());
        this.setPopupContents(popup.getPopupContents());
        this.setLink(popup.getLink());
        this.setX(popup.getLocation().getX());
        this.setY(popup.getLocation().getY());
        this.setWidth(popup.getSize().getWidth());
        this.setHeight(popup.getSize().getHeight());
        this.setExposure(popup.isExposure());
        if(popup.getExposureStart()!=null) {
            this.setExposureStart(sdf.format(popup.getExposureStart()));
        }
        if(popup.getExposureEnd()!=null) {
            this.setExposureEnd(sdf.format(popup.getExposureEnd()));
        }
        this.setClosingOption(popup.isClosingOption());
        this.setPopupImageUrl(imgUrl);
        if(popup.getPopupImage()!=null && !popup.getPopupImage().equals("")) {
            this.setPopupImageOriginalName(popup.getPopupImage().getOriginalName());
        }
    }
}
