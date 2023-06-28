package com.muzisoft.division.config.security;

import com.muzisoft.division.domain.common.enums.Role;
import com.muzisoft.division.domain.manager.*;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationDynamicHandler {

    private final UserRepository userRepository;
    private final ManagerUsersRepository managerUsersRepository;
    private final ManagerPermissionRepository managerPermissionRepository;

    @Transactional(readOnly = true)
    public boolean isAuthorization(HttpServletRequest request, Authentication authentication) {
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());
//        System.out.println(">>> " + requestPath);
        if (!authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains(Role.ADMIN.getValue())) {
            return false;
        }
        User foundUser = EntityUtils.userThrowable(userRepository, ((User) authentication.getPrincipal()).getSeq());
        ManagerUsers foundManagerUser = managerUsersRepository.findByUser(foundUser).orElseThrow(CEntityNotFoundException.CManagerUserNotFoundException::new);
        ManagerKinds foundManagerKinds = foundManagerUser.getKinds();
        List<String> abilitySeqList = foundManagerKinds.getAbilities();
        List<String> distinctPaths = new ArrayList<>();
        for(String abilitySeq : abilitySeqList) {
            ManagerPermission managerPermission = EntityUtils.ManagerPermissionThrowable(managerPermissionRepository, abilitySeq);
            String path = managerPermission.getUrl();
            distinctPaths.add(path);
        }

//        List<Executive> foundExecutives = executiveRepository.findByChorusAndNameIn(foundUser.getUserDetails().getRepresentativeChorus(), Arrays.asList(foundChorusMember.getDivNames().split("\\|")));
//        List<String> distinctPaths = foundExecutives.stream().map(Executive::getPaths).flatMap(Collection::stream).distinct().map(ChorusMemberAuthority::getPath).collect(Collectors.toList());

        return distinctPaths.stream().anyMatch(path -> path.equals(requestPath));
    }
}
