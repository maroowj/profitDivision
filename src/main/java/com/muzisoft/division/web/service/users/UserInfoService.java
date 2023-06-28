package com.muzisoft.division.web.service.users;

import com.muzisoft.division.domain.user.*;
import com.muzisoft.division.utils.EntityUtils;
import com.muzisoft.division.web.api.dto.users.userInfo.*;
import com.muzisoft.division.web.exception.business.CBusinessException;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import com.muzisoft.division.web.exception.business.CInvalidValueException;
import com.muzisoft.division.web.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserPasswordCodeRepository userPasswordCodeRepository;
    private final MailService mailService;

    @Transactional
    public UserInfoResponse userDetails() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = EntityUtils.userThrowable(userRepository, user.getSeq());
        UserDetails userDetails = foundUser.getUserDetails();
        UserAccount userAccount = null;
        Optional<UserAccount> foundUserAccount = userAccountRepository.findByUserDetailsAndUsed(user.getUserDetails(), true);

        if(foundUserAccount.isPresent()) {
            userAccount = foundUserAccount.get();
        }

        return new UserInfoResponse(foundUser, userDetails, userAccount);
    }

    @Transactional
    public void resetPw(ResetPasswordRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = EntityUtils.userThrowable(userRepository, user.getSeq());

        if(!passwordEncoder.matches(request.getOriginalPassword(), foundUser.getPassword())) {
            throw new CInvalidValueException.CLoginFailedException();
        }else {
            foundUser.changePassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    @Transactional
    public void updateUserInfo(UserInfoUpdateRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User foundUser = EntityUtils.userThrowable(userRepository, user.getSeq());
        UserDetails userDetails = foundUser.getUserDetails();

        userDetails.updateInfo(
                request.getName(),
                request.getBirth(),
                request.getMobile(),
                request.getEmail(),
                request.getAddress()
        );

        Optional<UserAccount> optUserAccount = userAccountRepository.findByUserDetailsAndUsed(userDetails, true);
        if(optUserAccount.isPresent()) {
            UserAccount userAccount = optUserAccount.get();
            userAccount.update(request.getBankName(), request.getAccountNumber());
        }else {
            if(request.getBankName()!=null && !request.getBankName().equals("") && request.getAccountNumber()!=null && !request.getAccountNumber().equals("")) {
                userAccountRepository.save(
                        UserAccount.create(userDetails, request.getBankName(), request.getAccountNumber())
                );
            }
        }
    }

    @Transactional
    public FindIdResponse find_id(FindIdRequest request) {
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByNameAndMobile(request.getName(), request.getMobile());

        if(!optionalUserDetails.isPresent()) {
            throw new CEntityNotFoundException.CUserDetailsNotFoundException();
        }

        UserDetails userDetails = optionalUserDetails.get();

        User user = EntityUtils.userThrowable(userRepository, userDetails);

        if(user.isWithdrawal()) {
            throw new CInvalidValueException.CWithdrawalException();
        }

        if(user.getSnsId()!=null) {
            throw new CEntityNotFoundException.CUserNotFoundException();
        }

        FindIdResponse response = new FindIdResponse();
        response.setUserId(user.getUserId());

        return response;
    }

    @Transactional
    public void passwordCertificate(FindPasswordRequest request, HttpServletRequest req) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByUserIdAndUsername(request.getUserId(), request.getName());

        if(!optionalUser.isPresent()) {
            throw new CEntityNotFoundException.CUserNotFoundException();
        }
        if(optionalUser.get().isWithdrawal()) {
            throw new CInvalidValueException.CWithdrawalException();
        }

        if(optionalUser.get().getSnsId()!=null) {
            throw new CEntityNotFoundException.CUserNotFoundException();
        }

        User user = optionalUser.get();
        String code = generatedRandomKey();

        List<UserPasswordCode> userPasswordCodeList = userPasswordCodeRepository.findAllByUserAndUsed(user, false);
        if(userPasswordCodeList.size()!=0) {
            for(UserPasswordCode userPasswordCode : userPasswordCodeList) {
                userPasswordCode.updateUsed();
            }
        }

        userPasswordCodeRepository.save(
                UserPasswordCode.create(
                        user,
                        code
                )
        );

        mailService.passwordCertification(request, code, req);
    }

    @Transactional
    public boolean certificateCode(String code) {
        boolean valid = false;
        Optional<UserPasswordCode> optionalUserPasswordCode = userPasswordCodeRepository.findByCodeAndUsed(code, false);
        Date today = new Date();

        if(optionalUserPasswordCode.isPresent()) {
            UserPasswordCode userPasswordCode = optionalUserPasswordCode.get();
            Date expireDate = userPasswordCode.getExpireTime();
            if(today.compareTo(expireDate) < 0) {
                valid = true;
            }
        }

        return valid;
    }

    @Transactional
    public void resetPasswordByCode(String code, PasswordResetRequest request) {
        Optional<UserPasswordCode> optionalUserPasswordCode = userPasswordCodeRepository.findByCodeAndUsed(code, false);
        if(!optionalUserPasswordCode.isPresent()) {
            throw new CInvalidValueException.CInvalidCodeException();
        }
        UserPasswordCode userPasswordCode = optionalUserPasswordCode.get();
        User user = userPasswordCode.getUser();

        user.changePassword(passwordEncoder.encode(request.getPassword()));
        userPasswordCode.updateUsed();
    }

    // 비밀번호 인증 코드 생성 메소드
    private static String generatedRandomKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
