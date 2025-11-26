package com.rootcore.auth.service;

public interface SmsService {
    void sendAuthCode(String phone, String authCode);
}
