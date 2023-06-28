package com.muzisoft.division.web.api.controller.users;

import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.enums.SocialType;
import com.muzisoft.division.utils.handler.FileHandler;
import com.muzisoft.division.web.api.dto.common.TokenRequest;
import com.muzisoft.division.web.api.dto.common.TokenResponse;
import com.muzisoft.division.web.api.dto.user.LoginRequest;
import com.muzisoft.division.web.api.dto.user.SignupRequest;
import com.muzisoft.division.web.api.dto.user.SocialLoginRequest;
import com.muzisoft.division.web.api.dto.user.SocialSignupRequest;
import com.muzisoft.division.web.client.dto.SocialProfile;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.muzisoft.division.web.service.AuthService;
import com.muzisoft.division.web.service.social.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserApiController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final FileHandler fileHandler;
    private final OAuthService oauthService;

    @PostMapping
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

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Validated LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        return authService.login(loginRequest, response);
    }

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

}
