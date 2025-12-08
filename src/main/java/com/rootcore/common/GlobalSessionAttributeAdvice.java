package com.rootcore.common;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalSessionAttributeAdvice {

    @ModelAttribute("loginUserId")
    public String loginUserId(HttpSession session) {
        return (String) session.getAttribute("LOGIN_USER_ID");
    }

    @ModelAttribute("loginCompanyCode")
    public String loginCompanyCode(HttpSession session) {
        return (String) session.getAttribute("LOGIN_COMPANY_CODE");
    }
}