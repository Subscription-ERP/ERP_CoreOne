package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rootcore.sb.domain.Order;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);

    Order selectByOrderId(@Param("orderId") String orderId);

    void updateOrderStatus(@Param("orderId") String orderId,
                           @Param("orderStatus") String orderStatus);

    void updateOrderSubCode(@Param("orderId") String orderId,
                            @Param("subCode") String subCode,
                            @Param("updatedBy") String updatedBy);
}