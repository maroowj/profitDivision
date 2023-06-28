package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.board.BoardListForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import com.muzisoft.division.web.service.users.BoardServiceUser;
import com.muzisoft.division.web.service.users.NoticeServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/common/board")
@RequiredArgsConstructor
public class BoardApiControllerUser {

    private final BoardServiceUser boardServiceUser;

    @GetMapping
    public BoardListForUserResponse boardListForUser(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20)Pageable pageable,
                                                     BoardCondition boardCondition, CommonCondition condition) {
        return boardServiceUser.boardListForUser(pageable, boardCondition, condition);
    }

    @GetMapping("/{boardSeq}")
    public BoardDetailsForUserResponse noticeDetailsForUser(@PathVariable("boardSeq") String boardSeq) {
        return boardServiceUser.boardDetailsForUser(boardSeq);
    }
}
