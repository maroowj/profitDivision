package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.web.api.dto.users.userInfo.ResetPasswordRequest;
import com.muzisoft.division.web.api.dto.users.userInfo.UserInfoResponse;
import com.muzisoft.division.web.api.dto.users.userInfo.UserInfoUpdateRequest;
import com.muzisoft.division.web.service.users.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoApiController {

    private final UserInfoService userDetailsService;

    @GetMapping("/details")
    public UserInfoResponse userDetails() {
       return userDetailsService.userDetails();
    }

    @PostMapping("/reset-pw")
    public void resetPw(@RequestBody ResetPasswordRequest req, HttpServletRequest request, HttpServletResponse response) {
        userDetailsService.resetPw(req);

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
    }

    @PutMapping("/details")
    public void updateUserInfo(@RequestBody UserInfoUpdateRequest request) {
        userDetailsService.updateUserInfo(request);
    }
}
