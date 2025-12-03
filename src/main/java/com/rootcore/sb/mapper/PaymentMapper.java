package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.PaymentVO;

@Mapper
public interface PaymentMapper {
	void insertPayment(PaymentVO payment);
//	void insert(NewSubsPaymentVO vo);
//    void callExistSubsPayment(ExistSubsPaymentVO vo);

    // 🔹 토스에서 내려온 카드사명으로 우리 공통코드 CODE 찾기
    String findCardCompanyCode(@Param("groupCode") String groupCode,
                               @Param("displayName") String displayName);
}
