package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.sb.vo.SubscribeVO;

@Mapper
public interface SubscribeMapper {
	
	  void insertSubscribe(SubscribeVO subscribe);
}
