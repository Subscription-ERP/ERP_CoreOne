package com.rootcore.sb.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentVO {

	private String paymentCode;  // PAYMENT_CODE
	private String subCode;      // SUB_CODE
	private String companyCode;
	private String orderId;

	private String subsStatus;   // SUBS_STATUS (구독상태)
	private LocalDateTime subsStart; // SUBS_START
	private LocalDateTime subsEnd;   // SUBS_END
	private String planCode;     // PLAN_CODE

	private Long totalPrice;
	private String paymentStat;
	private String paymentMethod;
	private LocalDateTime paymentDate;
	private String cardCompany;
	private String paymentKey;
	private LocalDateTime recentPayment;
	private LocalDateTime nextPayment;
	private LocalDateTime billingStart;
	private LocalDateTime billingEnd;
	private String createdBy;
	private LocalDateTime createDate;
	private String updatedBy;
	private LocalDateTime updateDate;
}
