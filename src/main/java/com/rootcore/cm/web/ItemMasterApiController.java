package com.rootcore.cm.web;

import java.util.List;

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

    /** 신규 등록 */
    @PostMapping("/save")
    public void save(@RequestBody SkuVO skuVO) {
        itemMasterService.insertSku(skuVO);
    }

    /** SKU 자동생성 */
    @PostMapping("/generate-sku")
    public String generateSku(@RequestBody SkuVO cond) {
        return itemMasterService.generateSku(
                cond.getCompanyCode(),
                cond.getSkuType()
        );
    }
}
