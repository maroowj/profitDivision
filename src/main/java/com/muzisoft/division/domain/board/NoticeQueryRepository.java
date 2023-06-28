package com.muzisoft.division.domain.board;

import com.muzisoft.division.web.api.dto.admin.notice.NoticeListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeCondition;
import com.muzisoft.division.web.api.dto.users.notice.NoticeListForUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeQueryRepository {

    Page<NoticeListResponse> noticeList(Pageable pageable, CommonCondition condition);


    List<NoticeListForUserResponse.NoticeList> noticeListForUser();
    Page<NoticeListForUserResponse.NormalList> normalListForUser(Pageable pageable, NoticeCondition noticeCondition, CommonCondition condition);
}