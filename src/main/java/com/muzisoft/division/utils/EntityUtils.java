package com.muzisoft.division.utils;

import com.muzisoft.division.domain.board.*;
import com.muzisoft.division.domain.dues.Dues;
import com.muzisoft.division.domain.dues.DuesRepository;
import com.muzisoft.division.domain.manager.*;
import com.muzisoft.division.domain.point.*;
import com.muzisoft.division.domain.profit.Profit;
import com.muzisoft.division.domain.profit.ProfitRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException.CUserNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

public class EntityUtils {
    public static User userThrowable(UserRepository userRepository) {
        return userRepository.findById(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSeq())
                .orElseThrow(CUserNotFoundException::new);
    }
    public static User userThrowable(UserRepository userRepository, String seq) {
        return userRepository.findById(seq)
                .orElseThrow(CUserNotFoundException::new);
    }
    public static User userThrowable(UserRepository userRepository, UserDetails userDetails) {
        return userRepository.findByUserDetails(userDetails)
                .orElseThrow(CUserNotFoundException::new);
    }
    public static UserDetails userDetailsThrowable(UserDetailsRepository userDetailsRepository, String seq) {
        return userDetailsRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CUserDetailsNotFoundException::new);
    }
    public static Grade gradeThrowable(GradeRepository gradeRepository, int seq) {
        return gradeRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CGradeNotFoundException::new);
    }
    public static Investment investmentThrowable(InvestmentRepository investmentRepository, String seq) {
        return investmentRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CInvestmentNotFoundException::new);
    }
    public static Environments environmentsThrowable(EnvironmentsRepository environmentsRepository, long seq) {
        return environmentsRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CEnvironmentsNotFoundException::new);
    }
    public static Dues duesThrowable(DuesRepository duesRepository, String seq) {
        return duesRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CDuesNotFoundException::new);
    }
    public static ManagerKinds managerKindsThrowable(ManagerKindsRepository managerKindsRepository, String seq) {
        return managerKindsRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CManagerKindsNotFoundException::new);
    }
    public static ManagerUsers managerUsersThrowable(ManagerUsersRepository managerUsersRepository, String seq) {
        return managerUsersRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CManagerUserNotFoundException::new);
    }
    public static Profit profitThrowable(ProfitRepository profitRepository, String seq) {
        return profitRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CProfitNotFoundException::new);
    }
    public static Withdrawal withdrawalThrowable(WithdrawalRepository withdrawalRepository, String seq) {
        return withdrawalRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CWithdrawalNotFoundException::new);
    }
    public static Interest interestThrowable(InterestRepository interestRepository, String seq) {
        return interestRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CInterestNotFoundException::new);
    }
    public static Popup popupThrowable(PopupRepository popupRepository, String seq) {
        return popupRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CPopupNotFoundException::new);
    }
    public static Notice noticeThrowable(NoticeRepository noticeRepository, String seq) {
        return noticeRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CNoticeNotFoundException::new);
    }
    public static Board boardThrowable(BoardRepository boardRepository, String seq) {
        return boardRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CBoardNotFoundException::new);
    }
    public static Faq faqThrowable(FaqRepository faqRepository, String seq) {
        return faqRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CFaqNotFoundException::new);
    }
    public static ManagerPermission ManagerPermissionThrowable(ManagerPermissionRepository managerPermissionRepository, String seq) {
        return managerPermissionRepository.findById(seq)
                .orElseThrow(CEntityNotFoundException.CManagerPermissionNotFoundException::new);
    }
}
