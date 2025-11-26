package com.rootcore.auth.service.impl;

import com.rootcore.auth.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;

    @Value("${coolsms.sender-number}")
    private String senderNumber;

    @Override
    public void sendAuthCode(String phone, String authCode) {

        try {
            URL url = new URL("https://api.coolsms.co.kr/sms/v1/messages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes()));
            conn.setDoOutput(true);

            String json = "{"
                    + "\"to\": \"" + phone + "\","
                    + "\"from\": \"" + senderNumber + "\","
                    + "\"text\": \"[CoreOne] 인증번호 [" + authCode + "]\""
                    + "}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            System.out.println("CoolSMS Response Code = " + conn.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
