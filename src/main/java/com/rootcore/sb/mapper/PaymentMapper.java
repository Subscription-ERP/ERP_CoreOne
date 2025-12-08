package com.rootcore.sb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.PaymentVO;
import com.rootcore.sb.vo.SubscribeVO;

@Mapper
public interface PaymentMapper {
	void insertPayment(PaymentVO payment);
//	void insert(NewSubsPaymentVO vo);
//    void callExistSubsPayment(ExistSubsPaymentVO vo);

	  // 코드로 카드사명(한글) 조회
    String findCardCompanyCode(
        @Param("groupCode") String groupCode,
        @Param("code") String code
    );
    // 회사코드로 비활성 구독 이력 조회
    List<SubscribeVO> selectInactiveSubListByComCode(@Param("companyCode") String companyCode);
    SubscribeVO selectSubDetail(@Param("companyCode") String companyCode);
    List<PaymentVO> selectPaymentHistory(@Param("companyCode") String companyCode);
    
}
