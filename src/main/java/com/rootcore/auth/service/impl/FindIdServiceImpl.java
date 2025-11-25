package com.rootcore.auth.service.impl;


	import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootcore.auth.mapper.FindIdMapper;
import com.rootcore.auth.service.FindIdService;
import com.rootcore.auth.vo.LoginFindIdVO;

	@Service
	public class FindIdServiceImpl implements FindIdService {

	    @Autowired
	    private FindIdMapper findIdMapper;

	    @Override
	    public List<LoginFindIdVO> findIdByEmail(String email) {
	        return findIdMapper.findIdByEmail(email);
	    }
	}


