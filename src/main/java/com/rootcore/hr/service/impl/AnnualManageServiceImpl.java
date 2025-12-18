package com.rootcore.hr.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rootcore.hr.mapper.AnnualManageMapper;
import com.rootcore.hr.service.AnnualManageService;
import com.rootcore.hr.vo.AnnualLeaveDetailVO;
import com.rootcore.hr.vo.AnnualLeaveVO;

import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>제목 : 연차 관리 페이지 Impl</li>
 * <li>설명 : 연차 관리와 관련된 로직들을 관리하는 Service Implementaion입니다.</li>
 * </ul>
 * 
 * @author 장준현
 */
@Service
@RequiredArgsConstructor
public class AnnualManageServiceImpl implements AnnualManageService {

	private final AnnualManageMapper annualManageMapper;

	// 연차조회
	@Override
	public List<AnnualLeaveDetailVO> selectAnnualManageList(AnnualLeaveDetailVO param) {
		return annualManageMapper.selectAnnualManageList(param);
	}

	// 연차현황
	@Override
	public AnnualLeaveVO selectmyAnnualStatus(AnnualLeaveVO param) {
		return annualManageMapper.selectmyAnnualStatus(param);
	}

	/**
	 * 제목 : 연차 신청 하는 메소드 설명 : 연차 신청을 하면 연차상세관리 테이블과 근태 테이블에는 insert가 되고 연차관리 테이블에는
	 * update가 됩니다.
	 * 
	 * @author 장준현
	 * @param param 연차 신청할때 필요한 데이터를 담은 VO
	 * @return totalSuccess 각각 쿼리문들이 실행되고 받은 숫자들을 다 더한 값
	 */
	@Override
	@Transactional
	public int insertmyAnnualApply(AnnualLeaveDetailVO param) {
		int totalSuccess = 0;
		
		// 연차신청하기전에 연차상세관리 테이블에서 등록할려는 연차랑 겹치는지 확인하는 쿼리
		

		// 연차신청하면 연차상세관리 테이블에 등록되고
		totalSuccess += annualManageMapper.insertmyAnnualApply(param);

		// 연차관리 테이블은 update가 되야하고(잔여연차, 총사용일수)
		totalSuccess += annualManageMapper.updateAnnualApply(param);

		/*
		 * 근태 태이블에 insert, 연차 신청을 하면 근태쪽에도 추가해줘야한다. 만약에 'b1'연차인경우는 usedDays일 수 만큼 반복해서
		 * annualStartDate + 1해서 넣어줘야한다. 예시)usedDays가 2일인경우 쿼리문을 2번 반복해서 넣어줘야하는데 이때
		 * annualStartDate를 처음은 그냥 넣고 다음날짜를 +1해서 넣어줘야함
		 */
		// 반복 횟수 지정
		// 만약에 b1경우(연차)는 uesdDays를 그대로 사용하고 나머지는(반차) 한번만 반복한다는 의미 
		int loopCount = (param.getLeaveType().equals("b1")) ? (int) param.getUsedDays() : 1;
		
		// 형식 지정
		// 나 이런 형식으로 쓸꺼에요~ 라고 지정해주는거다
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		// param에 있는 annualStartDate를 String타입의 dateString에 담는다
		String dateString = param.getAnnualStartDate();
		
		// dateString이 yyyy-MM-dd 이거 뒤에 붙는 것들을 다 잘라버린다
		if (dateString.length() > 10) {
			dateString = dateString.substring(0, 10);
		}

		// 
		LocalDate originalDate = LocalDate.parse(dateString, formatter);

		
		for (int i = 0; i < loopCount; i++) {
			LocalDate calculatedDate = originalDate.plusDays(i);
			param.setAnnualStartDate(calculatedDate.format(formatter));
			totalSuccess += annualManageMapper.insertAttendance(param);
		}

		return totalSuccess;
	}

}
