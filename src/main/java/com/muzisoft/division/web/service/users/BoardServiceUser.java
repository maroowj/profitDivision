package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.board.Board;
import com.muzisoft.division.domain.board.BoardRepository;
import com.muzisoft.division.domain.board.Notice;
import com.muzisoft.division.domain.board.NoticeRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.board.BoardListForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeDetailsForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceUser {

    private final BoardRepository boardRepository;
    private final FileHandler fileHandler;

    @Transactional
    public BoardListForUserResponse boardListForUser(Pageable pageable, BoardCondition boardCondition, CommonCondition condition) {
        BoardListForUserResponse result = new BoardListForUserResponse();


        List<BoardListForUserResponse.NoticeList> noticeList = boardRepository.noticeListForUser();
        Page<BoardListForUserResponse.NormalList> normalList = boardRepository.normalListForUser(pageable, boardCondition, condition);

        result.setNoticeList(noticeList);
        result.setNormalList(normalList);

        return result;
    }

    @Transactional
    public BoardDetailsForUserResponse boardDetailsForUser(String boardSeq) {
        Board board = EntityUtils.boardThrowable(boardRepository, boardSeq);

        String attachUrl = null;
        if(!ObjectUtils.isEmpty(board.getAttach())) {
            attachUrl = fileHandler.fileUrl(board.getAttach());
        }

        return new BoardDetailsForUserResponse(board, attachUrl);
    }
}
