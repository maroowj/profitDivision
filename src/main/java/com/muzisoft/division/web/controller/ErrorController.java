package com.muzisoft.division.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/error")
@RequiredArgsConstructor
public class ErrorController {

    private final static Logger log = Logger.getGlobal();

    @GetMapping("/denied")
    public String testInsert() {
        return "/error/denied";
    }

    @GetMapping("/admin-login")
    public String adminLoginError(HttpServletRequest request) {
        request.setAttribute("msg", "로그인 후 이용 가능합니다.");
        request.setAttribute("nextPage", "/management/login");
        return "/error/ad_login";
    }

    @GetMapping("/admin-not-allowed")
    public String adminPermissionError(HttpServletRequest request) {
        request.setAttribute("msg", "접근 권한이 없습니다.");
        request.setAttribute("nextPage", "/management/login");
        return "/error/ad_permission";
    }

    @GetMapping("/admin-no-grade")
    public String adminGradeError() {
        return "/error/ad_grade";
    }

    @GetMapping("/user-login")
    public String userLoginError() {
        return "/error/user_login";
    }

    @GetMapping("/user-drop")
    public String userDropError() {
        return "/error/user_drop";
    }

    @GetMapping("/user-not-accepted")
    public String userAcceptedError() {
        return "/error/user_not_accepted";
    }
}
