package com.rootcore.auth.service;

public interface FindIdService {
    boolean sendAuthCode(String phone);
    boolean verifyAuthCode(String phone, String code);
    String getUserId(String name, String phone);
}
