package com.rootcore.auth.service;

import com.rootcore.auth.vo.LoginVO;

public interface LoginService {

    LoginVO login(String userId, String password);

}
