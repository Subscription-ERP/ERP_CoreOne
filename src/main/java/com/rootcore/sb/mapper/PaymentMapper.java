package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.sb.vo.ExistSubsPaymentVO;
import com.rootcore.sb.vo.NewSubsPaymentVO;

@Mapper
public interface PaymentMapper {

	void insert(NewSubsPaymentVO vo);
    void callExistSubsPayment(ExistSubsPaymentVO vo);
}
