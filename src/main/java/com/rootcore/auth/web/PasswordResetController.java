package com.rootcore.auth.web;

import com.rootcore.auth.service.PasswordResetService;
import com.rootcore.auth.vo.PasswordResetRequestVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /** 비밀번호 재설정 링크 발송 화면 */
    @GetMapping("/auth/password_reset/send")
    public String passwordResetSendPage() {
        return "auth/password_reset_send";
    }

    /** 이메일로 비밀번호 재설정 링크 발송 (AJAX) */
    @PostMapping("/auth/password_reset/send")
    @ResponseBody
    public Map<String, Object> sendPasswordResetLink(
            @Valid PasswordResetRequestVO request,
            BindingResult bindingResult) {

        Map<String, Object> result = new HashMap<>();

        if (bindingResult.hasErrors()) {
            result.put("status", "FAIL");
            result.put("message", bindingResult.getFieldError().getDefaultMessage());
            return result;
        }

        boolean success = passwordResetService.sendResetLink(
                request.getUserId(),
                request.getEmail()
        );

        if (success) {
            result.put("status", "OK");
            result.put("message", "비밀번호 재설정 링크를 이메일로 발송했습니다.");
        } else {
            result.put("status", "FAIL");
            result.put("message", "아이디 또는 이메일이 일치하지 않습니다.");
        }

        return result;
    }

    /** 비밀번호 재설정 화면 */
    @GetMapping("/auth/password_reset")
    public String passwordResetPage(@RequestParam(required = false) String token,
                                    Model model) {

        boolean valid = passwordResetService.isValidToken(token);

        model.addAttribute("valid", valid);
        model.addAttribute("token", token);

        return "auth/password_reset";
    }

    /** 비밀번호 실제 변경 처리 */
    @PostMapping("/auth/password_reset")
    public String doPasswordReset(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        // 비밀번호 확인 불일치
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/auth/password_reset/result?result=false";
        }

        boolean result = passwordResetService.resetPassword(token, newPassword);

        // 결과 페이지로 QueryString 방식 전달
        return "redirect:/auth/password_reset/result?result=" + result;
    }

    /** 결과 화면 */
    @GetMapping("/auth/password_reset/result")
    public String passwordResetResultPage(
            @RequestParam(defaultValue = "false") boolean result,
            Model model) {

        model.addAttribute("result", result);

        return "auth/password_reset_send_result";
    }

}
