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

    @GetMapping("/find_id")
    public String findIdPage() {
        return "auth/find_id";  // find_id.html
    }

    @PostMapping("/send_code")
    @ResponseBody
    public String sendCode(@RequestParam String phone) {
        boolean result = findIdService.sendAuthCode(phone);
        return result ? "OK" : "FAIL";
    }

    @PostMapping("/verify_code")
    @ResponseBody
    public String verify(@RequestParam String phone,
                         @RequestParam String code) {
        boolean ok = findIdService.verifyAuthCode(phone, code);
        return ok ? "true" : "false";
    }

    @PostMapping("/find_id_do")
    @ResponseBody
    public String sendUserId(@RequestParam String name,
                             @RequestParam String phone,
                             @RequestParam String email) {

        boolean ok = findIdService.sendUserIdToEmail(name, phone, email);
        return ok ? "OK" : "FAIL";
    }
}
