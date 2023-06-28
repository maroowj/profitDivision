package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.popup.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.PopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin/popup")
@RequiredArgsConstructor
public class PopupApiController {

    private final PopupService popupService;
    private final FileHandler fileHandler;

    /** 팝업 목록 (페이징) **/
    @GetMapping
    public Page<PopupListResponse> popupList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                             PopupCondition popupCondition, CommonCondition condition) {
        return popupService.popupList(pageable, popupCondition, condition);
    }

    /** 팝업 등록 **/
    @PostMapping
    public void createPopup(PopupSaveRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getPopupImage()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "popup-img"));
        });

        try {
            popupService.createPopup(request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }
    
    /** 팝업 상세 **/
    @GetMapping("/{popupSeq}")
    public PopupDetailsResponse popupDetails(@PathVariable("popupSeq") String popupSeq) {
        return popupService.popupDetails(popupSeq);
    }

    /** 팝업 수정 **/
    @PutMapping("/{popupSeq}")
    public void updatePopup(@PathVariable("popupSeq") String popupSeq, PopupUpdateRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getPopupImage()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "popup-img"));
        });

        try {
            popupService.updatePopup(popupSeq, request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 팝업 삭제 **/
    @DeleteMapping("/delete")
    public void deletePopup(@RequestBody PopupDeleteRequest request) {
        popupService.deletePopup(request);
    }

    /** 팝업 노출 수정 **/
    @PutMapping("/exposure/{popupSeq}")
    public void updateExposure(@PathVariable("popupSeq") String popupSeq, @RequestBody PopupUpdateRequest request) {
        popupService.updateExposure(popupSeq,request);
    }
}
