package com.rootcore.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * 아이디 안내 메일 발송
     */
    public void sendUserId(String toEmail, String userId) {
        String subject = "[CoreOne] 아이디 찾기 안내";

        String content = "<h3>CoreOne 아이디 안내</h3>" +
                "<p>회원님의 아이디는 <b>" + userId + "</b> 입니다.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("아이디 안내 이메일 전송 완료 -> {}", toEmail);

        } catch (MessagingException e) {
            log.error("아이디 이메일 전송 실패", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 비밀번호 재설정 링크 발송
     */
    public void sendPasswordResetLink(String toEmail, String resetLink) {
        String subject = "[CoreOne] 비밀번호 재설정 안내";

        String content = "<h2>비밀번호 재설정 안내</h2>" +
                "<p>아래 링크를 클릭하여 비밀번호를 재설정하세요.</p>" +
                "<p><a href='" + resetLink + "' style='font-size:16px; color:#0D6EFD;'>비밀번호 재설정하기</a></p>" +
                "<br>" +
                "<p>※ 본 링크는 보안을 위해 일정 시간 후 만료됩니다.</p>" +
                "<p>※ 본인이 요청하지 않은 경우 이 메일을 무시해 주세요.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML 형식

            mailSender.send(message);
            log.info("비밀번호 재설정 링크 이메일 전송 완료 -> {}", toEmail);

        } catch (MessagingException e) {
            log.error("비밀번호 재설정 링크 이메일 전송 실패", e);
            throw new RuntimeException(e);
        }
    }
}
