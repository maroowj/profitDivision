package com.muzisoft.division.domain.profit;

import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.profit.ProfitLogListResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitTypeCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitDetailsLogQueryRepository {

    Page<ProfitLogListResponse> profitLogList(Pageable pageable, ProfitTypeCondition profitTypeCondition, CommonCondition condition, UserDetails userDetails);
}
