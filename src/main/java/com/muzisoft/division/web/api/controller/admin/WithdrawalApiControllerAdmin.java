package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.profit.*;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalCondition;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalDisposalRequest;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.ProfitServiceAdmin;
import com.muzisoft.division.web.service.admin.WithdrawalServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/withdrawals")
@RequiredArgsConstructor
public class WithdrawalApiControllerAdmin {

    private final WithdrawalServiceAdmin withdrawalServiceAdmin;

    /** 출금목록 (페이징)**/
    @GetMapping
    public Page<WithdrawalListResponse> withdrawalList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                       WithdrawalCondition withdrawalCondition, CommonCondition condition) {
        return withdrawalServiceAdmin.withdrawalList(pageable, withdrawalCondition, condition);
    }

    /** 회원출금내역 **/
    @GetMapping("/users/{userDetailsSeq}")
    public Page<WithdrawalListPerUserResponse> withdrawalListPerUser(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                     @PathVariable("userDetailsSeq") String userDetailsSeq) {
        return withdrawalServiceAdmin.withdrawalListPerUser(pageable, userDetailsSeq);
    }

    /** 회원출금처리 **/
    @PutMapping("/{withdrawalSeq}")
    public void withdrawalDisposal(@PathVariable("withdrawalSeq") String withdrawalSeq, @RequestBody WithdrawalDisposalRequest request) {
         withdrawalServiceAdmin.withdrawalDisposal(withdrawalSeq, request);
    }
}
