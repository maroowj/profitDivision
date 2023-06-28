package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.point.*;
import com.muzisoft.division.domain.profit.ProfitDetailsLog;
import com.muzisoft.division.domain.profit.ProfitDetailsLogRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.interest.*;
import com.muzisoft.division.web.api.dto.admin.member.UserListResponse;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestServiceAdmin {

    private final EnvironmentsRepository environmentsRepository;
    private final InterestRepository interestRepository;
    private final InterestDetailsRepository interestDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserPointsRepository userPointsRepository;
    private final ProfitDetailsLogRepository profitDetailsLogRepository;

    @Transactional
    public InterestRateResponse interestRate() {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        return new InterestRateResponse(environments);
    }

    @Transactional
    public void updateInterestRate(InterestRateUpdateRequest request) {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        environments.updateInterestRate(request.getInterestRate());
    }

    @Transactional
    public Page<InterestListResponse> interestList(Pageable pageable, CommonCondition condition) {
        return interestRepository.interestList(pageable, condition);
    }

    @Transactional
    public Page<InterestListPerUserResponse> interestListByInterest(Pageable pageable, String interestSeq, BasicCondition basicCondition, CommonCondition condition) {
        Interest interest = EntityUtils.interestThrowable(interestRepository, interestSeq);
        Page<InterestListPerUserResponse> result = interestDetailsRepository.interestListByInterest(pageable, interest, basicCondition, condition);

        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        int conversionRate = environments.getConversionRate();

        for(InterestListPerUserResponse response : result.getContent()) {
            UserDetails foundUserDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, response.getUserDetailsSeq());
            long totalInvestments = foundUserDetails.getInvestment();
            long level = totalInvestments/conversionRate;
            response.setLevel(level + "Level");
        }
        return result;
    }

    @Transactional
    public Page<InterestDetailsResponse> interestDetails(Pageable pageable, String userDetailsSeq, CommonCondition condition) {
        UserDetails userDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);
        return interestDetailsRepository.interestDetails(pageable, userDetails, condition);
    }

    /**
     * 이자 지급
     **/
    @Transactional
    public void createInterest() {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        float interestRate = environments.getInterestRate(); // 이자율
        int cuttingRate = environments.getCuttingRate(); // 절삭 비율

        Date dateTo = new Date();
        long remainedSavedAmount = userPointsRepository.remainedTotalSavedAmount(dateTo, null);
        long remainedUsedAmount = userPointsRepository.remainedTotalUsedAmount(dateTo, null);
        float totalInterest = ((remainedSavedAmount - remainedUsedAmount) * (interestRate / 100)) / 12;

        // 해당 월의 전체 이자 엔터티 생성
        Interest interest = Interest.create(
                (remainedSavedAmount - remainedUsedAmount),
                (long) totalInterest,
                interestRate
        );

        // 전체 이자 전체 회원에게 분배
        List<InterestDetails> saveInterestDetailsList = new ArrayList<>();
        List<UserPoints> saveUserPointsList = new ArrayList<>();
        List<ProfitDetailsLog> saveProfitDetailsLogList = new ArrayList<>();
        List<UserDetails> userDetailsList = userDetailsRepository.userDetailsList();
        for (UserDetails userDetails : userDetailsList) {
            long sumSavedAmount = userPointsRepository.remainedTotalSavedAmount(dateTo, userDetails);
            long sumUsedAmount = userPointsRepository.remainedTotalUsedAmount(dateTo, userDetails);
            float userInterest = ((sumSavedAmount - sumUsedAmount) * (interestRate / 100)) / 12;

            int share = (int) userInterest / cuttingRate;
            long interestAmount = share * cuttingRate;

            // 사용자 이자 엔터티 생성
            if (share > 0) {
                // 이자 상세 엔터티
                InterestDetails interestDetails = InterestDetails.create(
                        interest,
                        userDetails,
                        (sumSavedAmount - sumUsedAmount),
                        interestAmount
                );

                saveInterestDetailsList.add(
                        interestDetails
                );

                // 사용자 포인트 엔터티
                saveUserPointsList.add(
                        UserPoints.create(
                                userDetails,
                                PointSupplier.INTEREST,
                                (int) interestAmount
                        )
                );

                // 사용자수익상세 엔터티
                Optional<ProfitDetailsLog> recentLog = profitDetailsLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(userDetails);
                long logTotal = 0;
                if (recentLog.isPresent()) {
                    logTotal = recentLog.get().getTotal() + interestAmount;
                }

                GregorianCalendar today = new GregorianCalendar();
                int year = today.get(today.YEAR);
                int month = (today.get(today.MONTH)+1);
                if(month == 1) {
                    year = year-1;
                    month = 12;
                }
                String content = year + "년 " + month + "월 이자";

                saveProfitDetailsLogList.add(
                        ProfitDetailsLog.createByInterest(
                            interestDetails,
                                userDetails,
                                (int) interestAmount,
                                logTotal,
                                content
                        )
                );
            }
        }

        interestRepository.save(interest);
        interestDetailsRepository.saveAll(saveInterestDetailsList);
        userPointsRepository.saveAll(saveUserPointsList);
        profitDetailsLogRepository.saveAll(saveProfitDetailsLogList);
    }
}
