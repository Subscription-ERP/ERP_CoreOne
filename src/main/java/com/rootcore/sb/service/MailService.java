package com.rootcore.sb.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    /**
     * 회사 관리자 계정 생성 안내 메일 발송
     */
    public void sendAccountMail(String managerEmail, String userId, String tempPassword,  String companyCode) {

        // 1️⃣ 담당자 이메일 null / 빈값 체크
        if (managerEmail == null || managerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("담당자 이메일이 없어 계정 안내 메일을 발송할 수 없습니다.");
        }

        // 2️⃣ 메일 객체 생성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(managerEmail); // ← company.getManagerEmail() 넘겨받은 값
        message.setSubject("[ERP] 관리자 계정 생성 안내");
        message.setText(
            "ERP 관리자 계정이 생성되었습니다.\n\n" +
            "회사코드: " + companyCode + "\n" +
            "아이디: " + userId + "\n" +
            "임시 비밀번호: " + tempPassword + "\n\n" +
            "보안을 위해 최초 로그인 후 반드시 비밀번호를 변경해주세요."
        );

        // 3️⃣ 메일 발송
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // 메일 발송 실패 시 트랜잭션 롤백용 예외
            throw new IllegalStateException("계정 안내 메일 발송에 실패했습니다.", e);
        }
    }
}