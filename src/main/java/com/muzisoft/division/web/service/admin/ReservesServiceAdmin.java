package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.reverses.Reserves;
import com.muzisoft.division.domain.reverses.ReservesLog;
import com.muzisoft.division.domain.reverses.ReservesLogRepository;
import com.muzisoft.division.domain.reverses.ReservesRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.reserve.*;
import com.muzisoft.division.web.api.dto.common.BasicCondition;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.EnvironmentsUpdateRequest;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservesServiceAdmin {

    private final ReservesRepository reservesRepository;
    private final ReservesLogRepository reservesLogRepository;
    private final EnvironmentsRepository environmentsRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Transactional
    public RecommenderDividendRateResponse recommenderDividendRate() {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        return new RecommenderDividendRateResponse(environments);
    }

    @Transactional
    public void updatedRecommenderDividendRate(EnvironmentsUpdateRequest request) {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        environments.updateRecommenderDividendRate(request.getRecommenderDividendRate());
    }

    @Transactional
    public ConversionRateResponse conversionRate() {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        return new ConversionRateResponse(environments);
    }

    @Transactional
    public void updatedConversionRate(EnvironmentsUpdateRequest request) {
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);
        environments.updateConversionRate(request.getConversionRate());
    }

    @Transactional
    public void createReserves(ReservesSaveRequest request) {
        List<Reserves> saveReservesList = new ArrayList<>();
        List<ReservesLog> saveReservesLogList = new ArrayList<>();

        for (String userDetailsSeq : request.getUserDetailsSeq()) {
            UserDetails userDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);

            int residual = 0; // 잔여적립금 (총 적립금)
            Optional<ReservesLog> recentReservesLog = reservesLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(userDetails);
            if (recentReservesLog.isPresent()) {
                residual = recentReservesLog.get().getResidual() + request.getSavedAmount();
            } else {
                residual = request.getSavedAmount();
            }

            saveReservesList.add(
                    Reserves.saveByAdmin(userDetails, request.getSavedAmount())
            );

            saveReservesLogList.add(
                    ReservesLog.saveLog(userDetails, request.getContent(), request.getSavedAmount(), residual)
            );
        }

        reservesRepository.saveAll(saveReservesList);
        reservesLogRepository.saveAll(saveReservesLogList);
    }

    @Transactional
    public Page<ReservesListResponse> reservesList(Pageable pageable, BasicCondition basicCondition, CommonCondition condition) {
        return reservesLogRepository.reservesLogList(pageable, basicCondition, condition);
    }

    @Transactional
    public Page<ReservesDetailsResponse> reservesDetails(Pageable pageable, String userDetailsSeq, CommonCondition condition) {
        UserDetails userDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);
        return reservesLogRepository.reservesDetails(pageable, userDetails, condition);
    }
}
