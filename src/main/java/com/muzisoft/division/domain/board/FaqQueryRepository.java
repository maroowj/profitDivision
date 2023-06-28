package com.muzisoft.division.domain.board;

import com.muzisoft.division.web.api.dto.admin.board.BoardListResponse;
import com.muzisoft.division.web.api.dto.admin.faq.FaqListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardCondition;
import com.muzisoft.division.web.api.dto.users.board.BoardListForUserResponse;
import com.muzisoft.division.web.api.dto.users.faq.FaqListResponseForUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqQueryRepository {

    Page<FaqListResponse> faqList(Pageable pageable, CommonCondition condition);

    Page<FaqListResponseForUser> faqListForUser(Pageable pageable, CommonCondition condition);
}