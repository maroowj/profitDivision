package com.muzisoft.division.domain.board;

import com.muzisoft.division.web.api.dto.admin.board.BoardListResponse;
import com.muzisoft.division.web.api.dto.admin.notice.NoticeListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardListForUserResponse;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardQueryRepository {

    Page<BoardListResponse> boardList(Pageable pageable, CommonCondition condition);


    List<BoardListForUserResponse.NoticeList> noticeListForUser();
    Page<BoardListForUserResponse.NormalList> normalListForUser(Pageable pageable, BoardCondition boardCondition, CommonCondition condition);
}