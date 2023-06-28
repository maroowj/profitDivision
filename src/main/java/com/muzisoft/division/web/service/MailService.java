package com.muzisoft.division.web.service;


import com.muzisoft.division.Application;
import com.muzisoft.division.domain.user.User;
import com.muzisoft.division.domain.user.UserPasswordCode;
import com.muzisoft.division.domain.user.UserPasswordCodeRepository;
import com.muzisoft.division.domain.user.UserRepository;
import com.muzisoft.division.web.api.dto.users.userInfo.FindPasswordRequest;
import com.muzisoft.division.web.exception.business.CEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final UserRepository userRepository;
    private final UserPasswordCodeRepository userPasswordCodeRepository;
    private final Application application;


    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void  passwordCertification(FindPasswordRequest request, String code, HttpServletRequest req) throws MessagingException {
        String address = "http://" + req.getServerName();
        if(req.getServerName().equals("localhost")) {
            address = address + ":" + req.getServerPort();
        }

//        DecimalFormat decFormat = new DecimalFormat("###,###");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일 HH시 mm분");

        MimeMessage msg = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true, "UTF-8");

        StringBuilder content = new StringBuilder(); // 메일 내용

        content.append("비밀번호 변경 인증 메일입니다.<br/>");
        content.append("이 메일은 발송일로부터 7일간 유효합니다.<br/>");
        content.append("비밀번호를 변경하시려면 아래 링크를 클릭해주세요.<br/>");
        content.append("<a href='"+address+"/reset-password/"+code+"'><b>비밀번호 변경 바로가기</b>");

        mimeMessageHelper.setFrom(new InternetAddress("info@metacity1000.com")); // 보내는 사람
        mimeMessageHelper.setSubject("비밀번호 변경 인증 메일입니다."); // 메일 제목
        mimeMessageHelper.setText(content.toString(), true);

        mimeMessageHelper.setTo(request.getEmail());
        mailSender.send(msg);
    }

}
