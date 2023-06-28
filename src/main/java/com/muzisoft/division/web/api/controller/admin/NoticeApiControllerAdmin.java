package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.notice.*;
import com.muzisoft.division.web.api.dto.admin.popup.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.NoticeServiceAdmin;
import com.muzisoft.division.web.service.admin.PopupService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class NoticeApiControllerAdmin {

    private final NoticeServiceAdmin noticeServiceAdmin;
    private final FileHandler fileHandler;

    /** 공지 등록 **/
    @PostMapping
    public void createNotice(NoticeSaveRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "notice-file"));
        });

        try {
            noticeServiceAdmin.createNotice(request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 목록 **/
    @GetMapping
    public Page<NoticeListResponse> noticeList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, CommonCondition condition) {
        return noticeServiceAdmin.noticeList(pageable, condition);
    }

    /** 공지 상세 **/
    @GetMapping("/{noticeSeq}")
    public NoticeDetailsResponse noticeDetails(@PathVariable("noticeSeq") String noticeSeq) {
        return noticeServiceAdmin.noticeDetails(noticeSeq);
    }

    /** 공지 수정 **/
    @PutMapping("/{noticeSeq}")
    public void updateNotice(@PathVariable("noticeSeq") String noticeSeq, NoticeUpdateRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "notice-file"));
        });

        try {
            noticeServiceAdmin.updateNotice(noticeSeq, request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 삭제 **/
    @DeleteMapping("/delete")
    public void deleteNotice(@RequestBody NoticeDeleteRequest request) {
        noticeServiceAdmin.deleteNotice(request);
    }
}
