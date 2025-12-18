package com.rootcore.cm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cm")
public class ItemMasterController {

    @GetMapping("/item-master")
    public String itemMaster() {
        return "cm/item_master";
    }
}

