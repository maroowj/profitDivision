package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.dues.Dues;
import com.muzisoft.division.domain.dues.DuesMonth;
import com.muzisoft.division.domain.dues.DuesMonthRepository;
import com.muzisoft.division.domain.dues.DuesRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.users.dues.DuesConfirmRequest;
import com.muzisoft.division.web.api.dto.users.dues.DuesInformationResponse;
import com.muzisoft.division.web.api.dto.users.dues.NotPaidDuesResponse;
import com.muzisoft.division.web.api.dto.users.dues.UserDuesListResponse;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DuesServiceUser {

    private final EnvironmentsRepository environmentsRepository;
    private final UserRepository userRepository;
    private final DuesRepository duesRepository;
    private final DuesMonthRepository duesMonthRepository;

    @Transactional
    public DuesInformationResponse duesInformation() {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        return new DuesInformationResponse(environments);
    }

    @Transactional
    public Page<UserDuesListResponse> userDuesList(Pageable pageable, CommonCondition condition) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Page<UserDuesListResponse> result = duesRepository.userDuesList(pageable, condition, userDetails);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int totalCount = (int) result.getTotalElements();

        int dec = pageSize * pageNumber;

        for(UserDuesListResponse response : result.getContent()) {
            response.setRowNum(totalCount - dec);
            dec++;
        }

        return result;
    }

    @Transactional
    public List<NotPaidDuesResponse> notPaidDues() {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        List<NotPaidDuesResponse> notPaidDuesList = new ArrayList<>();
        List<Dues> duesList = duesRepository.findAllByUserDetailsAndPaymentState(userDetails, 0);
        if(duesList.size()!=0) {
            for(Dues dues : duesList) {
                if(dues.getPaymentState() == 0) {
                    NotPaidDuesResponse notPaidDues = new NotPaidDuesResponse(dues);
                    notPaidDuesList.add(notPaidDues);
                }
            }
        }

        return notPaidDuesList;
    }

    @Transactional
    public void requestConfirm(DuesConfirmRequest request) {
        User securityUser = securityUser();
        User user = EntityUtils.userThrowable(userRepository, securityUser.getSeq());
        UserDetails userDetails = user.getUserDetails();

        Optional<DuesMonth> duesMonth = duesMonthRepository.findByYearAndMonth(request.getYear(), request.getMonth());
        if(!duesMonth.isPresent()) {
            throw new CEntityNotFoundException.CDuesMonthNotFoundException();
        }

        Optional<Dues> optionalDues = duesRepository.findByDuesMonthAndUserDetailsAndPaymentState(duesMonth.get(), userDetails, 0);
        if(!optionalDues.isPresent()) {
            throw new CEntityNotFoundException.CDuesNotFoundException();
        }

        Dues dues = optionalDues.get();
        dues.requestConfirm(
                request.getAmount(),
                request.getBankName(),
                request.getAccountNumber(),
                request.getPaidAt()
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
