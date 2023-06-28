package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.point.Withdrawal;
import com.muzisoft.division.domain.point.WithdrawalRepository;
import com.muzisoft.division.domain.reverses.ReservesLog;
import com.muzisoft.division.domain.reverses.ReservesLogRepository;
import com.muzisoft.division.domain.reverses.ReservesRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.reserve.ReserveOverviewResponse;
import com.muzisoft.division.web.api.dto.users.reserve.ReservesConversionRequest;
import com.muzisoft.division.web.api.dto.users.reserve.UserReservesListResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.UserWithdrawalListResponse;
import com.muzisoft.division.web.exception.business.CBusinessException;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservesServiceUser {

    private final EnvironmentsRepository environmentsRepository;
    private final UserRepository userRepository;
    private final ReservesRepository reservesRepository;
    private final ReservesLogRepository reservesLogRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public ReserveOverviewResponse reserveOverview() {
        User securityUser = securityUser();
//        System.out.println(">>>" + securityUser.getSeq());
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();
        Optional<UserAccount> userAccount = userAccountRepository.findByUserDetailsAndUsed(userDetails, true);

        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        int conversionRate = environments.getConversionRate();

        ReserveOverviewResponse response = new ReserveOverviewResponse();
        response.setName(userDetails.getName());
        response.setGrade(userDetails.getGrade().getTitle());
        response.setRecommendCode(userDetails.getRecommendCode());
        int level = (int)(userDetails.getInvestment() / conversionRate);
        response.setLevel(level);
        response.setInvestment(userDetails.getInvestment());
        long savedAmount = reservesRepository.totalSavedAmount(userDetails);
        long usedAmount = reservesRepository.totalUsedAmount(userDetails);
        long totalReserves = savedAmount - usedAmount;
        response.setReserves((int) totalReserves);
        response.setConversionRate(conversionRate);
        if(userAccount.isPresent()) {
            response.setBankName(userAccount.get().getBankName());
            response.setAccountNumber(userAccount.get().getAccountNumber());
        }
        return response;
    }

    @Transactional
    public Page<UserReservesListResponse> userReservesList(Pageable pageable, CommonCondition condition) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();
        Page<UserReservesListResponse> result = reservesLogRepository.userReservesList(pageable, condition, userDetails);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCount = (int) result.getTotalElements();

        int dec = pageSize * pageNumber;

        for(UserReservesListResponse response : result.getContent()) {
            response.setRowNum(totalCount - dec);
            dec++;
        }

        return result;
    }

    @Transactional
    public void reservesConversion(ReservesConversionRequest request) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        boolean waitingWithdrawal = withdrawalRepository.existsAllByUserDetailsAndAcceptedAndSource(userDetails, 0, 1);

        if(waitingWithdrawal) {
            throw  new CBusinessException.CWithdrawalDisposalNotYetException();
        }

        long savedAmount = reservesRepository.totalSavedAmount(userDetails);
        long usedAmount = reservesRepository.totalUsedAmount(userDetails);
        long totalReserves = savedAmount - usedAmount;

        withdrawalRepository.save(
                Withdrawal.create(
                        userDetails,
                        request.getBankName(),
                        request.getAccountNumber(),
                        totalReserves,
                        request.getAmount(),
                        1
                )
        );

        String content = "적립금 전환 신청";

        reservesLogRepository.save(
                ReservesLog.requestLog(
                        userDetails,
                        content,
                        (int)request.getAmount(),
                        (int)totalReserves
                )
        );
    }

    private static User securityUser() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new CSecurityException.CAuthenticationEntryPointException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
