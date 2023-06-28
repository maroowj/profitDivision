package com.muzisoft.division.web.controller;

import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.web.api.dto.KakaoSignupRequest;
import com.muzisoft.division.web.api.dto.common.TokenResponse;
import com.muzisoft.division.web.service.AuthService;
import com.muzisoft.division.web.service.users.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserInfoService userInfoService;

    @GetMapping()
    public String main() {
        return "/user/index";
    }

    // 사용자 회원가입
    @GetMapping("/join")
    public String join() {
        if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "/user/join";
    }

    // 사용자 로그인
    @GetMapping("/login")
    public String login() {
        if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }
        return "/user/login";
    }

    // 아이디 찾기
    @GetMapping("/find-id")
    public String find_id() {
        return "/user/findId";
    }

    // 비밀번호 찾기
    @GetMapping("/find-pw")
    public String find_pw() {
        return "/user/findPassword";
    }

    // 회원정보 수정
    @GetMapping("/mypage")
    public String mypage() {
        return "/user/mypage";
    }

    // 비밀번호 변경
    @GetMapping("/change-password")
    public String change_password() {
        return "/user/changePassword";
    }

    // 인증 코드에 의한 비밀번호 변경
    @GetMapping("/reset-password/{code}")
    public String reset_password_by_code(@PathVariable("code") String code) {
        if(userInfoService.certificateCode(code)) {
            return "/user/changePasswordByCode";
        }else {
            return "/error/invalid_certification_code";
        }
    }


    // 사용자 투자하기
    @GetMapping("/invest")
    public String invest() {
        return "/user/invest";
    }

    // 수익관리
    @GetMapping("/profit")
    public String profit() {
        return "/user/profit";
    }

    // 출금신청하기
    @GetMapping("/withdrawal")
    public String withdrawal() {
        return "/user/withdrawal";
    }

    // 회비관리
    @GetMapping("/dues")
    public String dues() {
        return "/user/dues";
    }

    // 적립금관리
    @GetMapping("/point")
    public String point() {
        return "/user/point";
    }

    // 게시판
    @GetMapping("/board")
    public String board() {
        return "/user/board";
    }

    // 게시판 상세
    @GetMapping("/board-view")
    public String board_view() {
        return "/user/board_view";
    }

    // 자주묻는질문
    @GetMapping("/faq")
    public String qna() {
        return "/user/qna";
    }


    // 공지사항
    @GetMapping("/notice")
    public String notice() {
        return "/user/notice";
    }

    // 공지사항 상세
    @GetMapping("/notice-view")
    public String notice_view() {
        return "/user/notice_view";
    }

    // 회사소개
    @GetMapping("/greeting")
    public String greeting() {
        return "/user/greeting";
    }

    // 수익 결산표
    @GetMapping("/account")
    public String account() {
        return "/user/account";
    }


    // 로그아웃
    @GetMapping("/user/logout")
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

        return "redirect:/";
    }

    // 로그인 여부 체크
    @ResponseBody
    @GetMapping("/api/common/login")
    public Map<String, Object> loginChk() {
        Map<String, Object> result = new HashMap<>();
        String memberSeq = SecurityContextHolder.getContext().getAuthentication().getName();

        if (memberSeq == null || memberSeq.equals("") || memberSeq.equals("anonymousUser")) {
            result.put("login", false);
        }else {
            result.put("login", true);
        }

        return  result;
    }

    @GetMapping("/login/kakao")
    public void kakao_login(KakaoSignupRequest request, HttpServletResponse response) throws IOException {

        TokenResponse tokenResponse = authService.kakaoLogin(request);

        Optional<User> optionalUser = userRepository.findBySnsIdAndProvider(request.getSid(), request.getProvider());

        if(optionalUser.get().isWithdrawal()) {
            response.sendRedirect("/error/user-drop");
        }else if(optionalUser.get().getUserDetails().getAccepted()!=1) {
                response.sendRedirect("/error/user-not-accepted");
        }else {
            Cookie access_cookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
//        access_cookie.setMaxAge(60 * 60 * 24 );
            access_cookie.setMaxAge(-1);
            access_cookie.setPath("/");
            access_cookie.setHttpOnly(true);
            response.addCookie(access_cookie);

            Cookie refresh_cookie = new Cookie("RefreshToken", tokenResponse.getRefreshToken());
            refresh_cookie.setMaxAge(-1);
            refresh_cookie.setPath("/");
            refresh_cookie.setHttpOnly(true);
            response.addCookie(refresh_cookie);

            response.sendRedirect("/");
        }
    }

}
