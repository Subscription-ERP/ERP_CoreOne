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
	private String cardCompany;	//내가 화면에 보여줄 매핑용 데이터로 null상태임
	//값을 꺼내와서 넣어야 들어감

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

		public String getDisplayName() {
			if (company != null)
				return company;
			if (issuerCode != null)
				return issuerCode;
			if (acquirerCode != null)
				return acquirerCode;
			return "UNKNOWN";
		}
	}
}
