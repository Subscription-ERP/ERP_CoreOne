package com.rootcore.sb.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.sb.vo.ContractVO;

@Mapper
public interface ContractMapper {
	
	void insertContract(ContractVO contract);
}
