package com.rootcore.hr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.rootcore.hr.vo.PayrollVO;
import com.rootcore.hr.vo.UserVO;

@Mapper
public interface HrMapper {

	List<UserVO> selectAllUserList();

}
