package com.rootcore.cm.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.rootcore.cm.service.ItemMasterService;
import com.rootcore.cm.vo.SkuVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cm/item")
@RequiredArgsConstructor
public class ItemMasterApiController {

    private final ItemMasterService itemMasterService;

    /** 품번 조회 */
    @PostMapping("/search")
    public List<SkuVO> search(@RequestBody SkuVO cond) {
        return itemMasterService.search(cond);
    }

    /** 저장 (신규 / 수정 공용) */
    @PostMapping("/save")
    public void save(@RequestBody SkuVO skuVO) {
        itemMasterService.save(skuVO);
    }

    /** SKU 자동생성 (GET) */
    @GetMapping("/sku")
    public Map<String, String> generateSku(
            @RequestParam String companyCode
    ) {
        return Map.of(
            "sku", itemMasterService.generateSku(companyCode)
        );
    }
}
