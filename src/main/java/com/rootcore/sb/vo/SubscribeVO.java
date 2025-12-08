package com.rootcore.sb.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SubscribeVO {
	  private String subCode;          // SUB_CODE (PK, 구독코드)
	    private String companyCode;      // COMPANY_CODE (회사코드 FK)
	    private String subsStatus;       // SUBS_STATUS (구독상태)
	    private String subsStatusName;       // 구독 활성 / 비활성      ← DB X, 조인값
	    private String inactiveReason;       // CANCEL / EXPIRE / ...   ← DB 컬럼
	    private String inactiveReasonName;   // 구독 해지 / 만료 / ...  ← DB X, 조인값
	    private LocalDate subsStart;     // SUBS_START (구독시작일)
	    private LocalDate subsEnd;       // SUBS_END (구독종료일)

	    private String createdBy;        // CREATED_BY (생성자)
	    private LocalDateTime createDate;// CREATE_DATE (생성일자)
	    private String updatedBy;        // UPDATED_BY (수정자)
	    private LocalDateTime updateDate;// UPDATE_DATE (수정일자)

	    private String planCode;         // PLAN_CODE (플랜코드 FK)
	    private String contractCode;     // CONTRACT_CODE (계약코드)

	    private Integer billingPeriod;   // BILLING_PERIOD (청구 주기)
	    private Integer currentUserCount;// CURRENT_USER_COUNT (현재 사용자 수)
	    private Double currentPrice;     // CURRENT_PRICE (현재 청구금액)
	    private Double Price;
	    private String paymentMethod;
		private LocalDate recentPayment;
		private LocalDate nextPayment;
}
