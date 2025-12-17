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

    /** 링크 발송 화면 */
    @GetMapping("/auth/password_reset/send")
    public String passwordResetSendPage() {
        return "auth/password_reset_send";
    }

    /** 링크 발송 처리 */
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
                request.getUserId(), request.getEmail());

        result.put("status", success ? "OK" : "FAIL");
        result.put("message", success
                ? "비밀번호 재설정 링크를 이메일로 발송했습니다."
                : "아이디 또는 이메일이 일치하지 않습니다.");

        return result;
    }

    /** 비밀번호 입력 화면 */
    @GetMapping("/auth/password_reset")
    public String passwordResetPage(@RequestParam(required = false) String token,
                                    Model model) {

        boolean valid = passwordResetService.isValidToken(token);

        model.addAttribute("valid", valid);
        model.addAttribute("token", token);

        return "auth/password_reset";
    }

    /** 비밀번호 변경 처리 */
    @PostMapping("/auth/password_reset")
    public String doPasswordReset(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("valid", true);
            model.addAttribute("token", token);
            model.addAttribute("error", "비밀번호가 서로 일치하지 않습니다.");
            return "auth/password_reset";
        }

        Map<String, String> error = new HashMap<>();
        boolean result = passwordResetService.resetPassword(token, newPassword, error);

        if (!result) {
            model.addAttribute("valid", true);
            model.addAttribute("token", token);
            model.addAttribute("error", error.get("msg"));
            return "auth/password_reset";
        }

        return "redirect:/auth/password_reset/result?result=true";
    }

    /** 결과 화면 */
    @GetMapping("/auth/password_reset/result")
    public String passwordResetResultPage(@RequestParam boolean result, Model model) {

        model.addAttribute("result", result);
        return "auth/password_reset_result";
    }
}
