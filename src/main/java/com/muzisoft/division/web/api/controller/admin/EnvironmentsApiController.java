package com.muzisoft.division.web.api.controller.admin;

import com.muzisoft.division.domain.setup.Environments;
import com.muzisoft.division.domain.setup.EnvironmentsRepository;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerKindsSaveAndUpdateRequest;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerSaveRequest;
import com.muzisoft.division.web.api.dto.admin.manager.PermissionSaveRequest;
import com.muzisoft.division.web.api.dto.admin.member.GradeSaveAndUpdateRequest;
import com.muzisoft.division.web.api.dto.common.EnvironmentsUpdateRequest;
import com.muzisoft.division.web.service.AuthService;
import com.muzisoft.division.web.service.admin.DuesServiceAdmin;
import com.muzisoft.division.web.service.admin.GradeServiceAdmin;
import com.muzisoft.division.web.service.admin.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/environments")
public class EnvironmentsApiController {

    private final EnvironmentsRepository environmentsRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;
    private final AuthService authService;
    private final DuesServiceAdmin duesServiceAdmin;
    private final GradeServiceAdmin gradeServiceAdmin;
    private final ManagerService managerService;

    /** 1. 회원등급추가(등록) **/
    @PostMapping("/grade")
    public void gradeSave(@RequestBody GradeSaveAndUpdateRequest request) {
        gradeServiceAdmin.create(request);
    }

    /** 2.환경설정(등록) **/
    @PostMapping
    public void saveEnvironments(@RequestBody EnvironmentsUpdateRequest request) {
        environmentsRepository.save(
                Environments.create(
                        request.getDuesDate(),
                        request.getDuesAmount(),
                        request.getInterestRate(),
                        request.getConversionRate(),
                        request.getRecommenderDividendRate(),
                        request.getCuttingRate(),
                        request.getTotalBalance()
                )
        );
    }

    /** 3. 관리자 접근 주소 추가 (초기 설정) **/
    @PostMapping("/managers/permission")
    public void addPermissionUrl(@RequestBody PermissionSaveRequest request) {
        managerService.createPermissionUrl(request);
    }

    /** 4. 관리자 분류(등급) 추가 (초기 대표관리자 설정용) **/
    @PostMapping("/managers/kinds")
    public void addManagerKinds(@RequestBody ManagerKindsSaveAndUpdateRequest request) {
        managerService.createManagerKinds(request);
    }

    /** 5. 초기 관리자 생성**/
    @PostMapping("/admin")
    public void createAdmin(ManagerSaveRequest saveRequest) {
        saveRequest.setPassword(passwordEncoder.encode(saveRequest.getPassword()));
        authService.signupManager(saveRequest, null);
    }

    /** 회비 생성 테스트 호출용 **/
    @PostMapping("/dues")
    public void testDues() {
        duesServiceAdmin.createDuesTest();
    }
}
