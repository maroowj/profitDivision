package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.point.Investment;
import com.muzisoft.division.domain.point.InvestmentRepository;
import com.muzisoft.division.domain.point.UserPoints;
import com.muzisoft.division.domain.point.UserPointsRepository;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentApprovalRequest;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentCondition;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.investment.InvestmentListResponse;
import com.muzisoft.division.web.api.dto.admin.profit.TotalPointsResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.exception.business.CBusinessException;
import com.muzisoft.division.web.exception.business.CInvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentServiceAdmin {

    private final InvestmentRepository investmentRepository;
    private final UserPointsRepository userPointsRepository;

    @Transactional
    public Page<InvestmentListResponse> investmentList(Pageable pageable, InvestmentCondition investmentCondition, CommonCondition condition){
        return investmentRepository.investmentList(pageable, investmentCondition, condition);
    }

    @Transactional
    public Page<InvestmentListPerUserResponse> investmentListPerUser(Pageable pageable, String userDetailsSeq) {
        return investmentRepository.investmentListPerUser(pageable, userDetailsSeq);
    }

    @Transactional
    public void investmentApproval(String seq, InvestmentApprovalRequest request) {
        Investment investment = EntityUtils.investmentThrowable(investmentRepository, seq);
        TotalPointsResponse totalPointsResponse =userPointsRepository.totalPointsByUserDetailsAndPointsSupplier(investment.getUserDetails(), PointSupplier.INVESTMENT);
        long totalInvestment = totalPointsResponse.getSavedAmount() - totalPointsResponse.getUsedAmount();

        if(investment.getStatus()!=0) {
            throw new CInvalidValueException.CAlreadyInvestmentStatusUpdateException();
        }

        if(request.getStatus()==1) { // 승인
            Optional<Investment> recentInvestment = investmentRepository.findTopByUserDetailsAndStatusOrderBySeqDesc(investment.getUserDetails(), 1);
            long total = request.getAmount();
            if(recentInvestment.isPresent()) {
                total = recentInvestment.get().getTotal() + request.getAmount();
            }
            investment.accept(request.getAmount(), total);

            userPointsRepository.save(
                    UserPoints.create(
                            investment.getUserDetails(),
                            PointSupplier.INVESTMENT,
                            investment.getAmount()
                            )
            );

            UserDetails userDetails = investment.getUserDetails();
            totalInvestment = totalInvestment + request.getAmount();
            userDetails.updateInvestment(totalInvestment);
        }else if(request.getStatus()==2) { // 거절
            investment.decline(request.getComment());
        }
    }


}
