package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.investment.InvestmentSaveRequest;
import com.muzisoft.division.web.api.dto.users.investment.UserInvestmentListResponse;
import com.muzisoft.division.web.service.users.InvestmentServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/invest")
@RequiredArgsConstructor
public class InvestmentApiControllerUser {

    private final InvestmentServiceUser investmentServiceUser;

    /** 투자확인요청 **/
    @PostMapping
    public void createInvestment(@RequestBody InvestmentSaveRequest request) {
        investmentServiceUser.createInvestment(request);
    }

    /** 투자내역 (페이징) **/
    @GetMapping
    public Page<UserInvestmentListResponse> userInvestmentList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                                                               CommonCondition condition) {
        return investmentServiceUser.userInvestmentList(pageable, condition);
    }
}
