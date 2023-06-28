package com.muzisoft.division.web.service.admin;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.manager.*;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserDetails;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.admin.AdminBasicResponse;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerKindsSaveAndUpdateRequest;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerUpdateRequest;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerUserDetailsResponse;
import com.muzisoft.division.web.api.dto.admin.manager.PermissionSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerPermissionRepository permissionRepository;
    private final ManagerKindsRepository managerKindsRepository;
    private final ManagerUsersRepository managerUsersRepository;
    private final FileHandler fileHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 로그인 관리자 기본 정보
    @Transactional
    public AdminBasicResponse adminBasicResponse() {
        AdminBasicResponse response = new AdminBasicResponse();
        User foundUser = EntityUtils.userThrowable(userRepository);
        Optional<ManagerUsers> foundManagerUser = managerUsersRepository.findByUser(foundUser);
        String imgUrl = null;
        if(foundManagerUser.isPresent()) {
            ManagerUsers managerUsers = foundManagerUser.get();
            ManagerKinds managerKinds = foundManagerUser.get().getKinds();
            if(managerKinds!=null) {
                response.setGrade(managerKinds.getName());
            }else {
                response.setGrade("등급 없음");
            }
            if(!ObjectUtils.isEmpty(managerUsers.getProfilePicture())) {
                imgUrl = fileHandler.fileUrl(managerUsers.getProfilePicture());
            }
        }
        response.setName(foundUser.getUsername());
        response.setProfileImage(imgUrl);
        return response;
    }

    // 관리자 회원 목록
    @Transactional
    public List<ManagerUserDetailsResponse> managerUserList() {
        List<ManagerUserDetailsResponse> responseList = new ArrayList<>();
        List<ManagerUsers> managerUsersList = managerUsersRepository.findAllByDeletedIsFalse();
        List<ManagerUsers> resultList = new ArrayList<>();

        // 대표관리자 제외
        if(managerUsersList.size()>1) {
            for(int i=1; i<managerUsersList.size(); i++) {
                ManagerUsers managerUsers = managerUsersList.get(i);
                resultList.add(managerUsers);
            }

            for(ManagerUsers managerUsers : resultList) {
                String imgUrl = null;
                if(!ObjectUtils.isEmpty(managerUsers.getProfilePicture())) {
                    imgUrl = fileHandler.fileUrl(managerUsers.getProfilePicture());
                }
                ManagerUserDetailsResponse response = new ManagerUserDetailsResponse(managerUsers, imgUrl);
                responseList.add(response);
            }
        }

        return responseList;
    }

    // 관리자 회원 상세
    @Transactional
    public ManagerUserDetailsResponse managerUserDetails(String managerUsersSeq) {
        ManagerUsers managerUsers = EntityUtils.managerUsersThrowable(managerUsersRepository, managerUsersSeq);
        String imgUrl = null;
        if(!ObjectUtils.isEmpty(managerUsers.getProfilePicture())) {
            imgUrl = fileHandler.fileUrl(managerUsers.getProfilePicture());
        }
        return new ManagerUserDetailsResponse(managerUsers, imgUrl);
    }

    // 관리자 회원 수정
    @Transactional
    public void updateManagerUser(String managerUsersSeq, ManagerUpdateRequest request, EFileManager profilePicture) {
        ManagerUsers managerUsers = EntityUtils.managerUsersThrowable(managerUsersRepository, managerUsersSeq);
        User user = managerUsers.getUser();
        UserDetails userDetails = user.getUserDetails();
        ManagerKinds managerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, request.getManagerKindsSeq());
        String password = null;
        if(request.getPassword()!=null && !request.getPassword().equals("")) {
            password = passwordEncoder.encode(request.getPassword());
        }
        user.updateManager(password, request.getName());
        userDetails.updateManager(request.getName(), request.getMobile());
        managerUsers.update(request.getNickName(), managerKinds, profilePicture, request.isDefaultImg());
    }

    // 관리자 회원 삭제
    @Transactional
    public void deleteManagerUser(String managerUsersSeq) {
        ManagerUsers managerUsers = EntityUtils.managerUsersThrowable(managerUsersRepository, managerUsersSeq);
        managerUsers.delete();
        User user = managerUsers.getUser();
        user.withdrawal(null);
    }

    // 관리자 회원 등급만 수정
    @Transactional
    public void updateMangerUserGrade(String managerUsersSeq, String managerKindsSeq) {
        ManagerUsers managerUsers = EntityUtils.managerUsersThrowable(managerUsersRepository, managerUsersSeq);
        ManagerKinds managerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, managerKindsSeq);

        managerUsers.updateGrade(managerKinds);
    }

    // 권한 주소 추가
    @Transactional
    public void createPermissionUrl(PermissionSaveRequest request) {
        List<ManagerPermission> managerPermissionList = new ArrayList<>();

        for(int i=0; i<request.getTitle().size(); i++) {
            ManagerPermission managerPermission = ManagerPermission.create(request.getTitle().get(i), request.getUrl().get(i));
            managerPermissionList.add(managerPermission);
        }

        permissionRepository.saveAll(managerPermissionList);
    }



    // 관리자 분류(등급) 목록
    @Transactional
    public List<ManagerKinds> managerKindsList() {
        List<ManagerKinds> beforeList = managerKindsRepository.findAllByUsableIsTrue();
        List<ManagerKinds> afterList = new ArrayList<>();

        // 대표관리자를 제외.
        if(beforeList.size()>1) {
            for(int i=1; i<beforeList.size(); i++) {
                ManagerKinds managerKinds = beforeList.get(i);
                afterList.add(managerKinds);
            }
        }

        return afterList;
    }

    // 관리자 분류(등급) 추가
    @Transactional
    public void createManagerKinds(ManagerKindsSaveAndUpdateRequest request) {
        managerKindsRepository.save(
                ManagerKinds.create(request.getName(), request.getAbilities())
        );
    }

    // 관리자 분류(등급) 수정
    @Transactional
    public void updateManagerKinds(String managerKindsSeq, ManagerKindsSaveAndUpdateRequest request) {
        ManagerKinds managerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, managerKindsSeq);

        managerKinds.update(request.getName(), request.getAbilities());
    }

    // 관리자 분류(등급) 삭제 (사용안함)
    @Transactional
    public void deleteManagerKinds(String managerKindsSeq) {
        ManagerKinds managerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, managerKindsSeq);
        managerKinds.unUsed();
        List<ManagerUsers> managerUsersList = managerUsersRepository.findByKinds(managerKinds);
        for(ManagerUsers managerUsers : managerUsersList) {
            managerUsers.deleteManagerKinds();;
        }
    }
}
