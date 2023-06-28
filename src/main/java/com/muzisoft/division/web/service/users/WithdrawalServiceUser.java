package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.point.UserPointsRepository;
import com.muzisoft.division.domain.point.Withdrawal;
import com.muzisoft.division.domain.point.WithdrawalRepository;
import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.withdrawal.UserWithdrawalListResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalOverviewResponse;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalSaveRequest;
import com.muzisoft.division.web.api.dto.users.withdrawal.WithdrawalTypeCondition;
import com.muzisoft.division.web.exception.business.CBusinessException;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalServiceUser {

    private final WithdrawalRepository withdrawalRepository;
    private final UserRepository userRepository;
    private final UserPointsRepository userPointsRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public void createWithdrawalByProfits(WithdrawalSaveRequest request) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        boolean waitingWithdrawal = withdrawalRepository.existsAllByUserDetailsAndAcceptedAndSource(userDetails, 0, 0);

        if(waitingWithdrawal) {
            throw  new CBusinessException.CWithdrawalDisposalNotYetException();
        }

        long totalSavedProfit = userPointsRepository.remainedTotalSavedAmountForUser(userDetails);
        long totalUsedProfit = userPointsRepository.remainedTotalUsedAmountForUser(userDetails);
        long totalBalance = totalSavedProfit - totalUsedProfit;

        if(request.getAmount() > totalBalance) {
            throw new CBusinessException.CExceedRequestAmount();
        }

        withdrawalRepository.save(
                Withdrawal.create(
                        userDetails,
                        request.getBankName(),
                        request.getAccountNumber(),
                        totalBalance,
                        (long)request.getAmount(),
                        0
                )
        );
    }

    @Transactional
    public Page<UserWithdrawalListResponse> userWithdrawalList(Pageable pageable, WithdrawalTypeCondition withdrawalTypeCondition, CommonCondition condition) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Page<UserWithdrawalListResponse> result = withdrawalRepository.userWithdrawalList(pageable, withdrawalTypeCondition, condition, userDetails);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCount = (int) result.getTotalElements();

        int dec = pageSize * pageNumber;

        for(UserWithdrawalListResponse response : result.getContent()) {
            response.setRowNum(totalCount - dec);
            dec++;
        }

        return result;
    }

    @Transactional
    public void cancelWithdrawalRequest(String withdrawalSeq) {
        Withdrawal withdrawal = EntityUtils.withdrawalThrowable(withdrawalRepository, withdrawalSeq);
        withdrawal.cancelWithdrawal();
    }

    @Transactional
    public WithdrawalOverviewResponse overviewWithdrawal() {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();
        Optional<UserAccount> userAccount = userAccountRepository.findByUserDetailsAndUsed(userDetails, true);

        WithdrawalOverviewResponse withdrawalOverviewResponse = new WithdrawalOverviewResponse();
        withdrawalOverviewResponse.setName(userDetails.getName());
        if(userAccount.isPresent()) {
            withdrawalOverviewResponse.setBankName(userAccount.get().getBankName());
            withdrawalOverviewResponse.setAccountNumber(userAccount.get().getAccountNumber());
        }

        long totalSavedProfit = userPointsRepository.remainedTotalSavedAmountForUser(userDetails);
        long totalUsedProfit = userPointsRepository.remainedTotalUsedAmountForUser(userDetails);
        long total = totalSavedProfit - totalUsedProfit;
        withdrawalOverviewResponse.setTotal(total);
        withdrawalOverviewResponse.setAvailable(total);

        Date firstDay = new Date();
        firstDay.setMonth(0);
        firstDay.setDate(1);
        firstDay.setHours(0);
        firstDay.setMinutes(0);
        firstDay.setSeconds(0);
//        System.out.println(">>>"+firstDay);
        List<Withdrawal> withdrawalList = withdrawalRepository.amountPerYear(userDetails, firstDay); // 수익금에서만 출금한 금액
        long amountPerYear = 0;
        if(withdrawalList.size()!=0) {
            for(Withdrawal withdrawal : withdrawalList) {
                amountPerYear = amountPerYear + withdrawal.getAmount();
            }
        }
        withdrawalOverviewResponse.setAmountPerYear(amountPerYear);
        return withdrawalOverviewResponse;
    }

    private static User securityUser() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new CSecurityException.CAuthenticationEntryPointException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
