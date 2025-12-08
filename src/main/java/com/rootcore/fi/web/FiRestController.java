package com.rootcore.fi.web;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.fi.service.CreditService;
import com.rootcore.fi.service.UnitPriceService;
import com.rootcore.fi.vo.CreditVO;
import com.rootcore.fi.vo.UnitPriceVO;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fi")
public class FiRestController {
	private final UnitPriceService unitPriceService;
	private final CreditService creditService;
	
	//
	//단가관리
	//
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
    
    //
    //여신관리
    //
    @GetMapping("/credit")
    public List<CreditVO> getCreditList(CreditVO param){
    	System.out.println(param.custCode);
    	return creditService.selectList(param);
    }
    @PutMapping("/credit")
    public int upsertCredit(@RequestBody CreditVO param) {
    	return creditService.mergeCredit(param);
    }
    
}
