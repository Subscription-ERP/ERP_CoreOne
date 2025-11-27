package com.rootcore.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.FindIdMapper;
import com.rootcore.auth.mapper.SmsAuthMapper;
import com.rootcore.auth.service.EmailService;
import com.rootcore.auth.service.FindIdService;
import com.rootcore.auth.service.SmsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindIdServiceImpl implements FindIdService {

    private final SmsService smsService;
    private final EmailService emailService;
    private final SmsAuthMapper smsAuthMapper;
    private final FindIdMapper findIdMapper;

    @Override
    public boolean sendAuthCode(String phone) {

        String authCode = String.valueOf((int)(Math.random() * 900000 + 100000));

        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", "ROOT");   // ★ 필수 추가
        param.put("phone", phone);
        param.put("authCode", authCode);
        param.put("companyCode", "ROOT");   

        smsAuthMapper.saveAuthCode(param);
        smsService.sendAuthCode(phone, authCode);

        log.info("인증번호 생성 완료: phone={}, code={}", phone, authCode);
        return true;
    }

    @Override
    public boolean verifyAuthCode(String phone, String code) {
        Map<String, Object> param = new HashMap<>();
        param.put("phone", phone);

        String validCode = smsAuthMapper.getValidAuthCode(param);

        return code != null && code.equals(validCode);
    }

    @Override
    public String getUserId(String name, String phone, String email) {
        return findIdMapper.findUserId(name, phone, email);
    }

    @Override
    public boolean sendUserIdToEmail(String name, String phone, String email) {
        String userId = getUserId(name, phone, email);

        if (userId == null) return false;

        emailService.sendUserId(email, userId);
        return true;
    }
}
