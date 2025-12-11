package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.hr.mapper.HrDocumentMapper;
import com.rootcore.hr.service.HrDocumentService;
import com.rootcore.hr.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrDocumentServiceImpl implements HrDocumentService{
	
	final private HrDocumentMapper hrDocumentMapper;

	// 사원조회 및 검색
	@Override
	public List<UserVO> selectUser(String userName, String userId) {
		return hrDocumentMapper.selectUser(userName, userId);
	}
	
	

}
