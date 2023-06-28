package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.manager.ManagerKinds;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.dues.*;
import com.muzisoft.division.web.api.dto.admin.manager.*;
import com.muzisoft.division.web.api.dto.admin.member.*;
import com.muzisoft.division.web.api.dto.common.CommonCondition;
import com.muzisoft.division.web.api.dto.user.SignupRequest;
import com.muzisoft.division.web.service.AuthService;
import com.muzisoft.division.web.service.admin.DuesServiceAdmin;
import com.muzisoft.division.web.service.admin.GradeServiceAdmin;
import com.muzisoft.division.web.service.admin.ManagerService;
import com.muzisoft.division.web.service.admin.UserServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final AuthService authService;
    private final GradeServiceAdmin gradeServiceAdmin;
    private final UserServiceAdmin userServiceAdmin;
    private final DuesServiceAdmin duesServiceAdmin;
    private final ManagerService managerService;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;

    /** 관리자 회원 목록 **/
    @GetMapping("/managers")
    public List<ManagerUserDetailsResponse> managerUserList() {
        return managerService.managerUserList();
    }

    /** 관리자 회원 추가 **/
    @PostMapping("/managers")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(ManagerSaveRequest saveRequest) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(saveRequest.getProfilePicture()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "manager-img"));
        });

        try {
            saveRequest.setPassword(passwordEncoder.encode(saveRequest.getPassword()));
            authService.signupManager(saveRequest, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 관리자 회원 상세 **/
    @GetMapping("/managers/{managerUsersSeq}")
    public ManagerUserDetailsResponse managerUserDetails(@PathVariable("managerUsersSeq") String managerUsersSeq) {
        return managerService.managerUserDetails(managerUsersSeq);
    }

    /** 관리자 회원 수정 **/
    @PutMapping("/managers/{managerUsersSeq}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateManagerUsers(@PathVariable("managerUsersSeq") String managerUsersSeq, ManagerUpdateRequest request) {
        final List<EFileManager> fileManagers = new ArrayList<>();

        Optional.ofNullable(request.getProfilePicture()).ifPresent(multipartFile -> {
            fileManagers.addAll(fileHandler.parse(Collections.singletonList(multipartFile), "manager-img"));
        });

        try {
            managerService.updateManagerUser(managerUsersSeq, request, fileManagers.stream().findAny().orElse(null));
        } catch (Exception e) {
            fileManagers.stream().findAny().ifPresent(FileManagerUtils::delete);
            throw e;
        }
    }

    /** 관리자 회원 삭제 **/
    @DeleteMapping("/managers/{managerUsersSeq}")
    public void deleteManagerUsers(@PathVariable("managerUsersSeq") String managerUsersSeq) {
        managerService.deleteManagerUser(managerUsersSeq);
    }

    /** 관리자 회원 등급만 수정 221007 추가 **/
    @PutMapping("/managers/{managerUsersSeq}/grade")
    public void updateManagerUsersGrade(@PathVariable("managerUsersSeq") String managerUsersSeq, String managerKindsSeq) {
        managerService.updateMangerUserGrade(managerUsersSeq, managerKindsSeq);
    }

    /** 관리자 접근 주소 추가 **/
    @PostMapping("/managers/permission")
    public void addPermissionUrl(@RequestBody PermissionSaveRequest request) {
        managerService.createPermissionUrl(request);
    }

    /** 관리자 분류(등급) 목록 **/
    @GetMapping("/managers/kinds")
    public List<ManagerKinds> managerKindsList() {
        return managerService.managerKindsList();
    }

    /** 관리자 분류(등급) 추가 **/
    @PostMapping("/managers/kinds")
    public void addManagerKinds(@RequestBody ManagerKindsSaveAndUpdateRequest request) {
        managerService.createManagerKinds(request);
    }

    /** 관리자 분류(등급) 수정 **/
    @PutMapping("/managers/kinds/{managerKindsSeq}")
    public void updateManagerKinds(@PathVariable("managerKindsSeq") String managerKindsSeq, @RequestBody ManagerKindsSaveAndUpdateRequest request) {
        managerService.updateManagerKinds(managerKindsSeq, request);
    }

    /** 관리자 분류(등급) 추가 **/
    @DeleteMapping("/managers/kinds/{managerKindsSeq}")
    public void deleteManagerKinds(@PathVariable("managerKindsSeq") String managerKindsSeq) {
        managerService.deleteManagerKinds(managerKindsSeq);
    }

    /** 회원등급목록 **/
    @GetMapping("/grade")
    public List<GradeListResponse> gradeList() {
        return gradeServiceAdmin.gradeList();
    }

    /** 회원등급수정 **/
    @PutMapping("/grade/{seq}")
    public void gradeUpdate(@PathVariable("seq") int seq, @RequestBody GradeSaveAndUpdateRequest request) {
        gradeServiceAdmin.update(seq, request);
    }

    /** 회원목록 **/
    @GetMapping("/users")
    public UserListResponse userList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                           UserCondition userCondition, CommonCondition condition) {
        return userServiceAdmin.userList(pageable, userCondition, condition);
    }

    /**
     * 회원등급변경
     **/
    @PutMapping("/users/{userDetailsSeq}/grade")
    public void userGradeUpdate(@PathVariable("userDetailsSeq") String userDetailsSeq, @RequestBody UserGradeUpdateRequest request) {
        userServiceAdmin.userGradeUpdate(userDetailsSeq, request);
    }

    /** 회원탈퇴 **/
    @DeleteMapping("/users/{userDetailsSeq}")
    public void withdrawalByAdmin(@PathVariable("userDetailsSeq") String userDetailsSeq, @RequestBody UserWithdrawalRequest request) {
        userServiceAdmin.withdrawalByAdmin(userDetailsSeq, request);
    }

    /** 회원상세 **/
    @GetMapping("/users/{userDetailsSeq}")
    public UserDetailsResponseInAdmin userDetailsInAdmin(@PathVariable("userDetailsSeq") String userDetailsSeq) {
        return userServiceAdmin.userDetailsInAdmin(userDetailsSeq);
    }

    /** 가입대기목록 **/
    @GetMapping("/waiting")
    public Page<WaitingListResponse> waitingList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                 CommonCondition condition) {
        return userServiceAdmin.waitingList(pageable, condition);
    }

    /** 가입대기회원상세 **/
    @GetMapping("/waiting/{userDetailsSeq}")
    public WaitingUserDetailsResponse waitingUserDetails(@PathVariable("userDetailsSeq") String userDetailsSeq) {
        return userServiceAdmin.waitingUserDetails(userDetailsSeq);
    }

    /** 가입대기처리 **/
    @PutMapping("/waiting/{userDetailsSeq}")
    public void userApproval(@PathVariable("userDetailsSeq") String userDetailsSeq, @RequestBody UserApprovalRequest request) {
        userServiceAdmin.approval(userDetailsSeq, request);
    }

    /** 회비입금통계 **/
    @GetMapping("/dues/total")
    public DuesStatsResponse duesStats(int year, int month) {
        return duesServiceAdmin.duesStats(year, month);
    }

    /** 회비입금목록(페이징) **/
    @GetMapping("/dues")
    public DuesListResponse duesList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, DuesCondition duesCondition, CommonCondition condition, int year, int month) {
        return duesServiceAdmin.duesList(pageable, duesCondition, condition, year, month);
    }

    /** 회비납부확인요청목록(페이징) **/
    @GetMapping("/dues/waiting")
    public Page<DuesListResponse.DuesList> duesWaitingList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, DuesCondition duesCondition, CommonCondition condition) {
        return duesServiceAdmin.duesWaitingList(pageable, duesCondition, condition);
    }

    /** 회비납부상세 **/
    @GetMapping("/dues/{duesSeq}")
    public DuesDetailsResponse duesDetails(@PathVariable("duesSeq") String duesSeq) {
        return duesServiceAdmin.duesDetails(duesSeq);
    }

    /** 회비납부처리 **/
    @PutMapping("/dues/{duesSeq}")
    public void updatePaymentState(@PathVariable("duesSeq") String duesSeq, @RequestBody DuesUpdatePaymentStateRequest request) {
        duesServiceAdmin.updatePaymentState(duesSeq, request);
    }

    /** 미납알림발송 **/
    @PostMapping("/dues/alarm")
    public void createDuesPush(@RequestBody DuesPushSaveRequest request) {
        duesServiceAdmin.createDuesPush(request);
    }
}
