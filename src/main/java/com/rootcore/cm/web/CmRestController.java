package com.rootcore.cm.web;

import com.rootcore.cm.service.SkuService;
import com.rootcore.cm.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cm")
public class CmRestController {

    @Autowired
    SkuService skuService;

    @GetMapping("/skuList")
    public List<SkuVO> skuList(
            @RequestParam(required = false) String custCode,
            @RequestParam(required = false) String unitPriceType,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String skuName
    ) {
        return skuService.getSku(custCode, unitPriceType, sku, skuName);
    }

}
