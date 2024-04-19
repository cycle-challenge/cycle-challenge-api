package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.EmailVerificationCodeRepository;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.util.VerificationCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberSendVerifyEmailService {

    private final EmailVerificationCodeRepository codeRepository;

    private final JavaMailSender mailSender;
    private final VerificationCodeGenerator codeGenerator;


    @Async
    public void send(String email) {

        try {

            String code = codeGenerator.strong();
            String content = String.format("인증에 필요한 코드는 다음과 같습니다.\n%s", code);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("[여행뚝딱] 회원가입 인증 메일입니다.");
            helper.setText(content);

            mailSender.send(message);
            codeRepository.save(email, code);

        } catch (NoSuchAlgorithmException | MessagingException e) {
            log.error(e.getMessage());
            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

}
