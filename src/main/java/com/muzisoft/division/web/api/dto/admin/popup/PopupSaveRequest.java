package com.muzisoft.division.web.api.dto.admin.popup;

import com.muzisoft.division.domain.board.PopupSizeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupSaveRequest {

    MultipartFile popupImage;

    private String mainTitle;
    private int width;
    private int height;
    private int x;
    private int y;
    private String popupTitle;
    private String popupContents;
    private int exposure; // 0 또는 1
    private int closingOption; // 0 또는 1
    private String link;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exposureStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exposureEnd;
}
