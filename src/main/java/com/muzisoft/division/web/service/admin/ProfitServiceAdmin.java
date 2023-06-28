package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.board.AccountBook;
import com.muzisoft.division.domain.board.AccountBookRepository;
import com.muzisoft.division.domain.common.enums.AccountBookType;
import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.point.UserPoints;
import com.muzisoft.division.domain.point.UserPointsRepository;
import com.muzisoft.division.domain.profit.*;
import com.muzisoft.division.domain.reverses.Reserves;
import com.muzisoft.division.domain.reverses.ReservesLog;
import com.muzisoft.division.domain.reverses.ReservesLogRepository;
import com.muzisoft.division.domain.reverses.ReservesRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserDetailsRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.profit.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfitServiceAdmin {

    private final ExpectedProfitRepository expectedProfitRepository;
    private final ExpectedProfitDetailsRepository expectedProfitDetailsRepository;
    private final ProfitDetailsLogRepository profitDetailsLogRepository;
    private final ProfitRepository profitRepository;
    private final ProfitDetailsRepository profitDetailsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final EnvironmentsRepository environmentsRepository;
    private final UserPointsRepository userPointsRepository;
    private final AccountBookRepository accountBookRepository;
    private final RemainderLogRepository remainderLogRepository;
    private final ReservesRepository reservesRepository;
    private final ReservesLogRepository reservesLogRepository;

    // 예상(가상) 수익 등록
    @Transactional
    public void createExpectedProfit(ExpectedProfitSaveAndUpdateRequest request) throws ParseException {
        Optional<ExpectedProfit> foundExpectedProfit = expectedProfitRepository.findByReferenceYearAndReferenceMonth(request.getReferenceYear(), request.getReferenceMonth());
        long totalProfit = request.getAmount(); // 총 수익금

        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        int conversionRate = environments.getConversionRate(); // 레벨 당 전환 금액
        int cuttingRate = environments.getCuttingRate(); // 절삭 비율

        // 기준 년, 월의 기존 예상수익 데이터가 있을 경우 기존 기준 년, 월의 데이터 삭제
        if(foundExpectedProfit.isPresent()) {
            expectedProfitDetailsRepository.deleteAllByExpectedProfit(foundExpectedProfit.get()); // 상세정보 테이블 레코드 삭제
            expectedProfitRepository.delete(foundExpectedProfit.get()); // 전체정보 테이블 레코드 삭제
        }

        // 1. 예상 수익 전체 테이블 레코드 생성
        ExpectedProfit expectedProfit = ExpectedProfit.create(totalProfit, request.getReferenceYear(), request.getReferenceMonth());

        List<UserDetails> userDetailsList = userDetailsRepository.userDetailsList();
        List<ExpectedProfitDetails> saveDetailsList = new ArrayList<>();

        // 2. 환산 배당률의 총합 구하기
        float totalRatio = 0; // 환산 배당률의 총합
        for(UserDetails userDetails : userDetailsList) {
            int userLevel = (int)(userDetails.getInvestment() / conversionRate);
            float gradeRatio = userDetails.getGrade().getPercent();
            totalRatio = totalRatio + (userLevel * gradeRatio);
        }

        // 3. 각 회원의 레벨과 등급에 따른 배당률로 수익금 분배 (예상수익상세 테이블 레코드 생성)
        int remainder = 0;
        for(UserDetails userDetails : userDetailsList) {
            int userLevel = (int)(userDetails.getInvestment() / conversionRate); // 회원 레벨
            float gradeRatio = userDetails.getGrade().getPercent(); // 회원 등급에 따른 배당률
            float userOdds = userLevel * gradeRatio; // 레벨 * 등급 배당률 = 최종 배당률

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int year = request.getReferenceYear();
            int month = request.getReferenceMonth();
            if(month==12) {
                year = year+1;
                month = 1;
            }else {
                month = month+1;
            }
            String strYear = Integer.toString(year);
            String strMonth = Integer.toString(month);
            if(strMonth.length()==1) {
                strMonth = "0"+strMonth;
            }
            String strDate = strYear+"-"+strMonth+"-01";
            Date dateTo = sdf.parse(strDate);
            TotalPointsResponse totalPointsResponse = userPointsRepository.totalPointsByUserDetailsAndPointsSupplierAndDateTo(userDetails, PointSupplier.PROCEEDS, dateTo);

            long userTotal = totalPointsResponse.getSavedAmount();

            // 회원의 배당률 만큼 분배 후 절삭 비율만큼 절삭 (정수)
            int share = (int) (totalProfit * userOdds / totalRatio) / cuttingRate;

            // 몫을 절삭 비율 만큼 곱하여 원래 수익금으로 만들기
            int amount = share * cuttingRate;

            // 절삭 비율만큼 절삭 후의 나머지 잔액의 총합 (관리자 관리 금액으로 저장)
            remainder = (int)((totalProfit * userOdds / totalRatio) % cuttingRate) + remainder;

//            System.out.println(userDetails.getSeq() + ": " + share);
//            System.out.println("remainder: " + remainder);

            // 이전까지의 총금액에 추가된 배당금 더하기
            userTotal = userTotal + amount;

            saveDetailsList.add(
                    ExpectedProfitDetails.create(
                            expectedProfit,
                            userDetails,
                            userLevel,
                            userTotal,
                            amount
                    )
            );
        }

/*        System.out.println("!!! "+saveDetailsList.size());
        System.out.println(saveDetailsList);
        for(ExpectedProfitDetails details:saveDetailsList) {
            System.out.println(">>>");
            System.out.println(details);
        }*/
//        System.out.println(">>>");
        System.out.println(expectedProfit);

        // 4. 최종 엔터티 DB 저장
        expectedProfitRepository.save(expectedProfit);
        expectedProfitDetailsRepository.saveAll(saveDetailsList);


    }

    // 월별 가상(예상) 수익
    @Transactional
    public Page<MonthlyExpectedProfitsResponse> monthlyExpectedProfits(Pageable pageable, CommonCondition condition) {
        return expectedProfitRepository.monthlyExpectedProfitList(pageable, condition);
    }

    // 가상수익목록(페이징)
    public Page<ExpectedProfitListResponse> expectedProfitList(Pageable pageable, ExpectedProfitRequest request,
                                                               ProfitCondition profitCondition, CommonCondition condition) {
        ExpectedProfit foundExpectedProfit = expectedProfitRepository.findByReferenceYearAndReferenceMonth(request.getYear(), request.getMonth())
                .orElseThrow(CEntityNotFoundException.CExpectedProfitNotFoundException::new);
        return expectedProfitDetailsRepository.expectedProfitList(pageable, request, profitCondition, condition, foundExpectedProfit);
    }

    // 지난 수익 리스트
    @Transactional
    public Page<LastProfitListResponse> lastProfitList(Pageable pageable, CommonCondition condition) {
        return profitRepository.lastProfitList(pageable, condition);
    }

    // 수익 등록
    @Transactional
    public void createProfit(ProfitSaveRequest request) {
        long totalProfit = request.getAmount(); // 입력한 총 수익금
        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        int conversionRate = environments.getConversionRate(); // 레벨 당 전환 금액
        int cuttingRate = environments.getCuttingRate(); // 절삭 비율
        int recommenderDividedRate = environments.getRecommenderDividendRate();
        int yield = 0; // 수익률

        // 1. 가장 최신 수익 값 가져오기
        Optional<Profit> recentProfit = profitRepository.findTopByOrderByCreatedAtDesc();
        long lastProfit = 0;
        // 2. 직전 수익 대비 수익률 구하기
        if(recentProfit.isPresent()) {
            lastProfit = recentProfit.get().getAmount();
            long difference = (totalProfit - lastProfit);
            float diff = (float) difference;
            float last = (float) lastProfit;
            float quotient = diff/last * 100;
            yield = (int) quotient;
        }else {
            yield = 0;
        }
        // 3. 수익 테이블 값 생성
        Profit profit = Profit.create(request.getAmount(), yield);

        GregorianCalendar today = new GregorianCalendar();
        String content = today.get(today.YEAR) + "년 " + (today.get(today.MONTH)+1) + "월 " + today.get(today.DAY_OF_MONTH) + "일 수익 배분";

        // 4. 환산 배당률의 총합 구하기
        List<UserDetails> userDetailsList = userDetailsRepository.userDetailsList();
        float totalRatio = 0; // 환산 배당률의 총합
        for(UserDetails userDetails : userDetailsList) {
            int userLevel = (int)(userDetails.getInvestment() / conversionRate);
            float gradeRatio = userDetails.getGrade().getPercent();
            totalRatio = totalRatio + (userLevel * gradeRatio);
        }

        // 5. 각 회원의 레벨과 등급에 따른 배당률로 수익금 분배 (수익상세 엔터티 + 수익상세로그 엔터티 + 포인트 엔터티 생성)
        List<ProfitDetails> saveProfitDetailsList = new ArrayList<>(); // 수익상세
        List<ProfitDetailsLog> saveProfitDetailsLogList = new ArrayList<>(); // 수익상세로그
        List<UserPoints> saveUserPointsList = new ArrayList<>(); // 사용자포인트
        List<Reserves> saveReservesList = new ArrayList<>(); // 친구 추천 적립금
        List<ReservesLog> saveReservesLogList = new ArrayList<>(); // 친구 추천 적립금 로그
        int remainder = 0; // 잔액

        for(UserDetails userDetails : userDetailsList) {
            int userLevel = (int)(userDetails.getInvestment() / conversionRate); // 회원 레벨
            float gradeRatio = userDetails.getGrade().getPercent(); // 회원 등급에 따른 배당률
            float userOdds = userLevel * gradeRatio; // 레벨 * 등급 배당률 = 최종 배당률

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            int year = request.getReferenceYear();
//            int month = request.getReferenceMonth();
//            if(month==12) {
//                year = year+1;
//                month = 1;
//            }else {
//                month = month+1;
//            }
//            String strYear = Integer.toString(year);
//            String strMonth = Integer.toString(month);
//            if(strMonth.length()==1) {
//                strMonth = "0"+strMonth;
//            }
//            String strDate = strYear+"-"+strMonth+"-01";
            Date dateTo = new Date();
            TotalPointsResponse totalPointsResponse = userPointsRepository.totalPointsByUserDetailsAndPointsSupplierAndDateTo(userDetails, PointSupplier.PROCEEDS, dateTo);

            long userTotal = totalPointsResponse.getSavedAmount();

            // 회원의 배당률 만큼 분배 후 절삭 비율만큼 절삭 (정수)
            int share = (int) (totalProfit * userOdds / totalRatio) / cuttingRate;

            // 몫을 절삭 비율 만큼 곱하여 원래 수익금으로 만들기
            int amount = share * cuttingRate;

            // 절삭 비율만큼 절삭 후의 나머지 잔액의 총합 (관리자 관리 금액으로 저장)
            remainder = (int)((totalProfit * userOdds / totalRatio) % cuttingRate) + remainder;

//            System.out.println(userDetails.getSeq() + ": " + share);
//            System.out.println("remainder: " + remainder);

            // 이전까지의 총금액에 추가된 배당금 더하기
            userTotal = userTotal + amount;


            // 추천인이 있을 시 추천인에게 적립금 지급 및 적립금 로그 저장 + 분배 수익금 조정
            if(userDetails.getRecommender()!=null && !userDetails.getRecommender().equals("")) {
                UserDetails recommender = userDetails.getRecommender().getUserDetails();
                int reserves = amount * recommenderDividedRate / 100; // 적립금
                amount = (amount * (100-recommenderDividedRate)) / 100; // 적립금 만큼 제한 분배 수익금

                int residual = 0; // 잔여적립금 (총 적립금)
                Optional<ReservesLog> recentReservesLog = reservesLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(recommender);
                if(recentReservesLog.isPresent()) {
                    residual = recentReservesLog.get().getResidual() + reserves;
                }else {
                    residual = reserves;
                }

                // 적립금 저장 대기 리스트
                saveReservesList.add(
                        Reserves.saveByProfit(recommender, reserves)
                );

                // 적립금 로그 저장 대기 리스트
                String reservesLogContent = "추천인 수익" + recommenderDividedRate + "% 지급";
                saveReservesLogList.add(
                        ReservesLog.saveLog(recommender, reservesLogContent, reserves, residual)
                );
            }

            // 수익 상세 저장
            ProfitDetails profitDetails =  ProfitDetails.create(
                    profit,
                    userDetails,
                    userLevel,
                    userTotal,
                    amount
            );

            saveProfitDetailsList.add(
                   profitDetails
            );

            // 수익상세 로그 저장
            Optional<ProfitDetailsLog> recentLog = profitDetailsLogRepository.findTopByUserDetailsOrderByCreatedAtDesc(userDetails);
            long logTotal = 0;
            if(recentLog.isPresent()) {
                logTotal = recentLog.get().getTotal() + amount;
            }else {
                logTotal = amount;
            }

            saveProfitDetailsLogList.add(
                    ProfitDetailsLog.createByProfit(
                            profitDetails,
                            userDetails,
                            amount,
                            logTotal,
                            content
                    )
            );

            // 사용자 포인트 저장
            saveUserPointsList.add(
                    UserPoints.create(
                            userDetails,
                            PointSupplier.PROCEEDS,
                            amount
                    )
            );
        } // for 문 종료

        profitRepository.save(profit); // 수익
        profitDetailsRepository.saveAll(saveProfitDetailsList); // 수익 상세
        profitDetailsLogRepository.saveAll(saveProfitDetailsLogList); // 수익 상세 로그
        userPointsRepository.saveAll(saveUserPointsList); // 사용자 포인트
        environments.updateTotalBalance(environments.getTotalBalance()+remainder); // 환경 설정 총 잔액 수정
        remainderLogRepository.save( // 잔액 로그
                RemainderLog.create(
                   profit,
                   totalProfit,
                   remainder
                ));
        reservesRepository.saveAll(saveReservesList);
        reservesLogRepository.saveAll(saveReservesLogList);
    }

    // 회원별 수익 리스트
    @Transactional
    public Page<ProfitListResponse> profitList(Pageable pageable, ProfitCondition profitCondition, CommonCondition condition, String profitSeq) {
        Profit profit = EntityUtils.profitThrowable(profitRepository, profitSeq);
        return profitDetailsRepository.profitList(pageable, profitCondition, condition, profit);
    }

    // 회원별 수익 상세(리스트)
    public Page<ProfitDetailsPerUser> profitDetailsPerUsers(Pageable pageable, CommonCondition condition, String userDetailsSeq) {
        UserDetails userDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);

        return profitDetailsRepository.profitDetailsPerUser(pageable, condition, userDetails);
    }
}
