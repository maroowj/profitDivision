package com.muzisoft.division.domain.profit;

import com.muzisoft.division.web.api.dto.admin.profit.MonthlyExpectedProfitsResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpectedProfitQueryRepository {
    Page<MonthlyExpectedProfitsResponse> monthlyExpectedProfitList(Pageable pageable, CommonCondition condition);
}