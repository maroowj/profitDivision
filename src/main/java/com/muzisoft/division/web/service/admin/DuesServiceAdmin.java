package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.dues.*;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.dues.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.text.html.parser.Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DuesServiceAdmin {

    private final DuesRepository duesRepository;
    private final DuesMonthRepository duesMonthRepository;
    private final DuesPushRepository duesPushRepository;
    private final EnvironmentsRepository environmentsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createDuesMonth() {
        Date now = new Date();
        int year = now.getYear() + 1900;
        int month = now.getMonth()+1;

//        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
//        String strDate = sdf.format(new Date());
//        int amount = 100000;
//        int year = Integer.parseInt(strDate.substring(0, 4));
//        int month = Integer.parseInt(strDate.substring(4,6));

        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        int requestedAt = environments.getDuesDate();
        int amount = environments.getDuesAmount();
//        System.out.println("strDate="+strDate);
//        System.out.println("year="+year);
//        System.out.println("month="+month);
        duesMonthRepository.save(
                DuesMonth.create(amount, year, month, requestedAt)
        );
    }

    @Transactional
    public void createDuesTest() {
        Date now = new Date();
        int year = now.getYear() + 1900;
        int month = now.getMonth()+1;
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        int requestedAt = environments.getDuesDate();
        int amount = environments.getDuesAmount();

        DuesMonth duesMonth = DuesMonth.create(amount, year, month, requestedAt);

        List<UserDetails> userDetailsList = userDetailsRepository.userDetailsList();
        List<Dues> duesList = new ArrayList<>();
        for(UserDetails userDetails : userDetailsList) {
            duesList.add(Dues.create(
                            duesMonth,
                            userDetails,
                            userDetails.getName()
                    )
            );
        }
        duesMonthRepository.save(duesMonth);
        duesRepository.saveAll(duesList);
    }

    @Transactional
    public void createDues(DuesMonth duesMonth) {
        List<UserDetails> userDetailsList = userDetailsRepository.userDetailsList();
        List<Dues> duesList = new ArrayList<>();
        for(UserDetails userDetails : userDetailsList) {
            duesList.add(Dues.create(
                    duesMonth,
                    userDetails,
                    userDetails.getName()
                )
            );
        }
        duesRepository.saveAll(duesList);
    }

    // 회비입금통계
    @Transactional
    public DuesStatsResponse duesStats(int year, int month) {
        DuesMonth duesMonth = duesMonthRepository.findByYearAndMonth(year, month).orElseThrow(CEntityNotFoundException.CDuesMonthNotFoundException::new);

        DuesStatsResponse response = new DuesStatsResponse();
        response.setTotal(duesRepository.duesTotalCount(duesMonth));
        response.setPaid(duesRepository.duesPaidCount(duesMonth));
        response.setUnpaid(duesRepository.duesUnpaidCount(duesMonth));

        return response;
    }

    // 회비입금목록(페이징)
    @Transactional
    public DuesListResponse duesList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition, int year, int month) {
        DuesListResponse duesListResponse = new DuesListResponse();

        DuesMonth duesMonth = duesMonthRepository.findByYearAndMonth(year, month)
                .orElseThrow(CEntityNotFoundException.CDuesMonthNotFoundException::new);

        duesListResponse.setDuesList(duesRepository.duesList(pageable, duesCondition, condition, duesMonth));
        duesListResponse.setDuesWaitingCount(duesRepository.duesWaitingCount());

        return duesListResponse;
    }

    // 회비납부확인요청목록
    @Transactional
    public Page<DuesListResponse.DuesList> duesWaitingList(Pageable pageable, DuesCondition duesCondition, CommonCondition condition) {
        return duesRepository.duesWaitingList(pageable, duesCondition, condition);
    }

    // 회비납부상세
    @Transactional
    public DuesDetailsResponse duesDetails(String duesSeq) {
        Dues dues = EntityUtils.duesThrowable(duesRepository, duesSeq);
        User user = EntityUtils.userThrowable(userRepository, dues.getUserDetails());
        String userid = user.getUserId();

        return new DuesDetailsResponse(dues, userid);
    }

    // 회비납부처리
    @Transactional
    public void updatePaymentState(String duesSeq, DuesUpdatePaymentStateRequest request) {
        Dues foundDues = EntityUtils.duesThrowable(duesRepository, duesSeq);

        foundDues.updatePaymentState(request.getPaymentState(), request.getComment());
    }

    // 미납알림발송
    @Transactional
    public void createDuesPush(@RequestBody DuesPushSaveRequest request) {
        DuesMonth duesMonth = duesMonthRepository.findByYearAndMonth(request.getYear(), request.getMonth())
                .orElseThrow(CEntityNotFoundException.CDuesMonthNotFoundException::new);
        List<Dues> duesList = duesRepository.findByDuesMonthAndPaymentState(duesMonth, 0);
        List<DuesPush> duesPushList = new ArrayList<>();
        for(Dues dues : duesList) {
            UserDetails userDetails = dues.getUserDetails();
            duesPushList.add(DuesPush.create(
                        duesMonth,
                        userDetails,
                        request.getContents()
                    )
            );
        }
        duesPushRepository.saveAll(duesPushList);
    }
}