package com.golym.mylog.service;

import com.golym.mylog.common.constants.RoleType;
import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.exception.ConflictException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.golym.mylog.model.dto.common.UserDto;
import com.golym.mylog.model.dto.request.RequestSendAuthCodeDto;
import com.golym.mylog.model.dto.request.RequestSignupDto;
import com.golym.mylog.model.entity.*;
import com.golym.mylog.model.entity.id.UserRoleMappingId;
import com.golym.mylog.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final UserRepository userRepository;
    private final AuthEmailRepository authEmailRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Async
    public void sendAuthcode(RequestSendAuthCodeDto params) {
        String toEmail = params.getEmail();
        String authcode = CodeGenerator.generateAuthcode();

        // 인증 메일 생성 및 전송
        sendEmail(toEmail, authcode);

        // 메일주소, 인증코드를 redis에 저장
        AuthEmailEntity authEmailEntity = new AuthEmailEntity(toEmail, authcode);
        authEmailRepository.save(authEmailEntity);
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
        AuthEmailEntity authEmailEntity = authEmailRepository.findById(email)
                .orElseThrow(() -> new BadRequestException("Invalid email."));

        return email.equals(authEmailEntity.getEmail()) && authcode.equals(authEmailEntity.getAuthcode());
    }

    public boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isExistNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void signup(RequestSignupDto params) {

        UserEntity user = UserEntity.builder()
                .userId(CodeGenerator.generateID("U"))
                .email(params.getEmail())
                .password(passwordEncoder.encode(params.getPassword()))
                .username(params.getUsername())
                .nickname(params.getNickname())
                .isActive(true)
                .build();
        userRepository.save(user);

        RoleEntity role = new RoleEntity(RoleType.ROLE_USER);

        UserRoleMappingId id = new UserRoleMappingId();
        id.setUserId(user.getUserId());
        id.setRoleId(role.getRoleId());
        UserRoleMappingEntity userRoleMapping = new UserRoleMappingEntity(id, user, role);
        userRoleMappingRepository.save(userRoleMapping);
    }

    public UserDto getUser(String userId) {
        UserEntity user = userRepository.findByUserIdAndIsActive(userId, true)
                .orElseThrow(() -> new BadRequestException("Not found user. userId: " + userId));

        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Transactional
    public void updateProfileImage(String userId, String profileImage) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Not found user. userId: " + userId));

        user.updateProfileImage(profileImage);
        userRepository.save(user);
    }

    @Transactional
    public void updateNickname(String userId, String nickname) {
        // 닉네임 중복 확인
        if(isExistNickname(nickname))
            throw new ConflictException("The resource already exists. nickname=" + nickname);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Not found user. userId: " + userId));

        user.updateNickname(nickname);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(String userId, String password, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Not found user. userId: " + userId));

        // 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadRequestException("Invalid request update password.");

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Not found user. userId: " + userId));

        // 회원 비활성화
        user.deactivate();

        // 회원이 작성한 게시글 비활성화
        List<PostEntity> postEntityList = postRepository.findAllByUser_UserIdAndIsActive(userId, true);
        for (PostEntity postEntity : postEntityList)
            postEntity.deletePost();

        // 회원이 작성한 댓글 비활성화
        List<CommentEntity> commentEntityList = commentRepository.findAllByUser_UserIdAndIsActive(userId, true);
        for (CommentEntity commentEntity : commentEntityList)
            commentEntity.deleteComment();

        userRepository.save(user);
        postRepository.saveAll(postEntityList);
        commentRepository.saveAll(commentEntityList);
    }
}
