package com.muzisoft.division.domain.point;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentCondition;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.InvestmentSearchType;
import com.muzisoft.division.web.api.dto.users.investment.UserInvestmentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface InvestmentQueryRepository {

    Page<InvestmentListResponse> investmentList(Pageable pageable, InvestmentCondition investmentCondition, CommonCondition condition);

    Page<InvestmentListPerUserResponse> investmentListPerUser(Pageable pageable, String userDetailsSeq);

    // 사용자
    Page<UserInvestmentListResponse> userInvestment(Pageable pageable, CommonCondition condition, UserDetails userDetails);
}