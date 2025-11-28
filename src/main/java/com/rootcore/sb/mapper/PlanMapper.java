package com.rootcore.sb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.PlanVO;

@Mapper
public interface PlanMapper {
	// 모든 플랜 조회 (혹은 필요하면 사용여부 조건 추가)
    List<PlanVO> selectPlanList();

    // 플랜코드로 단건 조회
    PlanVO selectPlanByCode(@Param("planCode") String planCode);
}
