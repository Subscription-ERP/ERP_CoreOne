package com.rootcore.sb.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentVO {

	private String paymentCode;  // PAYMENT_CODE
	private String subCode;      // SUB_CODE
	private String companyCode;
	private String orderId;

	private String subsStatus;   // SUBS_STATUS (구독상태)
	private String planCode;     // PLAN_CODE

	private Long totalPrice;
	private BigDecimal discountAmount;
	private String paymentStat;
	private String paymentMethod;
	private LocalDate paymentDate;
	private String cardCompany;
	private String paymentKey;
	private LocalDate billingStart;
	private LocalDate billingEnd;
	private String createdBy;
	private LocalDateTime createDate;
	private String updatedBy;
	private LocalDateTime updateDate;
	private String paymentType;
	private String billingKey;
	private String cardNumberMask; 
	
}
