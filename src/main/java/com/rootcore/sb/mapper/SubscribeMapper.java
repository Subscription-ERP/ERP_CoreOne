package com.rootcore.sb.mapper;

import java.time.LocalDate;
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
	    void updateBillingDates(@Param("subCode") String subCode,
	                            @Param("recentBillingDate") LocalDate recentBillingDate,
	                            @Param("nextBillingDate") LocalDate nextBillingDate);
	    
	    // ⭐ 구독 상태 변경 (ACTIVE / PAST_DUE / EXPIRED 등)
	    int updateSubsStatus(
	        @Param("subCode") String subCode,
	        @Param("subsStatus") String subsStatus,
	        @Param("updatedBy") String updatedBy
	    );
	    
	    /** 빌링키 변경 (결제수단 변경) */
	    int updateBillingKey(
	        @Param("subCode") String subCode,
	        @Param("billingKey") String billingKey,
	        @Param("updatedBy") String updatedBy
	    );
	    /** 만료 재구독 처리 */
	    int updateResubscribe(
	        @Param("subCode") String subCode,
	        @Param("subsStatus") String subsStatus,
	        @Param("subsStart") LocalDate subsStart,
	        @Param("subsEnd") LocalDate subsEnd,
	        @Param("recentBillingDate") LocalDate recentBillingDate,
	        @Param("nextBillingDate") LocalDate nextBillingDate,
	        @Param("updatedBy") String updatedBy
	    );
	    
	    
}
