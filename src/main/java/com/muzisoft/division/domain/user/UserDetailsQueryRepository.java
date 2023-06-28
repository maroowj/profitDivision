package com.muzisoft.division.domain.user;

import com.muzisoft.division.domain.dues.DuesMonth;
import com.muzisoft.division.web.api.dto.admin.member.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDetailsQueryRepository {

    Page<UserListResponse.UserList> userList(Pageable pageable, UserCondition userCondition, CommonCondition condition);

    int userCount();

    int countByGrade(Grade grade);

    UserDetailsResponseInAdmin userDetailsInAdmin(String userDetailsSeq);

    Page<WaitingListResponse> waitingList(Pageable pageable, CommonCondition cond);

    WaitingUserDetailsResponse.User waitingUserDetails(String userDetailsSeq);
    WaitingUserDetailsResponse.Recommender waitingUserRecommenderDetails(String userDetailsSeq);

    // 탈퇴하지 않고, 가입이 승인된 회원목록
    List<UserDetails> userDetailsList();
}
