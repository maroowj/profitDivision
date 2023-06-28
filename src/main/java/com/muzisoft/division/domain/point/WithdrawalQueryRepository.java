package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalCondition;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.withdrawal.UserWithdrawalListResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalTypeCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WithdrawalQueryRepository {

    Page<WithdrawalListResponse> withdrawalList(Pageable pageable, WithdrawalCondition withdrawalCondition, CommonCondition condition);
    Page<WithdrawalListPerUserResponse> withdrawalListPerUser(Pageable pageable, UserDetails userDetails);

    // 사용자
    Page<UserWithdrawalListResponse> userWithdrawalList(Pageable pageable, WithdrawalTypeCondition withdrawalTypeCondition, CommonCondition condition, UserDetails userDetails);

    List<Withdrawal> amountPerYear(UserDetails userDetails, Date firstDay);
}