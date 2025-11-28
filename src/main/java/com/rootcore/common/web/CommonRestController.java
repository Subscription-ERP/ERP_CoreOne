package com.rootcore.common.web;

import com.rootcore.common.service.CommonService;
import com.rootcore.common.vo.CommonVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/com")
public class CommonRestController {

    @Autowired
    CommonService commonService;

    @GetMapping("/type")
    public List<CommonVO> custTypeList(String groupCode) {
        return commonService.selectType(groupCode);
    }

}
