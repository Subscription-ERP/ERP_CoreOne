package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rootcore.common.service.CommonService;
import com.rootcore.hr.mapper.HrMapper;
import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.CertificationVO;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;
import com.rootcore.hr.vo.WorkExperienceVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

	private final HrMapper hrMapper;

	// 사원 	
	// 전체조회
	@Override
	public List<UserVO> selectAllUserList() {
		return hrMapper.selectAllUserList();
	}
	
	// 상세조회 - 기본사항/자격증/경력사항/이력
	@Override
	public UserVO selectUserDetail(String userId) {
		// 1) 사원 기본사항 조회
	    UserVO user = hrMapper.selectUserDetail(userId);
		
	    // 2) 없는 사원일 경우
        if (user == null) {
        	return null; 
        }
		
        // 3) 자격증/경력사항/이력 조회
        List<CertificationVO> certi = hrMapper.selectUserCertification(userId); 
        List<WorkExperienceVO> wex = hrMapper.selectUserWorkExperience(userId);
        List<UserHistoryVO> hist = hrMapper.selectUserHistory(userId);
        
		// 4) userVO안의 리스트필드에 세팅
        user.setCertificationList(certi);
        user.setWorkExperienceList(wex);
        user.setHistoryList(hist);
			
		return user;
	}
	
	// 부서조회
	@Override
	public List<DeptVO> selectDeptMaster() {
		return hrMapper.selectDeptMaster();
	}
	
	// 검색
	@Override
	public List<UserVO> selectUserSearch(UserSearchVO userSearchVO) {
		return hrMapper.selectUserSearch(userSearchVO);
	}
		



}
