package com.rootcore.sb.vo;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
// Toss Payments에서 결제를 승인한 후 내려주는 응답 모델 
public class TossConfirmResponseVO {

	private String paymentKey;
	private String orderId;
	private String status; // SUCCESS, FAILED 등
	private Long totalAmount;
	private OffsetDateTime approvedAt;
	private String cardCompany; // 내가 화면에 보여줄 매핑용 데이터로 null상태임
	private String cardCompanyCode;
	// 값을 꺼내와서 넣어야 들어감
	
	// 정기결제(빌링결제)일 때만 값이 들어오는 필드 (일반결제면 null)
    private String billingKey;
    private String customerKey;
    

	// 🔹 토스에서 내려주는 결제수단 (카드, 계좌이체, 간편결제 등)
	private String method;

	// 🔹 카드 결제일 경우 카드 정보가 여기에 들어옴 (없으면 null)
	private Card card;

	// 👉 여기서부터가 "내부 클래스"라서 따로 import 하면 안 됨!!
	@Data
	public static class Card {
		private String company; // 카드사명 (신한, 국민 등)
		private String issuerCode; // 발급사
		private String acquirerCode; // 매입사

		  // ✔ 카드사 "이름"은 issuerCode를 기준으로 공통코드에서 조회해야 한다.
	    public String getCardCompanyCode() {
	        return issuerCode;  // 발급사 코드가 카드사 코드
	    }

	    // ✔ 회사명은 Toss가 줄 수도 있지만 대부분 null이므로 fallback이 있으면 안됨.
	    public String getCardCompanyName() {
	        return company;  // null이면 null
	    }
	}
}
