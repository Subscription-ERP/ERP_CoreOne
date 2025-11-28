package com.rootcore.sb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rootcore.sb.mapper.PlanMapper;
import com.rootcore.sb.service.PlanService;
import com.rootcore.sb.vo.PlanVO;

@Service
public class PlanServiceImpl implements PlanService {
	  private final PlanMapper planMapper;

	    public PlanServiceImpl(PlanMapper planMapper) {
	        this.planMapper = planMapper;
	    }
	    
	    @Override
	    public List<PlanVO> getPlanList() {
	        List<PlanVO> planList = planMapper.selectPlanList();

	        return planList.stream().map(p -> {
	            PlanVO vo = new PlanVO();
	            vo.setPlanCode(p.getPlanCode());
	            vo.setPlanName(p.getPlanName());
	            vo.setPlanInfo(p.getPlanInfo());
	            vo.setPrice(p.getPrice());
	            vo.setSubsPeriod(p.getSubsPeriod());
	            vo.setUserCount(p.getUserCount());
	            return vo;
	        }).collect(Collectors.toList());
	    }
	    
	    @Override
	    public PlanVO getPlanDetail(String planCode) {
	        PlanVO p = planMapper.selectPlanByCode(planCode);
	        if (p == null) return null;

	        PlanVO vo = new PlanVO();
	        vo.setPlanCode(p.getPlanCode());
	        vo.setPlanName(p.getPlanName());
	        vo.setPlanInfo(p.getPlanInfo());
	        vo.setPrice(p.getPrice());
	        vo.setSubsPeriod(p.getSubsPeriod());
	        vo.setUserCount(p.getUserCount());
	        return vo;
	    }

}
