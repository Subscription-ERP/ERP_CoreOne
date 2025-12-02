package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.sb.vo.PaymentVO;

@Mapper
public interface PaymentMapper {
	void insertPayment(PaymentVO payment);
//	void insert(NewSubsPaymentVO vo);
//    void callExistSubsPayment(ExistSubsPaymentVO vo);
}
