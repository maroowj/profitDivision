package com.muzisoft.division.web.controller;

import com.muzisoft.division.domain.dues.DuesMonth;
import com.muzisoft.division.domain.dues.DuesMonthRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.service.admin.DuesServiceAdmin;
import com.muzisoft.division.web.service.admin.InterestServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


import java.util.Date;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SchedulerController {

    private final DuesServiceAdmin duesServiceAdmin;
    private final DuesMonthRepository duesMonthRepository;
    private final InterestServiceAdmin interestServiceAdmin;


    @Scheduled(cron = "0 0 0 * * *")
    @SchedulerLock(name = "createDues", lockAtLeastFor = "23h", lockAtMostFor = "23h")
//    @Scheduled(cron = "0/10 * * * * *")
//    @SchedulerLock(name = "createDuesMonth", lockAtLeastFor = "9s", lockAtMostFor = "9s")
//    @Scheduled(cron = "0 0/1 * * * *") // 1분마다
//    @SchedulerLock(name = "createDuesMonth", lockAtLeastFor = "59s", lockAtMostFor = "59s")
    public void createDues() {

        Date now = new Date();
        int year = now.getYear() + 1900;
        int month = now.getMonth()+1;
        int day = now.getDate();
        int minute = now.getMinutes();

//        System.out.println(">>>"+minute);
        if(day == 1){
            duesServiceAdmin.createDuesMonth();
            interestServiceAdmin.createInterest();
        }

        Optional<DuesMonth> foundDuesMonth = duesMonthRepository.findByYearAndMonth(year, month);
        if(foundDuesMonth.isPresent()) {
            DuesMonth duesMonth = foundDuesMonth.get();
            int requestedAt = foundDuesMonth.get().getRequestedAt();
            if(day==requestedAt) {
                duesServiceAdmin.createDues(duesMonth);
            }
        }

    }

}
