package com.rootcore.auth.service.impl;

import com.rootcore.auth.mapper.SmsAuthMapper;
import com.rootcore.auth.mapper.FindIdMapper;
import com.rootcore.auth.service.FindIdService;
import com.rootcore.auth.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindIdServiceImpl implements FindIdService {

    private final SmsService smsService;
    private final SmsAuthMapper smsAuthMapper;
    private final FindIdMapper findIdMapper;

    @Override
    public boolean sendAuthCode(String phone) {
        String authCode = String.valueOf((int)(Math.random()*900000+100000));

        Map<String, Object> param = new HashMap<>();
        param.put("phone", phone);
        param.put("authCode", authCode);
        smsAuthMapper.saveAuthCode(param);

        smsService.sendAuthCode(phone, authCode);

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
    public String getUserId(String name, String phone) {
        return findIdMapper.findUserId(name, phone);
    }
}
