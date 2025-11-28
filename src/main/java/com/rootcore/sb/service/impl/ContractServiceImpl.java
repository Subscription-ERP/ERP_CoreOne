package com.rootcore.sb.service.impl;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.rootcore.sb.mapper.ContractMapper;
import com.rootcore.sb.service.ContractService;
import com.rootcore.sb.vo.ContractVO;

@Service
public class ContractServiceImpl implements ContractService {
	
	private final ContractMapper contractMapper;
	
	   public ContractServiceImpl(ContractMapper contractMapper) {
	        this.contractMapper = contractMapper;
	    }
	   @Override
	    public String registerContract(ContractVO vo) {


	        // 계약 시작 / 종료일 계산
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(vo.getContractStart());
	        cal.add(Calendar.MONTH, vo.getSubsPeriod());
	        vo.setContractEnd(cal.getTime());

	        // 도메인 매핑
	        ContractVO contract = new ContractVO();
	        contract.setCompanyCode(vo.getCompanyCode());
	        contract.setCompanyName(vo.getCompanyName());
	        contract.setCeoName(vo.getCeoName());
	        contract.setSignName(vo.getSignName());
	        contract.setFilePath(vo.getFilePath());

	        contract.setPlanCode(vo.getPlanCode());
	        contract.setContractStart(vo.getContractStart());
	        contract.setContractEnd(vo.getContractEnd());

	        contract.setSubsPeriod(vo.getSubsPeriod());
	        contract.setUserCount(vo.getUserCount());

	        contract.setTotalPrice(vo.getTotalPrice());
	        contract.setDiscountAmount(vo.getDiscountAmount());

	        contract.setStatus("ACTIVE");  // 기본 상태

	        contractMapper.insertContract(contract);
	        return "";//contractCode;
	    }
	   
}
