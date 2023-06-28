package com.muzisoft.division.domain.board;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.manager.SeqManager;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.security.core.parameters.P;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Popup extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_manager_popup")
    @GenericGenerator(name = "seq_manager_popup", strategy = "com.muzisoft.division.utils.manager.SeqManager", parameters = {
            @org.hibernate.annotations.Parameter(name = SeqManager.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_USER_SEQ_PARAMETER, value = "popu_"),
            @org.hibernate.annotations.Parameter(name = SeqManager.NUMBER_FORMAT_PARAMETER, value = "%010d"),
            @org.hibernate.annotations.Parameter(name = SeqManager.VALUE_COLUMN_PARAM, value = "seq")
    })
    @Column(nullable = false, updatable = false, length = 15)
    @Id
    private String seq;

    @Column(nullable = false)
    private String mainTitle;

    @Column
    private String popupTitle;

    @Column(columnDefinition = "text")
    private String popupContents;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "popupImage")
    private EFileManager popupImage;

    @Type(type = "json")
    @Column(columnDefinition = "JSON")
    private PopupSizeDTO size;

    @Type(type = "json")
    @Column(columnDefinition = "JSON")
    private PopupLocationDTO location;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    private boolean closingOption;

    @Column
    private String link;

    @Column
    private Date exposureStart;

    @Column
    private Date exposureEnd;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean exposure;

    public static Popup create(String mainTitle, int width, int height, int x, int y, String popupTitle, String popupContents,
                               String link, int exposure, int closingOption, Date exposureStart, Date exposureEnd, EFileManager popupImage) {
        Popup popup = new Popup();
        popup.setMainTitle(mainTitle);

        PopupSizeDTO size = new PopupSizeDTO();
        size.setWidth(width);
        size.setHeight(height);
        popup.setSize(size);

        PopupLocationDTO location = new PopupLocationDTO();
        location.setX(x);
        location.setY(y);
        popup.setLocation(location);

        popup.setPopupTitle(popupTitle);
        popup.setPopupContents(popupContents);
        popup.setLink(link);
        if(exposure==0) {
            popup.setExposure(false);
        }else {
            popup.setExposure(true);
        }
        if(closingOption==0) {
            popup.setClosingOption(false);
        }else {
            popup.setClosingOption(true);
        }
        popup.setExposureStart(exposureStart);
        popup.setExposureEnd(exposureEnd);
        popup.setPopupImage(popupImage);
        return popup;
    }

    public void update(String mainTitle, int width, int height, int x, int y, String popupTitle, String popupContents,
                       String link, int exposure, int closingOption, Date exposureStart, Date exposureEnd, EFileManager popupImage) {
        if(!FileManagerUtils.equals(getPopupImage(), popupImage)) {
            if(popupImage!=null) {
                setPopupImage(popupImage);
            }else {
                if(popupTitle!=null && !popupTitle.equals("")) {
                    setPopupImage(null);
                }
            }
        }else {
            FileManagerUtils.delete(popupImage);
        }

        setMainTitle(mainTitle);
        PopupSizeDTO sizeDTO = new PopupSizeDTO();
        sizeDTO.setWidth(width);
        sizeDTO.setHeight(height);
        setSize(sizeDTO);
        PopupLocationDTO locationDTO = new PopupLocationDTO();
        locationDTO.setX(x);
        locationDTO.setY(y);
        setLocation(locationDTO);
        setPopupTitle(popupTitle);
        setPopupContents(popupContents);
        setLink(link);
        if(exposure==0) {
            setExposure(false);
        }else {
            setExposure(true);
        }
        if(closingOption==0) {
            setClosingOption(false);
        }else {
            setClosingOption(true);
        }
        setExposureStart(exposureStart);
        setExposureEnd(exposureEnd);
    }

    public void updateExposure(int exposure) {
        if(exposure==0) {
            setExposure(false);
        }else {
            setExposure(true);
        }
    }
}
