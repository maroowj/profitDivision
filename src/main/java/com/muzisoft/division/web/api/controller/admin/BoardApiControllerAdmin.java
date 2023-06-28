package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.board.*;
import com.muzisoft.division.web.api.dto.admin.notice.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.BoardServiceAdmin;
import com.muzisoft.division.web.service.admin.NoticeServiceAdmin;
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
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class BoardApiControllerAdmin {

    private final BoardServiceAdmin boardServiceAdmin;
    private final FileHandler fileHandler;

    /** 공지 등록 **/
    @PostMapping
    public void createBoard(BoardSaveRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "board-file"));
        });

        try {
            boardServiceAdmin.createBoard(request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 목록 **/
    @GetMapping
    public Page<BoardListResponse> boardList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, CommonCondition condition) {
        return boardServiceAdmin.boardList(pageable, condition);
    }

    /** 공지 상세 **/
    @GetMapping("/{boardSeq}")
    public BoardDetailsResponse boardDetails(@PathVariable("boardSeq") String boardSeq) {
        return boardServiceAdmin.boardDetails(boardSeq);
    }

    /** 공지 수정 **/
    @PutMapping("/{boardSeq}")
    public void updateNotice(@PathVariable("boardSeq") String boardSeq, BoardUpdateRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getAttach()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "board-file"));
        });

        try {
            boardServiceAdmin.updateBoard(boardSeq, request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 공지 삭제 **/
    @DeleteMapping("/delete")
    public void deleteNotice(@RequestBody BoardDeleteRequest request) {
        boardServiceAdmin.deleteBoard(request);
    }
}
