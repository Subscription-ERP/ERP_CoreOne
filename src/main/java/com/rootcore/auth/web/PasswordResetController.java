package com.rootcore.auth.web;

import com.rootcore.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * 비밀번호 재설정 링크 발송 화면 (아이디 + 이메일 입력)
     */
    @GetMapping("/auth/password_reset/send")
    public String passwordResetSendPage() {
        return "auth/password_reset_send";
    }

    /**
     * 비밀번호 재설정 링크 발송 처리
     */
    @PostMapping("/auth/password_reset/send")
    public String sendPasswordResetLink(@RequestParam String userId,
                                        @RequestParam String email,
                                        Model model) {

        boolean result = passwordResetService.sendResetLink(userId, email);

        model.addAttribute("result", result);
        model.addAttribute("email", email);

        return "auth/password_reset_send_result";
    }

    /**
     * 비밀번호 재설정 화면 진입 (메일에서 링크 클릭)
     * /auth/password-reset?token=xxx
     */
    @GetMapping("/auth/password_reset")
    public String passwordResetPage(@RequestParam(required = false) String token,
                                    Model model) {

        boolean valid = passwordResetService.isValidToken(token);
        model.addAttribute("valid", valid);
        model.addAttribute("token", token);

        return "auth/password_reset";
    }

    /**
     * 새 비밀번호 저장 처리
     */
    @PostMapping("/auth/password_reset")
    public String doPasswordReset(@RequestParam String token,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  Model model) {

        if (!newPassword.equals(confirmPassword)) {	// security 적용 후 암호화 해야됨 / 암호화 된 것을 비교할 때는 match함수가 따로있다
            model.addAttribute("valid", true);
            model.addAttribute("token", token);
            model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "auth/password_reset";
        }

        boolean result = passwordResetService.resetPassword(token, newPassword);

        model.addAttribute("result", result);
        return "auth/password_reset_result";
    }
}
