package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.interest.*;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.service.admin.InterestServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/interest")
@RequiredArgsConstructor
public class InterestApiControllerAdmin {

    private final InterestServiceAdmin interestServiceAdmin;

    /** 지난 이자 리스트 **/
    @GetMapping
    public Page<InterestListResponse> interestList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, CommonCondition condition) {
        return interestServiceAdmin.interestList(pageable, condition);
    }

    /** 현재 이자율 **/
    @GetMapping("/rate")
    public InterestRateResponse interestRate() {
        return interestServiceAdmin.interestRate();
    }

    /** 이자율 수정 **/
    @PutMapping("/rate")
    public void updateInterestRate(@RequestBody InterestRateUpdateRequest request) {
        interestServiceAdmin.updateInterestRate(request);
    }

    /** 회원별 이자 리스트 **/
    @GetMapping("/{interestSeq}")
    public Page<InterestListPerUserResponse> interestListByInterest(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                    @PathVariable("interestSeq") String interestSeq, BasicCondition basicCondition, CommonCondition condition) {
        return interestServiceAdmin.interestListByInterest(pageable, interestSeq, basicCondition, condition);
    }

    /** 회원 이자 상세 **/
    @GetMapping("/users/{userDetailsSeq}")
    public Page<InterestDetailsResponse> interestDetails(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                         @PathVariable("userDetailsSeq") String userDetailsSeq, CommonCondition condition) {
        return interestServiceAdmin.interestDetails(pageable, userDetailsSeq, condition);
    }

    /** 테스트용 이자 지급 **/
    @PostMapping
    public void createInterest() {
        interestServiceAdmin.createInterest();
    }
}
