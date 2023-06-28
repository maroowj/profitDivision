package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitRequest;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitDetailsPerUser;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfitDetailsQueryRepository {

    Page<ProfitListResponse> profitList(Pageable pageable, ProfitCondition profitCondition, CommonCondition condition, Profit profit);
    Page<ProfitDetailsPerUser> profitDetailsPerUser(Pageable pageable, CommonCondition condition, UserDetails userDetails);
}
