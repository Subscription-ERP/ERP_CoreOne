package com.rootcore.fi.web;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.UnitPriceVO;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fi")
public class FiRestController {
	private final UnitPriceService unitPriceService;
	
    @GetMapping("/unitprice")
    public List<UnitPriceVO> getUnitPriceList(UnitPriceVO param) {
        return unitPriceService.selectList(param);
    }
    
    @PostMapping("/unitprice")
    public int registUnitPrice(@RequestBody UnitPriceVO param) {
        return unitPriceService.insertUnitPrice(param);
    }
    
    @GetMapping("/unitprice/check")
    public  Map<String, Object> unitPriceCheck(UnitPriceVO param) {
        int cnt = unitPriceService.checkUnitPrice(param);
        return Map.of("cnt", cnt);
    }
    @PutMapping("/unitprice")
    public int modifyUnitPrice(@RequestBody UnitPriceVO param) {
        return unitPriceService.updateUnitPrice(param);
    }
    
    
    
}
