package com.muzisoft.division.web.service;

import com.muzisoft.division.config.security.JwtProvider;
import com.muzisoft.division.domain.common.enums.Role;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.domain.manager.ManagerKinds;
import com.muzisoft.division.domain.manager.ManagerKindsRepository;
import com.muzisoft.division.domain.manager.ManagerUsers;
import com.muzisoft.division.domain.manager.ManagerUsersRepository;
import com.muzisoft.division.domain.refreshtoken.RefreshToken;
import com.muzisoft.division.domain.refreshtoken.RefreshTokenRepository;
import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.utils.enums.SocialType;
import com.muzisoft.division.web.api.dto.KakaoSignupRequest;
import com.muzisoft.division.web.api.dto.admin.manager.ManagerSaveRequest;
import com.muzisoft.division.web.api.dto.common.TokenRequest;
import com.muzisoft.division.web.api.dto.common.TokenResponse;
import com.muzisoft.division.web.api.dto.user.LoginRequest;
import com.muzisoft.division.web.api.dto.user.SignupRequest;
import com.muzisoft.division.web.api.dto.users.userInfo.DuplicateCheckIdRequest;
import com.muzisoft.division.web.client.dto.SocialProfile;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.business.CInvalidValueException;
import com.muzisoft.division.web.exception.business.CInvalidValueException.CAlreadySignedupException;
import com.muzisoft.division.web.exception.security.CTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GradeRepository gradeRepository;
    private final ManagerUsersRepository managerUsersRepository;
    private final ManagerKindsRepository managerKindsRepository;

    @Transactional
    public User signup(SignupRequest req) {
        User recommender = null;

        // 추천인을 입력 했을 때 존재여부 검증
        if (req.getRecommendCode()!=null && !req.getRecommendCode().equals("")) {
            Optional<UserDetails> recommenderDetails = userDetailsRepository.findByRecommendCode(req.getRecommendCode());
            if(recommenderDetails.isPresent()) {
                UserDetails foundUserDetails = recommenderDetails.get();
                recommender = userRepository.findByUserDetails(foundUserDetails).orElseThrow(CEntityNotFoundException.CUserNotFoundException::new);
            }
        }

        // 회원 번호 생성
        String membershipNumber = null;
        Optional<UserDetails> lastUser = userDetailsRepository.findTopByOrderByMembershipNumberDesc();
        if(lastUser.isPresent()) { // 마지막 회원의 번호 + 1
            membershipNumber = lastUser.get().getMembershipNumber();
            int numbering = Integer.parseInt(membershipNumber) + 1;
            membershipNumber = Integer.toString(numbering);
            if(membershipNumber.length()==1) {
                membershipNumber = "000"+membershipNumber;
            }else if(membershipNumber.length()==2) {
                membershipNumber = "00"+membershipNumber;
            }else if(membershipNumber.length()==3) {
                membershipNumber = "0"+membershipNumber;
            }
        }else { // 첫번 째 회원
            membershipNumber = "0001";
        }

        // 추천인 코드 생성 (대/소문자 + 숫자 조합 5자리)
        String recommendCode = generatedRandomKey();
        Optional<UserDetails> findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
        if(findRecommendCode.isPresent()) {
            while (!findRecommendCode.isPresent()){
                recommendCode = generatedRandomKey();
                findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
            }
        }

        // 회원 등급 불러오기 (준회원)
        Grade foundGrade = EntityUtils.gradeThrowable(gradeRepository, 1); // 준회원

        User user = User.createDirect(req.getId(), req.getPassword(), req.getProvider(), req.getUsername(),
                                        req.getBirth(), req.getMobile(), req.getEmail(), req.getAddress(),
                                        recommender, recommendCode, membershipNumber, foundGrade);
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new CAlreadySignedupException();
        }

        return userRepository.save(user);
    }

    @Transactional
    public ManagerUsers signupManager(ManagerSaveRequest req, EFileManager profilePicture) {

        // 회원 번호 생성 (관리자는 전부 0000)
        String membershipNumber = "0000";


        // 추천인 코드 생성 (대/소문자 + 숫자 조합 5자리)
        String recommendCode = generatedRandomKey();
        Optional<UserDetails> findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
        if(findRecommendCode.isPresent()) {
            while (!findRecommendCode.isPresent()){
                recommendCode = generatedRandomKey();
                findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
            }
        }

        // 회원 등급 불러오기 (준회원)
        Grade foundGrade = EntityUtils.gradeThrowable(gradeRepository, 1); // 준회원

        User user = User.createManager(req.getId(), req.getPassword(), req.getName(),
                    req.getMobile(), recommendCode, membershipNumber, foundGrade);
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new CAlreadySignedupException();
        }

        // 관리자 분류(등급) 불러오기
        ManagerKinds foundManagerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, req.getManagerKindsSeq());

        ManagerUsers managerUsers = ManagerUsers.create(user, req.getNickName(), foundManagerKinds, profilePicture);

        userRepository.save(user);
        return managerUsersRepository.save(managerUsers);
    }

    @Transactional
    public ManagerUsers signupDefaultManager(ManagerSaveRequest req) {

        // 회원 번호 생성 (관리자는 전부 0000)
        String membershipNumber = "0000";


        // 추천인 코드 생성 (대/소문자 + 숫자 조합 5자리)
        String recommendCode = generatedRandomKey();
        Optional<UserDetails> findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
        if(findRecommendCode.isPresent()) {
            while (!findRecommendCode.isPresent()){
                recommendCode = generatedRandomKey();
                findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
            }
        }

        // 회원 등급 불러오기 (준회원)
        Grade foundGrade = EntityUtils.gradeThrowable(gradeRepository, 1); // 준회원

        User user = User.createManager(req.getId(), req.getPassword(), req.getName(),
                req.getMobile(), recommendCode, membershipNumber, foundGrade);
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new CAlreadySignedupException();
        }

        // 관리자 분류(등급) 불러오기
        ManagerKinds foundManagerKinds = EntityUtils.managerKindsThrowable(managerKindsRepository, req.getManagerKindsSeq());

        ManagerUsers managerUsers = ManagerUsers.create(user, req.getNickName(), foundManagerKinds, null);

        userRepository.save(user);
        return managerUsersRepository.save(managerUsers);
    }




    /*@Transactional
    public void socialSignup(SocialProfile socialProfile, SocialType socialType) {
        userRepository.findBySnsIdAndProvider(socialProfile.getSnsId(), socialType.name().toLowerCase())
                .ifPresent(user -> {
                    throw new CAlreadySignedupException();
                });
        userRepository.save(
                User.createSocial(
                        socialType.name().toLowerCase() + RandomStringUtils.random(15, true, true),
                        passwordEncoder.encode(UUID.randomUUID().toString()),
                        socialType.name().toLowerCase(),
                        socialProfile.getSnsId()
                )
        );
    }*/

    @Transactional
    public TokenResponse login(LoginRequest request, HttpServletResponse response) throws IOException {
        User user = userRepository.findByUserId(request.getId()).orElseThrow(CInvalidValueException.CLoginFailedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CInvalidValueException.CInvalidPasswordException();
        }
        if (user.isWithdrawal()) {
            throw new CInvalidValueException.CWithdrawalException();
        }
        if (user.getUserDetails().getAccepted()!=1) {
            response.sendRedirect("/error/user-not-accepted");
        }
        refreshTokenRepository.findByKey(user.getSeq()).ifPresent(refreshTokenRepository::delete);
        TokenResponse tokenResponse = jwtProvider.createToken(user.getSeq(), user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()));
        refreshTokenRepository.save(RefreshToken.create(user.getSeq(), tokenResponse.getRefreshToken()));
        return tokenResponse;
    }

    @Transactional
    public TokenResponse socialLogin(LoginRequest request) {
        User user = userRepository.findBySnsIdAndProvider(request.getId(), request.getProvider())
                .orElseThrow(CEntityNotFoundException.CUserNotFoundException::new);
        refreshTokenRepository.findByKey(user.getSeq()).ifPresent(refreshTokenRepository::delete);
        TokenResponse tokenResponse = jwtProvider.createToken(user.getSeq(), user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()));
        refreshTokenRepository.save(RefreshToken.create(user.getSeq(), tokenResponse.getRefreshToken()));
        return tokenResponse;
    }

    /**
     * TokenRequest를 통해 액세스 토큰 재발급 요청
     * * 리프레시 토큰 만료 검증 -> 만료 시 재로그인 요청
     */
    @Transactional
    public TokenResponse reissue(TokenRequest request) {

        //리프레시 토큰 만료
        if (!jwtProvider.validationToken(request.getRefreshToken())) {
            throw new CTokenException.CRefreshTokenException();
        }

        String accessToken = request.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        User foundUser = EntityUtils.userThrowable(userRepository, ((User) authentication.getPrincipal()).getSeq());

        //리프레시 토큰 없음
        RefreshToken refreshToken = refreshTokenRepository.findByKey(foundUser.getSeq())
                .orElseThrow(CTokenException.CRefreshTokenException::new);

        //리프레시 토큰 불일치
        if (!refreshToken.getToken().equals(request.getRefreshToken())) {
            throw new CTokenException.CRefreshTokenException();
        }

        TokenResponse newCreatedToken = jwtProvider.createToken(foundUser.getSeq(), foundUser.getRoles().stream().map(Role::getValue).collect(Collectors.toList()));
        refreshToken.update(newCreatedToken.getRefreshToken());

        return newCreatedToken;
    }

    @Transactional
    public boolean duplicateCheckId(DuplicateCheckIdRequest request) {
        return userRepository.existsByUserId(request.getId());
    }

    @Transactional
    public TokenResponse kakaoLogin(KakaoSignupRequest request) {
        Optional<User> optionalUser = userRepository.findBySnsIdAndProvider(request.getSid(), request.getProvider());

        if(!optionalUser.isPresent()) {
            String userId = "user" + System.currentTimeMillis();
            if(request.getEmail()!=null) {
                userId = request.getEmail();
            }
            String userPwd = passwordEncoder.encode(generatedRandomKey());

            // 회원 번호 생성
            String membershipNumber = null;
            Optional<UserDetails> lastUser = userDetailsRepository.findTopByOrderByMembershipNumberDesc();
            if(lastUser.isPresent()) { // 마지막 회원의 번호 + 1
                membershipNumber = lastUser.get().getMembershipNumber();
                int numbering = Integer.parseInt(membershipNumber) + 1;
                membershipNumber = Integer.toString(numbering);
                if(membershipNumber.length()==1) {
                    membershipNumber = "000"+membershipNumber;
                }else if(membershipNumber.length()==2) {
                    membershipNumber = "00"+membershipNumber;
                }else if(membershipNumber.length()==3) {
                    membershipNumber = "0"+membershipNumber;
                }
            }else { // 첫번 째 회원
                membershipNumber = "0001";
            }

            // 추천인 코드 생성 (대/소문자 + 숫자 조합 5자리)
            String recommendCode = generatedRandomKey();
            Optional<UserDetails> findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
            if(findRecommendCode.isPresent()) {
                while (!findRecommendCode.isPresent()){
                    recommendCode = generatedRandomKey();
                    findRecommendCode = userDetailsRepository.findByRecommendCode(recommendCode);
                }
            }

            // 회원 등급 불러오기 (준회원)
            Grade foundGrade = EntityUtils.gradeThrowable(gradeRepository, 1); // 준회원

            User user = User.create(
                    userId,
                    userPwd,
                    request.getNickname(),
                    request.getEmail(),
                    recommendCode,
                    membershipNumber,
                    foundGrade,
                    request.getSid(),
                    request.getProvider()
            );
            userRepository.save(user);
        }

        User user = userRepository.findBySnsIdAndProvider(request.getSid(), request.getProvider())
                .orElseThrow(CEntityNotFoundException.CUserNotFoundException::new);
        refreshTokenRepository.findByKey(user.getSeq()).ifPresent(refreshTokenRepository::delete);
        TokenResponse tokenResponse = jwtProvider.createToken(user.getSeq(), user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()));
        refreshTokenRepository.save(RefreshToken.create(user.getSeq(), tokenResponse.getRefreshToken()));

        return tokenResponse;
    }

    // 추천인 코드 생성 메소드
    private static String generatedRandomKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
