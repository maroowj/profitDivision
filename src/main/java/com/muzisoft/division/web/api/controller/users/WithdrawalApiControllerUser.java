package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.withdrawal.UserWithdrawalListResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalOverviewResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalSaveRequest;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalTypeCondition;
import com.muzisoft.division.web.service.users.WithdrawalServiceUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/withdrawals")
@RequiredArgsConstructor
public class WithdrawalApiControllerUser {

    private final WithdrawalServiceUser withdrawalServiceUser;

    @GetMapping
    public Page<UserWithdrawalListResponse> userWithdrawalList(@PageableDefault(sort = "lastModifiedAt", direction = Sort.Direction.DESC, size = 20)Pageable pageable,
                                                                WithdrawalTypeCondition withdrawalTypeCondition, CommonCondition condition) {
        return withdrawalServiceUser.userWithdrawalList(pageable, withdrawalTypeCondition, condition);
    }

    @PostMapping
    public void createWithdrawalByProfit(@RequestBody WithdrawalSaveRequest request) {
        withdrawalServiceUser.createWithdrawalByProfits(request);
    }

    @PutMapping("/cancel/{withdrawalSeq}")
    public void cancelWithdrawal(@PathVariable("withdrawalSeq") String withdrawalSeq) {
        withdrawalServiceUser.cancelWithdrawalRequest(withdrawalSeq);
    }

    @GetMapping("/overview")
    public WithdrawalOverviewResponse withdrawalOverview() {
        return withdrawalServiceUser.overviewWithdrawal();
    }

}
