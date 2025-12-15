package com.rootcore.sb.service;

import org.springframework.stereotype.Component;

import com.rootcore.sb.mapper.PaymentMapper;
import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.TossConfirmResponseVO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TossCardInfoEnricher {
	  private final PaymentMapper paymentMapper;

	    public void applyCardInfo(TossConfirmResponseVO tossResponse, PaymentVO payment) {
	        if (tossResponse == null || payment == null) return;

	        // ✅ method 비교하지 말고 card 객체 존재로 판단 (가장 안전)
	        TossConfirmResponseVO.Card card = tossResponse.getCard();
	        if (card == null) return;

	        String cardCode = card.getCardCompanyCode();
	        String maskedNumber = card.getNumber(); // 마스킹된 카드번호

	        // 카드사 코드 -> 카드사명
	        if (cardCode != null && !cardCode.isBlank()) {
	            String cardName = paymentMapper.findCardCompanyCode("OP", cardCode);

	            // 응답 객체 세팅
	            tossResponse.setCardCompany(cardName);
	            tossResponse.setCardCompanyCode(cardCode);

	            // ✅ DB 저장은 '코드' 추천(가능하면)
	            // payment.setCardCompanyCode(cardCode);  // 필드 있으면 이걸 추천
	            payment.setCardCompany(cardName);        // 너는 cardCompany 하나만 쓰는 듯해서 이름 저장
	        }

	        // 마스킹 번호 세팅
	        if (maskedNumber != null && !maskedNumber.isBlank()) {
	            tossResponse.setCardNumberMask(maskedNumber);
	            payment.setCardNumberMask(maskedNumber);
	        }
	    }
}
