package com.rootcore.auth.web;

import com.rootcore.auth.service.FindIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class FindIdController {

    private final FindIdService findIdService;

    /* 아이디 찾기 페이지 */
    @GetMapping("/find_id")
    public String findIdPage() {
        return "auth/find_id";   // templates/auth/find_id.html
    }

    /* 1) 인증번호 전송 */
    @PostMapping("/send_code")
    @ResponseBody
    public String sendCode(@RequestParam String phone) {
        findIdService.sendAuthCode(phone);
        return "OK";
    }

    /* 2) 인증번호 검증 */
    @PostMapping("/verify_code")
    @ResponseBody
    public boolean verifyCode(@RequestParam String phone,
                              @RequestParam String code) {
        return findIdService.verifyAuthCode(phone, code);
    }

    /* 3) 이름+휴대폰으로 아이디 조회 */
    @PostMapping("/find_id_do")
    @ResponseBody
    public String findId(@RequestParam String name,
                         @RequestParam String phone) {

        String userId = findIdService.getUserId(name, phone);
        return userId == null ? "" : userId;
    }
}
