package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.point.Investment;
import com.muzisoft.division.domain.point.InvestmentRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.investment.InvestmentSaveRequest;
import com.muzisoft.division.web.api.dto.users.investment.UserInvestmentListResponse;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentServiceUser {

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Transactional
    public void createInvestment(@RequestBody InvestmentSaveRequest request) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        investmentRepository.save(
                Investment.create(
                        userDetails,
                        request.getName(),
                        request.getBankName(),
                        request.getAccountNumber(),
                        request.getAmount(),
                        request.getDepositedAt()
                )
        );
    }

    @Transactional
    public Page<UserInvestmentListResponse> userInvestmentList(Pageable pageable, CommonCondition condition) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Page<UserInvestmentListResponse> result = investmentRepository.userInvestment(pageable, condition, userDetails);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int totalCount = (int) result.getTotalElements();

        int dec = pageSize * pageNumber;

        for(UserInvestmentListResponse response : result.getContent()) {
            response.setRowNum(totalCount - dec);
            dec++;
        }

        return result;
    }

    private static User securityUser() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new CSecurityException.CAuthenticationEntryPointException();
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
