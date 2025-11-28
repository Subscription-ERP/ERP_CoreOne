package com.rootcore.common.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.common.service.CommonService;
import com.rootcore.common.vo.CommonVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/com")
public class CommonRestController {

    final CommonService commonService;

    @GetMapping("/type")
    public List<CommonVO> custTypeList(String groupCode) {
        return commonService.selectType(groupCode);
    }
    
    @GetMapping("/commonCode")
    public List<CommonVO> commonCode(String code){
    	return commonService.selectCode(code);
    }

    @GetMapping("/commonCodes")
    public Map<String,List<CommonVO>> commonCodes(String[] code){
    	return commonService.selectCodes(code);
    }

    
}
