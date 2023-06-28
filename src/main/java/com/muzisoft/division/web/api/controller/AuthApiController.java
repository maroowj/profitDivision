package com.muzisoft.division.web.api.controller;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.utils.enums.SocialType;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.KakaoSignupRequest;
import com.muzisoft.division.web.api.dto.common.TokenRequest;
import com.muzisoft.division.web.api.dto.common.TokenResponse;
import com.muzisoft.division.web.api.dto.user.LoginRequest;
import com.muzisoft.division.web.api.dto.user.SignupRequest;
import com.muzisoft.division.web.api.dto.user.SocialLoginRequest;
import com.muzisoft.division.web.api.dto.user.SocialSignupRequest;
import com.muzisoft.division.web.api.dto.users.userInfo.*;
import com.muzisoft.division.web.client.dto.SocialProfile;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.muzisoft.division.web.exception.business.CInvalidValueException;
import com.muzisoft.division.web.service.AuthService;
import com.muzisoft.division.web.service.MailService;
import com.muzisoft.division.web.service.social.OAuthService;
import com.muzisoft.division.web.service.users.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;
    private final OAuthService oauthService;
    private final UserInfoService userInfoService;
    private final MailService mailService;
    private final UserRepository userRepository;

    @PostMapping("/user/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Validated SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        authService.signup(signupRequest);
    }

    /*@PostMapping("/social/{socialType}")
    @ResponseStatus(HttpStatus.CREATED)
    public void socialSignup(@PathVariable(name = "socialType") SocialType socialType,
                             @RequestBody @Validated SocialSignupRequest request) {

        //구글은 access_token 대신 id_token 값으로 요청
        SocialProfile socialProfile = oauthService.profile(socialType, request.getAccessToken());

        //소셜 프로필이 없는 경우 에러
        if (ObjectUtils.isEmpty(socialProfile)) throw new CUserNotFoundException();

        //이미지가 있으면 FileManager 로 변환
        EFileManager profileImage = fileHandler.parseImageUrl(socialProfile.getProfileImageUrl(), "user-img");

        authService.socialSignup(socialProfile, socialType);
    }*/

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.CREATED)
    public void login(@RequestBody @Validated LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        TokenResponse tokenResponse = authService.login(loginRequest, response);

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
    }

    @PostMapping("/admin/login")
    @ResponseStatus(HttpStatus.CREATED)
    public void adminLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest req, HttpServletResponse response) throws IOException {
        TokenResponse tokenResponse =authService.login(loginRequest, response);

        Cookie access_cookie = new Cookie("AccessToken", tokenResponse.getAccessToken());
        access_cookie.setMaxAge(-1);
        access_cookie.setPath("/");
        access_cookie.setHttpOnly(true);
        response.addCookie(access_cookie);

        Cookie refresh_cookie = new Cookie("RefreshToken", tokenResponse.getRefreshToken());
//        if (Boolean.parseBoolean(request.getIsKeep())) {
//            refresh_cookie.setMaxAge(60 * 60 * 24 * 7);
//        } else {
//            refresh_cookie.setMaxAge(-1);
//        }
        refresh_cookie.setMaxAge(-1);
        refresh_cookie.setPath("/");
        refresh_cookie.setHttpOnly(true);
        response.addCookie(refresh_cookie);

        if (loginRequest.isChkLogin()) {
            Cookie loginSaveCookie = new Cookie("savedId", loginRequest.getId());
            loginSaveCookie.setMaxAge(60 * 60 * 24 * 14); // 2주
            loginSaveCookie.setPath("/");
            response.addCookie(loginSaveCookie);
        } else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("savedId")) {
                        cookie.setValue(null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
        }
    }

//    @PostMapping("/token")
//    @ResponseStatus(HttpStatus.CREATED)
//    public TokenResponse login(@RequestBody @Validated LoginRequest loginRequest) {
//        return authService.login(loginRequest);
//    }

    @PostMapping("/social/{socialType}/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse socialLogin(@PathVariable(name = "socialType") SocialType socialType,
                                     @RequestBody @Validated SocialLoginRequest request) {

        SocialProfile socialProfile = oauthService.profile(socialType, request.getAccessToken());

        //소셜 프로필이 없는 경우 에러
        if(ObjectUtils.isEmpty(socialProfile)) throw new CUserNotFoundException();

        return authService.socialLogin(new LoginRequest(socialProfile.getSnsId(), null, socialType.name().toLowerCase()));
    }

    @PostMapping("/token/expiration")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse reissue(@RequestBody @Validated TokenRequest request) {
        return authService.reissue(request);
    }


    /** 아이디 찾기 **/
    @PostMapping("/user/find-id")
    public FindIdResponse find_id(@RequestBody FindIdRequest request) {
        return userInfoService.find_id(request);
    }

    /** 비밀번호 찾기 (인증메일발송) **/
    @PostMapping("/user/find-pw")
    public void find_pw(@RequestBody FindPasswordRequest request, HttpServletRequest req) throws MessagingException {
        userInfoService.passwordCertificate(request, req);
    }

    /** 아이디 중복 확인 **/
    @GetMapping("/user/duplicate-check")
    public boolean duplicateCheckId(@Valid DuplicateCheckIdRequest request) {
        if(authService.duplicateCheckId(request)) {
            return false;
        }else {
            return true;
        }
    }

    /** 인증코드 검증 **/
    @PostMapping("/user/certificate/{code}")
    public ResponseEntity<Void> certificateCode(@PathVariable("code") String code) {
        if(userInfoService.certificateCode(code)) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else {
            throw new CInvalidValueException.CInvalidCodeException();
        }
    }

    /** 비밀번호 재설정 (코드에 의한 비밀번호 변경) **/
    @PostMapping("/user/reset-pw/{code}")
    public void reset_password(@PathVariable("code") String code, @RequestBody PasswordResetRequest request) {
        userInfoService.resetPasswordByCode(code, request);
    }

}
