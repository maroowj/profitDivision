package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.profit.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.ProfitServiceAdmin;
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
@RequestMapping("/api/admin/profits")
@RequiredArgsConstructor
public class ProfitApiControllerAdmin {

    private final ProfitServiceAdmin profitServiceAdmin;

    /** 가상수익등록 **/
    @PostMapping("/expected")
    public void CreateExpectedProfit(@RequestBody ExpectedProfitSaveAndUpdateRequest request) throws ParseException {
        profitServiceAdmin.createExpectedProfit(request);
    }

    /** 가상수익목록(페이징) **/
    @GetMapping("/expected")
    public Page<ExpectedProfitListResponse> expectedProfitList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                               ExpectedProfitRequest request, ProfitCondition profitCondition, CommonCondition condition) {
        return profitServiceAdmin.expectedProfitList(pageable, request, profitCondition, condition);
    }

    /** 월별가상수익 **/
    @GetMapping("/expected/monthly")
    public Page<MonthlyExpectedProfitsResponse> monthlyExpectedProfits(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       CommonCondition condition) {
        return profitServiceAdmin.monthlyExpectedProfits(pageable, condition);
    }

    /** 지난 수익 리스트 **/
    @GetMapping
    public Page<LastProfitListResponse> lastProfitList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, CommonCondition condition) {
        return profitServiceAdmin.lastProfitList(pageable, condition);
    }

    /** 수익 등록 **/
    @PostMapping
    public void createProfit(@RequestBody ProfitSaveRequest request) {
        profitServiceAdmin.createProfit(request);
    }

    /** 회원별 수익 리스트 **/
    @GetMapping("/users")
    public Page<ProfitListResponse> profitList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                               ProfitCondition profitCondition, CommonCondition condition, String profitSeq) {
        return profitServiceAdmin.profitList(pageable, profitCondition, condition, profitSeq);
    }

    /** 회원별 수익 상세(목록) **/
    @GetMapping("/users/{userDetailsSeq}")
    public Page<ProfitDetailsPerUser> profitDetailsPerUsers(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                            CommonCondition condition, @PathVariable("userDetailsSeq") String userDetailsSeq) {
        return profitServiceAdmin.profitDetailsPerUsers(pageable, condition, userDetailsSeq);
    }


}
