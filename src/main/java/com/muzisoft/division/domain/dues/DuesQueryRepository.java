package com.muzisoft.division.domain.dues;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.dues.DuesCondition;
import com.muzisoft.division.web.api.dto.admin.dues.DuesListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.DuesPaidCheck;
import com.muzisoft.division.web.api.dto.users.dues.UserDuesListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DuesQueryRepository {

    // 회비가 발생한 전체 인원 수
    int duesTotalCount(DuesMonth duesMonth);
    // 납부 인원
    int duesPaidCount(DuesMonth duesMonth);
    // 미납 인원
    int duesUnpaidCount(DuesMonth duesMonth);
    // (월별) 전체 회비 리스트
    Page<DuesListResponse.DuesList> duesList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition, DuesMonth duesMonth);
    // 회비납부 확인 요청 카운트
    int duesWaitingCount();
    // 회비납부 확인 요청 리스트
    Page<DuesListResponse.DuesList> duesWaitingList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition);
    // 회원의 회비 납부 내역
    Page<UserDuesListResponse> userDuesList(Pageable pageable, CommonCondition condition, UserDetails userDetails);
}
