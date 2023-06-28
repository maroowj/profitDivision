package com.muzisoft.division.web.controller;

import com.muzisoft.division.domain.common.enums.Role;
import com.muzisoft.division.domain.manager.*;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.security.CSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/management")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ManagerUsersRepository managerUsersRepository;
    private final ManagerKindsRepository managerKindsRepository;
    private final ManagerPermissionRepository managerPermissionRepository;

    // 관리자 로그인
    @GetMapping("/login")
    public String login() {
        return "/management/login";
    }

    // 관리자 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/management/login";
    }

    // 로그인 후 관리자 권한에 따른 경로로 이동
    @GetMapping
    public String permissionUrl(HttpServletResponse response) throws IOException {

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            response.sendRedirect("/error/admin-login");
            throw new CSecurityException.CAuthenticationEntryPointException();
        }

        User foundUser = EntityUtils.userThrowable(userRepository);

        if(!foundUser.getRoles().contains(Role.ADMIN)) {
            response.sendRedirect("/error/admin-login");
        }
        ManagerUsers foundManagerUser = managerUsersRepository.findByUser(foundUser).orElseThrow(CEntityNotFoundException.CManagerUserNotFoundException::new);
        if(foundManagerUser.getKinds()==null) {
            return "redirect:/error/admin-no-grade";
        }
        ManagerKinds foundManagerKinds = foundManagerUser.getKinds();

        List<String> abilitySeqList = foundManagerKinds.getAbilities();
        if(abilitySeqList==null || abilitySeqList.size()==0) {
            return "redirect:/management/login";
        }else {
            String managerPermissionSeq = abilitySeqList.get(0);
            ManagerPermission managerPermission = EntityUtils.ManagerPermissionThrowable(managerPermissionRepository, managerPermissionSeq);
            String path = managerPermission.getUrl();
            return "redirect:"+path;
        }
    }

    // 관리자 관리
    @GetMapping("/managers")
    public String managers() {
        return "/management/manager/list";
    }

    // 회원리스트
    @GetMapping("/members")
    public String members() {
        return "/management/member/list";
    }

    // 가입대기 리스트
    @GetMapping("/waiting")
    public String waiting() {
        return "/management/member/waiting";
    }

    // 회비입금 확인
    @GetMapping("/dues")
    public String dues() {
        return "/management/dues/list";
    }

    // 회비 확인 요청
    @GetMapping("/dues-waiting")
    public String dues_waiting() {
        return "/management/dues/waiting";
    }

    // 투자 관리
    @GetMapping("/investment")
    public String investment() {
        return "/management/investment/list";
    }

    // 수익 리스트
    @GetMapping("/profits")
    public String profits() {
        return "/management/profits/list";
    }

    // 출금 리스트
    @GetMapping("/withdrawals")
    public String withdrawals() {
        return "/management/withdrawals/list";
    }

    // 부대비용 관리리
   @GetMapping("/interest")
    public String interest() {
        return "/management/interest/list";
    }

    // 적립금 관리
    @GetMapping("/reserves-manage")
    public String reserves() {
        return "/management/reserves/manage";
    }

    // 적립금 리스트
    @GetMapping("/reserves-list")
    public String reserves_list() {
        return "/management/reserves/list";
    }

    // 팝업 관리 (목록)
    @GetMapping("/popup")
    public String popup() {
        return "/management/popup/list";
    }

    @GetMapping("/popup-new")
    public String popupCreate() {
        return "/management/popup/create";
    }

    @GetMapping("/popup-details")
    public String popupDetails() {
        return "/management/popup/details";
    }

    @GetMapping("/notices")
    public String notices() {
        return "/management/notices/list";
    }

    @GetMapping("/notices-new")
    public String noticesCreate() {
        return "/management/notices/create";
    }

    @GetMapping("/notices-details")
    public String noticeDetails() {
        return "/management/notices/read";
    }

    @GetMapping("/board")
    public String board() {
        return "/management/board/list";
    }

    @GetMapping("/board-new")
    public String boardCreate() {
        return "/management/board/create";
    }

    @GetMapping("/board-details")
    public String boardDetails() {
        return "/management/board/read";
    }

    @GetMapping("/faq")
    public String faq() {
        return "/management/faq/list";
    }

    @GetMapping("/faq-new")
    public String faqCreate() {
        return "/management/faq/create";
    }

    @GetMapping("/faq-details")
    public String faqDetails() {
        return "/management/faq/read";
    }

    @GetMapping("/accounts")
    public String accounts() {
        return "/management/accounts/accounts";
    }
}
