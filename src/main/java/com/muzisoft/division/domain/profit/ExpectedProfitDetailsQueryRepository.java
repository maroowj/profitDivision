package com.muzisoft.division.domain.profit;

import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.ExpectedProfitRequest;
import com.muzisoft.division.web.api.dto.admin.profit.ProfitCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpectedProfitDetailsQueryRepository {

    Page<ExpectedProfitListResponse> expectedProfitList(Pageable pageable, ExpectedProfitRequest request, ProfitCondition profitCondition, CommonCondition condition, ExpectedProfit expectedProfit);
}
