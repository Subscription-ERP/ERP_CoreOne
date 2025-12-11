package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class CmPageController {

    // 회사 등록 페이지
    @GetMapping("/cm/company")
    public String company() {
        return "cm/company";
    }

    // 품번/상품관리
    @GetMapping("/cm/item")
    public String item() {
        return "cm/item";
    }

    // 부서코드 관리
    @GetMapping("/deptCodeManage/page")
    public String deptCodeManagePage() {
            return "cm/deptCodeManage";
    }

}
