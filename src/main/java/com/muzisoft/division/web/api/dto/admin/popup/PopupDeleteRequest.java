package com.muzisoft.division.web.api.dto.admin.popup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopupDeleteRequest {

    private List<String> popupSeq;
}
