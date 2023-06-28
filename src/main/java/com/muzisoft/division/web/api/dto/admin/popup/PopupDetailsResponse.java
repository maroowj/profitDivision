package com.muzisoft.division.web.api.dto.admin.popup;

import com.muzisoft.division.domain.board.Popup;
import com.muzisoft.division.domain.board.PopupLocationDTO;
import com.muzisoft.division.domain.board.PopupSizeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupDetailsResponse {

    private String popupSeq;
    private String mainTitle;
    private PopupSizeDTO size;
    private PopupLocationDTO location;
    private String popupTitle;
    private String popupContents;
    private boolean closingOption;
    private String link;
    private String popupImageUrl;
    private String popupImageOriginalName;
    private boolean exposure;
    private String ExposureStart;
    private String ExposureEnd;


    public PopupDetailsResponse(Popup popup, String imgUrl) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.setPopupSeq(popup.getSeq());
        this.setExposure(popup.isExposure());
        this.setMainTitle(popup.getMainTitle());
        this.setSize(popup.getSize());
        this.setLocation(popup.getLocation());
        this.setPopupTitle(popup.getPopupTitle());
        this.setPopupContents(popup.getPopupContents());
        this.setClosingOption(popup.isClosingOption());
        this.setLink(popup.getLink());
        if(popup.getPopupImage()!=null && !popup.getPopupImage().equals("")) {
            this.setPopupImageOriginalName(popup.getPopupImage().getOriginalName());
        }
        if(popup.getExposureStart()!=null) {
            this.setExposureStart(sdf.format(popup.getExposureStart()));
        }
        if(popup.getExposureEnd()!=null) {
            this.setExposureEnd(sdf.format(popup.getExposureEnd()));
        }
        this.setPopupImageUrl(imgUrl);
    }

}
