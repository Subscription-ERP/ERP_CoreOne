package com.rootcore.auth.service.impl;

import com.rootcore.auth.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.sender-number}")
    private String senderNumber;

    public SmsServiceImpl(
            @Value("${coolsms.api-key}") String apiKey,
            @Value("${coolsms.api-secret}") String apiSecret) {

        this.messageService = NurigoApp.INSTANCE.initialize(
                apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Override
    public void sendAuthCode(String phone, String authCode) {

        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phone.replaceAll("[^0-9]", ""));
        message.setText("[CoreOne] 인증번호 [" + authCode + "]");

        try {
            MultipleDetailMessageSentResponse res = messageService.send(message);
            log.info("CoolSMS 발송 성공 = {}", res);

        } catch (Exception e) {
            log.error("CoolSMS 발송 실패", e);
        }
    }
}
