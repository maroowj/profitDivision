package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.point.*;
import com.muzisoft.division.domain.profit.Profit;
import com.muzisoft.division.domain.profit.ProfitDetailsLog;
import com.muzisoft.division.domain.profit.ProfitDetailsLogRepository;
import com.muzisoft.division.domain.reverses.Reserves;
import com.muzisoft.division.domain.reverses.ReservesLog;
import com.muzisoft.division.domain.reverses.ReservesLogRepository;
import com.muzisoft.division.domain.reverses.ReservesRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalCondition;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalDisposalRequest;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListPerUserResponse;
import com.muzisoft.division.web.api.dto.admin.withdrawal.WithdrawalListResponse;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.common.enums.WithdrawalStatusCheck;
import com.muzisoft.division.web.exception.business.CBusinessException;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalServiceAdmin {

    private final WithdrawalRepository withdrawalRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserPointsRepository userPointsRepository;
    private final ProfitDetailsLogRepository profitDetailsLogRepository;
    private final ReservesRepository reservesRepository;
    private final ReservesLogRepository reservesLogRepository;
    private final UsedPointsRepository usedPointsRepository;
    private final UsedPointsDetailsRepository usedPointsDetailsRepository;


    @Transactional
    public Page<WithdrawalListResponse> withdrawalList(Pageable pageable, WithdrawalCondition withdrawalCondition, CommonCondition condition) {
        Page<WithdrawalListResponse> result = withdrawalRepository.withdrawalList(pageable, withdrawalCondition, condition);

        for(WithdrawalListResponse response : result.getContent()) {
            if(response.getType()==0) {
                response.setSource("수익금");
            }else {
                response.setSource("적립금");
            }
        }

        return result;
    }

    @Transactional
    public Page<WithdrawalListPerUserResponse> withdrawalListPerUser(Pageable pageable, String userDetailsSeq) {
        UserDetails userDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);

        Page<WithdrawalListPerUserResponse> result = withdrawalRepository.withdrawalListPerUser(pageable, userDetails);
        for(WithdrawalListPerUserResponse response : result.getContent()) {
            if(response.getType()==0) {
                response.setSource("수익금");
            }else {
                response.setSource("적립금");
            }
        }
        return result;
    }

    @Transactional
    public void withdrawalDisposal(String withdrawalSeq, WithdrawalDisposalRequest request) {
        Withdrawal withdrawal = EntityUtils.withdrawalThrowable(withdrawalRepository, withdrawalSeq);
        UserDetails userDetails = withdrawal.getUserDetails();

        if(withdrawal.getAccepted()!=0) {
            throw new CBusinessException.CAlreadyWithdrawalDisposed();
        }

        if (request.getAccepted()==1) { // 승인 시
            long amount = withdrawal.getAmount(); // 요청 금액

            if(withdrawal.getSource() == 0) { // 출금 출처가 수익금인 경우,
                boolean profitRemain = false;
                boolean interestRemain = false;
                boolean investmentRemain = false;

                // 포인트사용내역 엔터티 생성
                UsedPoints usedPoints = UsedPoints.create(userDetails, "출금", (int) amount);
                List<UsedPointsDetails> saveUsedPointsDetailsList = new ArrayList<>();
                // 1. 수익금에서 출금요청금액 만큼 출금 시키기
                List<UserPoints> profitList = userPointsRepository.findAllByUserDetailsAndSupplierAndUsable(userDetails, PointSupplier.PROCEEDS, true);
                long savedProfits = 0;
                for (UserPoints p : profitList) {
                    savedProfits = savedProfits + (p.getSavedAmount() - p.getUsedAmount());
                }
                if (savedProfits - withdrawal.getAmount() < 0) {
                    interestRemain = true;
                }

                for (UserPoints profit : profitList) {
                    int profitAmount = profit.getSavedAmount() - profit.getUsedAmount();
                    long remainedAmount = amount;
                    amount = profitAmount - amount; // amount = 수익금 - 출금요청금액
                    if (amount > 0) { // 수익금 엔터티의 수익금이 요청금액 보다 많으면 for문 탈출
                        profit.usePoint(true, ((int) remainedAmount + profit.getUsedAmount()));
                        saveUsedPointsDetailsList.add(
                                UsedPointsDetails.create(usedPoints, profit, (int)remainedAmount)
                        );
                        break;
                    } else if (amount == 0) { // 수익금 엔터티의 수익금이 요청금액과 같다면 for문 탈출
                        profit.usePoint(false, profit.getSavedAmount());
                        saveUsedPointsDetailsList.add(
                                UsedPointsDetails.create(usedPoints, profit, profit.getSavedAmount())
                        );
                        break;
                    } else if (amount < 0) { // 수익금 엔터티의 수익금보다 출금요청금액이 더 많을 때
                        amount = amount * -1;
                        profit.usePoint(false, profit.getSavedAmount());
                        saveUsedPointsDetailsList.add(
                                UsedPointsDetails.create(usedPoints, profit, profit.getSavedAmount())
                        );
                    }
                }

                // 2. 모든 수익금 보다 출금요청금액이 더 많을 경우 남은 잔액 이자에서 출금 시키기
                if (interestRemain == true) {
                    List<UserPoints> interestList = userPointsRepository.findAllByUserDetailsAndSupplierAndUsable(userDetails, PointSupplier.INTEREST, true);
                    long savedInterests = savedProfits;
                    for (UserPoints p : interestList) {
                        savedInterests = savedInterests + (p.getSavedAmount() - p.getUsedAmount());
                    }
                    if (savedInterests - withdrawal.getAmount() < 0) {
                        investmentRemain = true;
                    }

                    for (UserPoints interest : profitList) {
                        int interestAmount = interest.getSavedAmount() - interest.getUsedAmount();
                        long remainedAmount = amount;
                        amount = interestAmount - amount;
                        if (amount > 0) { // 수익금 엔터티의 수익금이 요청금액 보다 많으면 for문 탈출
                            interest.usePoint(true, ((int) remainedAmount + interest.getUsedAmount()));
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, interest, (int)remainedAmount)
                            );
                            break;
                        } else if (amount == 0) { // 수익금 엔터티의 수익금이 요청금액과 같다면 for문 탈출
                            interest.usePoint(false, interest.getSavedAmount());
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, interest, (int)remainedAmount)
                            );
                            break;
                        } else if (amount < 0) { // 수익금 엔터티의 수익금보다 출금요청금액이 더 많을 때
                            amount = amount * -1;
                            interest.usePoint(false, interest.getSavedAmount());
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, interest, (int)remainedAmount)
                            );
                        }
                    }
                }

                // 3. 모든 수익금과 이자를 더한 값보다 출금요청금액이 더 많은 경우 투자금에서 출금 시키기
                /*
                if (investmentRemain == true) {
                    List<UserPoints> investmentList = userPointsRepository.findAllByUserDetailsAndSupplierAndUsable(userDetails, PointSupplier.INVESTMENT, true);
                    long totalInvestment = 0;
                    for (UserPoints userPoints : investmentList) {
                        totalInvestment = (userPoints.getSavedAmount() - userPoints.getUsedAmount()) + totalInvestment;
                    }
                    totalInvestment = totalInvestment - amount;
                    for (UserPoints investment : investmentList) {
                        int investmentAmount = investment.getSavedAmount() - investment.getUsedAmount();
                        amount = investmentAmount - amount;
                        if (amount > 0) { // 수익금 엔터티의 수익금이 요청금액 보다 많으면 for문 탈출
                            investment.usePoint(true, ((int) amount + investment.getUsedAmount()));
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, investment, (int)amount)
                            );
                            break;
                        } else if (amount == 0) { // 수익금 엔터티의 수익금이 요청금액과 같다면 for문 탈출
                            investment.usePoint(false, investment.getSavedAmount());
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, investment, (int)amount)
                            );
                            break;
                        } else if (amount < 0) { // 수익금 엔터티의 수익금보다 출금요청금액이 더 많을 때
                            amount = amount * -1;
                            investment.usePoint(false, investment.getSavedAmount());
                            saveUsedPointsDetailsList.add(
                                    UsedPointsDetails.create(usedPoints, investment, (int)amount)
                            );
                        }
                    }
                    // UserDetails 엔터티의 investment 컬럼 값 수정
                    userDetails.updateInvestment(totalInvestment);
                }
                */
                GregorianCalendar today = new GregorianCalendar();
                String content = today.get(today.YEAR) + "년 " + (today.get(today.MONTH) + 1) + "월 " + today.get(today.DAY_OF_MONTH) + "일 출금 신청 승인";

                // 수익상세 로그 저장
                Optional<ProfitDetailsLog> recentLog = profitDetailsLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(withdrawal.getUserDetails());
                long logTotal = recentLog.get().getTotal() - withdrawal.getAmount();

                profitDetailsLogRepository.save(
                        ProfitDetailsLog.createByWithdrawal(
                                withdrawal,
                                userDetails,
                                (int) withdrawal.getAmount(),
                                logTotal,
                                content)
                );

                // 포인트사용내역 테이블 저장
                usedPointsRepository.save(usedPoints);
                // 포인트사용내역상세 테이블 저장
                usedPointsDetailsRepository.saveAll(saveUsedPointsDetailsList);

                long totalSavedProfit = userPointsRepository.remainedTotalSavedAmountForUser(userDetails);
                long totalUsedProfit = userPointsRepository.remainedTotalUsedAmountForUser(userDetails);
                long totalBalance = totalSavedProfit - totalUsedProfit;
