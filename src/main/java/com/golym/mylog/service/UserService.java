package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.RequestEmailDto;
import com.golym.mylog.model.entity.AuthEmail;
import com.golym.mylog.repository.AuthEmailRepository;
import com.golym.mylog.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final UserRepository userRepository;
    private final AuthEmailRepository authEmailRepository;

    private final JavaMailSender mailSender;

    @Async
    public void sendAuthcode(RequestEmailDto params) {
        String toEmail = params.getEmail();
        String authcode = CodeGenerator.generateAuthcode();

        // 인증 메일 생성 및 전송
        sendEmail(toEmail, authcode);

        // 메일주소, 인증코드를 redis에 저장
        AuthEmail authEmail = new AuthEmail(toEmail, authcode);
        authEmailRepository.save(authEmail);
    }

    private void sendEmail(String toEmail, String authcode) {
        String setFrom = fromEmail;
        String title = "[MyLog] 회원가입 인증 이메일";
        String content = "MyLog를 방문해주셔서 감사합니다." + "<br><br>" + "인증 번호는 " + authcode + "입니다." + "<br>";
        MimeMessage message =  mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("인증 이메일 발송 실패" + e.getMessage(), e);
        }
    }

    public boolean verifyAuthcode(String email, String authcode) {

        AuthEmail authEmail = authEmailRepository.findById(email)
                .orElseThrow(() -> new BadRequestException("Invalid email."));

        return email.equals(authEmail.getEmail()) && authcode.equals(authEmail.getAuthcode());
    }
}
