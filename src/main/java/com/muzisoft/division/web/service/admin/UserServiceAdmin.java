package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import com.muzisoft.division.domain.point.UserPointsRepository;
import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.admin.member.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceAdmin {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final GradeRepository gradeRepository;
    private final EnvironmentsRepository environmentsRepository;
    private final UserPointsRepository userPointsRepository;

    // 회원목록(페이징)
    @Transactional
    public UserListResponse userList(Pageable pageable, UserCondition userCondition, CommonCondition condition) {
        UserListResponse response = new UserListResponse();

        List<Environments> environmentsList = environmentsRepository.findAll();
        if(environmentsList.size()==0) {
            throw new CEntityNotFoundException.CEnvironmentsNotFoundException();
        }
        Environments environments = environmentsList.get(0);

        int conversionRate = environments.getConversionRate();

        Page<UserListResponse.UserList> userList = userDetailsRepository.userList(pageable, userCondition, condition);

        for(UserListResponse.UserList user : userList) {
            UserDetails foundUserDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, user.getUserDetailsSeq());
            long totalInvestments = foundUserDetails.getInvestment();
            long level = totalInvestments/conversionRate;
            user.setLevel(level + "Level");
        }

        response.setUserList(userList);
        response.setUserCount(userDetailsRepository.userCount());

        return response;
    }

    // 회원등급변경
    @Transactional
    public void userGradeUpdate(String userDetailsSeq, UserGradeUpdateRequest request) {
        UserDetails foundUserDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);
        Grade foundGrade = EntityUtils.gradeThrowable(gradeRepository, request.getGradeSeq());
        User foundUser = userRepository.findByUserDetails(foundUserDetails).orElseThrow(CEntityNotFoundException.CUserNotFoundException::new);
        if(foundUser.isWithdrawal()) {
            foundUser.cancelWithdrawal();
        }
        foundUserDetails.gradeUpdate(foundGrade);
    }

    // 회원상세
    @Transactional
    public UserDetailsResponseInAdmin userDetailsInAdmin(String userDetailsSeq) {
        UserDetailsResponseInAdmin response = userDetailsRepository.userDetailsInAdmin(userDetailsSeq);
        return response;
    }

    // 회원탈퇴
    @Transactional
    public void withdrawalByAdmin(String userDetailsSeq, UserWithdrawalRequest request) {
        UserDetails foundUserDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);
        User foundUser = userRepository.findByUserDetails(foundUserDetails).orElseThrow(CEntityNotFoundException.CUserNotFoundException::new);

        foundUser.withdrawal(request.getWithdrawalReason());
    }

    // 가입대기목록(페이징)
    @Transactional
    public Page<WaitingListResponse> waitingList(Pageable pageable, CommonCondition condition) {
        return userDetailsRepository.waitingList(pageable, condition);
    }

    // 가입대기회원상세
    @Transactional
    public WaitingUserDetailsResponse waitingUserDetails(String userDetailsSeq) {
        WaitingUserDetailsResponse response = new WaitingUserDetailsResponse();
        WaitingUserDetailsResponse.User user = userDetailsRepository.waitingUserDetails(userDetailsSeq);
        response.setUserDetails(user);
        if(user.getRecommenderSeq()!=null && !user.getRecommenderSeq().equals("")) {
            User foundUser = EntityUtils.userThrowable(userRepository, user.getRecommenderSeq());
            response.setRecommenderDetails(userDetailsRepository.waitingUserRecommenderDetails(foundUser.getUserDetails().getSeq()));
        }
        return response;
    }


    // 가입대기처리
    @Transactional
    public void approval(String userDetailsSeq, UserApprovalRequest request) {
        UserDetails foundUserDetails = EntityUtils.userDetailsThrowable(userDetailsRepository, userDetailsSeq);

        foundUserDetails.approval(request.getAccepted());
    }
}