//                System.out.println(">>>" + totalBalance);
//                System.out.println("!!!" + amount);
//                totalBalance = totalBalance - amount;
//                System.out.println("<<<" + totalBalance);
                withdrawal.accept(totalBalance);

            }else if(withdrawal.getSource() == 1) { // 출금 출처가 적립금인 경우,
                List<Reserves> reservesList = reservesRepository.findAllByUserDetailsAndUsable(userDetails, true);
                for(Reserves reserves : reservesList) {
                    int reserveAmount = reserves.getSavedAmount() - reserves.getUsedAmount();
                    long remainedAmount = amount;
                    amount = reserveAmount - amount;
                    if (amount > 0) { // 수익금 엔터티의 수익금이 요청금액 보다 많으면 for문 탈출
                        reserves.usePoint(true, ((int) remainedAmount + reserves.getUsedAmount()));
                        break;
                    } else if (amount == 0) { // 수익금 엔터티의 수익금이 요청금액과 같다면 for문 탈출
                        reserves.usePoint(false, reserves.getSavedAmount());
                        break;
                    } else if (amount < 0) { // 수익금 엔터티의 수익금보다 출금요청금액이 더 많을 때
                        amount = amount * -1;
                        reserves.usePoint(false, reserves.getSavedAmount());
                    }
                }
                GregorianCalendar today = new GregorianCalendar();
                String content = today.get(today.YEAR) + "년 " + (today.get(today.MONTH) + 1) + "월 " + today.get(today.DAY_OF_MONTH) + "일 적립금 출금 신청 승인";

                int residual = 0; // 잔여적립금 (총 적립금)
                Optional<ReservesLog> recentReservesLog = reservesLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(userDetails);
                if(recentReservesLog.isPresent()) {
                    residual = (int) (recentReservesLog.get().getResidual() - withdrawal.getAmount());
                }

                reservesLogRepository.save(
                        ReservesLog.useLog(
                                userDetails,
                                content,
                                (int) withdrawal.getAmount(),
                                residual
                        )
                );

                long totalSavedReserves = reservesRepository.totalSavedAmount(userDetails);
                long totalUsedReserves = reservesRepository.totalUsedAmount(userDetails);
                long totalResidual = totalSavedReserves - totalUsedReserves;
//                System.out.println(">>>" + totalResidual);
//                System.out.println(">>>" + amount);
//                totalResidual = totalResidual - amount;
                withdrawal.accept(totalResidual);
            }


        } else if (request.getAccepted()==2) { // 거절 시
            withdrawal.refuse(request.getComment());
            if(withdrawal.getSource()==1) {
                long totalSavedReserves = reservesRepository.totalSavedAmount(userDetails);
                long totalUsedReserves = reservesRepository.totalUsedAmount(userDetails);
                long totalResidual = totalSavedReserves - totalUsedReserves;

                reservesLogRepository.save(
                        ReservesLog.failLog(
                                userDetails,
                                request.getComment(),
                                (int) withdrawal.getAmount(),
                                (int)totalResidual
                        )
                );
            }
        }

    }

}
