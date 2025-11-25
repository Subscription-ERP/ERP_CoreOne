package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.HrMapper;
import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {
	
	private final HrMapper hrMapper;
	
	// 급여대장-상여등록-사원조회
	@Override
	public List<UserVO> selectUserList(UserVO param) {
		return hrMapper.selectUserList(param);
	}
}
