package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.web.api.dto.admin.reserve.*;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.EnvironmentsUpdateRequest;
import com.muzisoft.division.web.service.admin.ReservesServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/reserves")
@RequiredArgsConstructor
public class ReservesApiControllerAdmin {

    private final ReservesServiceAdmin reservesServiceAdmin;

    /** 현재 추천인 적립률 **/
   @GetMapping("/recommender-rate")
    public RecommenderDividendRateResponse recommenderDividendRate() {
        return reservesServiceAdmin.recommenderDividendRate();
    }

    /** 추천인 적립률 수정 **/
    @PutMapping("/recommender-rate")
    public void updateRecommenderDividendRate(@RequestBody EnvironmentsUpdateRequest request) {
        reservesServiceAdmin.updatedRecommenderDividendRate(request);
    }

    /** 현재 레벨당 전환금 **/
    @GetMapping("/conversion-rate")
    public ConversionRateResponse conversionRateResponse() {
        return reservesServiceAdmin.conversionRate();
    }

    /** 레벨당 전환금 수정 **/
    @PutMapping("/conversion-rate")
    public void updateConversionRate(@RequestBody EnvironmentsUpdateRequest request) {
        reservesServiceAdmin.updatedConversionRate(request);
    }

    /** 적립금 지급 **/
    @PostMapping("/users")
    public void saveReserves(@RequestBody ReservesSaveRequest request) {
        reservesServiceAdmin.createReserves(request);
    }

    /** 적립금 리스트 **/
    @GetMapping
    public Page<ReservesListResponse> reservesList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                   BasicCondition basicCondition, CommonCondition condition) {
        return reservesServiceAdmin.reservesList(pageable, basicCondition, condition);
    }

    /** 회원 적립금 내역 **/
    @GetMapping("/users/{userDetailsSeq}")
    public Page<ReservesDetailsResponse> reservesDetails(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                         @PathVariable("userDetailsSeq") String userDetailsSeq, CommonCondition condition) {
        return reservesServiceAdmin.reservesDetails(pageable, userDetailsSeq, condition);
    }
}
