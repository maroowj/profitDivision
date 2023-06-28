package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.board.*;
import com.muzisoft.division.web.api.dto.admin.faq.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.BoardServiceAdmin;
import com.muzisoft.division.web.service.admin.FaqServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin/faq")
@RequiredArgsConstructor
public class FaqApiControllerAdmin {

    private final FaqServiceAdmin faqServiceAdmin;
    private final FileHandler fileHandler;

    /** 공지 등록 **/
    @PostMapping
    public void createFaq(FaqSaveRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "board-file"));
        });

        try {
            faqServiceAdmin.createFaq(request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 목록 **/
    @GetMapping
    public Page<FaqListResponse> faqList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, CommonCondition condition) {
        return faqServiceAdmin.faqList(pageable, condition);
    }

    /** 공지 상세 **/
    @GetMapping("/{faqSeq}")
    public FaqDetailsResponse faqDetails(@PathVariable("faqSeq") String faqSeq) {
        return faqServiceAdmin.faqDetails(faqSeq);
    }

    /** 공지 수정 **/
    @PutMapping("/{faqSeq}")
    public void updateNotice(@PathVariable("faqSeq") String faqSeq, FaqUpdateRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "board-file"));
        });

        try {
            faqServiceAdmin.updateFaq(faqSeq, request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 삭제 **/
    @DeleteMapping("/delete")
    public void deleteNotice(@RequestBody FaqDeleteRequest request) {
        faqServiceAdmin.deleteFaq(request);
    }
}
