package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.point.UserPointsRepository;
import com.muzisoft.division.domain.profit.*;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.profit.ProfitLogListResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitSummaryResponse;
import com.muzisoft.division.web.api.dto.users.profit.ProfitTypeCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfitServiceUser {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserPointsRepository userPointsRepository;
    private final ExpectedProfitRepository expectedProfitRepository;
    private final ExpectedProfitDetailsRepository expectedProfitDetailsRepository;
    private final ProfitDetailsLogRepository profitDetailsLogRepository;

    @Transactional
    public ProfitSummaryResponse profitSummary() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = EntityUtils.userThrowable(userRepository, user.getSeq());
        UserDetails userDetails = foundUser.getUserDetails();

        ProfitSummaryResponse profitSummary = new ProfitSummaryResponse();
        long totalSavedProfit = userPointsRepository.remainedTotalSavedAmountForUser(userDetails);
        long totalUsedProfit = userPointsRepository.remainedTotalUsedAmountForUser(userDetails);
        long total = totalSavedProfit - totalUsedProfit;

        /*
        Date today = new Date();
        int year = today.getYear() + 1900;
        int month = today.getMonth()+1;

        Optional<ExpectedProfit> expectedProfit = expectedProfitRepository.findByReferenceYearAndReferenceMonth(year, month);
        */

        ExpectedProfit expectedProfit = expectedProfitRepository.findTopByOrderBySeqDesc(); // 가장 최근에 등록한 기대 수익에서 가져옴
        long monthlyAmount = 0;
        long myMonthlyAmount = 0;
        if(expectedProfit!=null) {
            ExpectedProfitDetails expectedProfitDetails = expectedProfitDetailsRepository.findByExpectedProfitAndUserDetails(expectedProfit, userDetails);
            if(expectedProfitDetails!=null) {
                monthlyAmount = expectedProfit.getAmount();
                myMonthlyAmount = expectedProfitDetails.getAmount();
            }
        }

        long weeklyAmount = monthlyAmount / 4;
        long myWeeklyAmount = myMonthlyAmount / 4;
        long dailyAmount = monthlyAmount / 28;
        long myDailyAmount = myMonthlyAmount / 28;

        profitSummary.setTotal(total);
        profitSummary.setMonthlyAmount(monthlyAmount);
        profitSummary.setMyMonthlyAmount(myMonthlyAmount);
        profitSummary.setWeeklyAmount(weeklyAmount);
        profitSummary.setMyWeeklyAmount(myWeeklyAmount);
        profitSummary.setDailyAmount(dailyAmount);
        profitSummary.setMyDailyAmount(myDailyAmount);

        return profitSummary;
    }

    @Transactional
    public Page<ProfitLogListResponse> profitLogList(Pageable pageable, ProfitTypeCondition profitTypeCondition, CommonCondition condition) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = EntityUtils.userThrowable(userRepository, user.getSeq());
        UserDetails foundUserDetails = foundUser.getUserDetails();

        Page<ProfitLogListResponse> result = profitDetailsLogRepository.profitLogList(pageable, profitTypeCondition, condition, foundUserDetails);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCount = (int) result.getTotalElements();

        int dec = pageSize * pageNumber;

        for(ProfitLogListResponse response : result.getContent()) {
            response.setRowNum(totalCount - dec);
            dec++;
        }

        return result;
    }
}
