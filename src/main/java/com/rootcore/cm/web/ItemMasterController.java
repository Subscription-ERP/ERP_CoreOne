package com.rootcore.cm.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cm")
public class ItemMasterController {

    @GetMapping("/item-master")
    public String itemMaster(Model model) {
    	model.addAttribute("breadcrumb", List.of(
    	        "시스템관리",
    	        "품번등록 및 조회"
    	    ));
        return "cm/item_master";
    }
}

