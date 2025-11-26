package com.rootcore.auth.service;

import java.util.List;
import com.rootcore.auth.vo.LoginFindIdVO;

public interface FindIdService {

	    List<LoginFindIdVO> findIdByEmail(String email);
	}


