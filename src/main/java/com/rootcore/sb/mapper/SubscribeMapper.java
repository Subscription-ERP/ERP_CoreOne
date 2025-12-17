package com.rootcore.sb.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.SubscribeVO;

@Mapper
public interface SubscribeMapper {

	void insertSubscribe(SubscribeVO subscribe);

	SubscribeVO selectSubscribeBySubCode(@Param("subCode") String subCode);

	// ✅ 오늘 결제해야 하는 구독 목록 조회 (스케줄러에서 사용)
	List<SubscribeVO> selectNeedBilling(@Param("today") LocalDate today);

	// ✅ 결제 후 최근/다음 청구일 업데이트
	void updateBillingDates(@Param("recentBillingDate") LocalDate recentBillingDate,
			@Param("nextBillingDate") LocalDate nextBillingDate, @Param("subCode") String subCode);

	// ⭐ 구독 상태 변경 (ACTIVE / PAST_DUE / EXPIRED 등)
	int updateSubsStatus(@Param("subCode") String subCode, @Param("subsStatus") String subsStatus,
			@Param("updatedBy") String updatedBy);

	/** 회사의 ACTIVE 구독을 EXPIRED로 종료 */
	int expireActiveSubscribe(@Param("companyCode") String companyCode, @Param("endDate") LocalDate endDate,
			@Param("updatedBy") String updatedBy, @Param("updateDate") LocalDateTime updateDate);

}
