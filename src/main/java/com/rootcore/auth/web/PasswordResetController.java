package com.rootcore.auth.web;

import com.rootcore.auth.service.PasswordResetService;
import com.rootcore.auth.vo.PasswordResetRequestVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/auth/password_reset/send")
    public String passwordResetSendPage() {
        return "auth/password_reset_send";
    }

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

    @GetMapping("/auth/password_reset")
    public String passwordResetPage(@RequestParam(required = false) String token,
                                    Model model) {

        boolean valid = passwordResetService.isValidToken(token);

        model.addAttribute("valid", valid);
        model.addAttribute("token", token);

        return "auth/password_reset";
    }

    @PostMapping("/auth/password_reset")
    public String doPasswordReset(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("valid", true);
            redirectAttributes.addFlashAttribute("token", token);
            redirectAttributes.addFlashAttribute("error", "비밀번호가 서로 일치하지 않습니다.");
            return "redirect:/auth/password_reset?token=" + token;
        }

        boolean result = passwordResetService.resetPassword(token, newPassword);

        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/auth/password_reset/result";
    }

    @GetMapping("/auth/password_reset/result")
    public String passwordResetResultPage(@ModelAttribute("result") Boolean result,
                                          Model model) {

        model.addAttribute("result", result); // 직접 넣어주기

        return "auth/password_reset_result";
    }

}
