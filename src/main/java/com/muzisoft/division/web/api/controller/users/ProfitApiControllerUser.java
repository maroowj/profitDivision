package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.profit.ProfitLogListResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitSummaryResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitTypeCondition;
import com.muzisoft.division.web.service.users.ProfitServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user/profits")
@RequiredArgsConstructor
public class ProfitApiControllerUser {

    private final ProfitServiceUser profitServiceUser;

    @GetMapping
    public Page<ProfitLogListResponse> userProfitLogList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                                                         ProfitTypeCondition profitTypeCondition, CommonCondition condition) {
        return profitServiceUser.profitLogList(pageable, profitTypeCondition, condition);
    }

    @GetMapping("/overview")
    public ProfitSummaryResponse profitSummary() {
        return profitServiceUser.profitSummary();
    }
}
