package com.rootcore.sb.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.vo.OrderVO;

@Mapper
public interface OrderMapper {

	void insertOrder(OrderVO order);

	OrderVO selectByOrderId(@Param("orderId") String orderId);

	void updateOrderStatus(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus);

	void updateOrderSubCode(@Param("orderId") String orderId, @Param("subCode") String subCode,
			@Param("updatedBy") String updatedBy);

	int updateOrderCompanyCode(@Param("orderId") String orderId, @Param("companyCode") String companyCode,
			@Param("updatedBy") String updatedBy);
	void updateOrderBilling( @Param("orderId") String orderId,
	        @Param("billingStart") LocalDateTime billingStart,
	        @Param("billingEnd") LocalDateTime billingEnd);
}