package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.investment.InvestmentApprovalRequest;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentCondition;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.InvestmentServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/investments")
@RequiredArgsConstructor
public class InvestmentApiControllerAdmin {

    private final InvestmentServiceAdmin investmentServiceAdmin;


    /** 투자내역목록 **/
    @GetMapping
    public Page<InvestmentListResponse> investmentList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                       InvestmentCondition investmentCondition, CommonCondition condition) {
        return investmentServiceAdmin.investmentList(pageable, investmentCondition, condition);
    }

    /** 회원별투자내역 **/
    @GetMapping("/{userDetailsSeq}")
    public Page<InvestmentListPerUserResponse> investmentListPerUser(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                     @PathVariable("userDetailsSeq") String userDetailsSeq) {
        return investmentServiceAdmin.investmentListPerUser(pageable, userDetailsSeq);
    }


    /** 투자처리 (승인-거절 상태 변경) **/
    @PutMapping ("/{investmentSeq}")
    public void investmentApproval(@PathVariable("investmentSeq") String investmentSeq, @RequestBody InvestmentApprovalRequest request) {
        investmentServiceAdmin.investmentApproval(investmentSeq, request);
    }
}
