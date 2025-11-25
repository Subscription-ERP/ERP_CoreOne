package com.rootcore.fi.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.UnitPriceVO;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fi")
public class FiRestController {
	@Autowired UnitPriceService unitPriceService;
	
    @GetMapping("/unitprice")
    public List<UnitPriceVO> getUnitPriceList(UnitPriceVO param) {
        return unitPriceService.selectList(param);
    }
}
