package com.rootcore.sd.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SdPageController {

    @GetMapping("/sd/customer")
    public String customer() { return "sd/cust"; }

    @GetMapping("/sd/customer/modal")
    public String customerModal() { return "sd/customer_modal"; }

    @GetMapping("/sd/order/reg")
    public String orderReg() { return "sd/inord"; }

    @GetMapping("/sd/order/item_modal")
    public String orderItemModal() { return "sd/item_modal"; }

    @GetMapping("/sd/purchase/reg")
    public String purchaseReg() { return "sd/outord"; }

    @GetMapping("/sd/order/complete")
    public String orderComplete() { return "sd/inordList"; }

    @GetMapping("/sd/purchase/complete")
    public String purchaseComplete() { return "sd/purchase_complete"; }

    @GetMapping("/sd/customer/bank_modal")
    public String bankModal() { return "sd/bank_modal"; }

    @GetMapping("/sd/customer/transfer_modal")
    public String transferModal() { return "sd/transfer_modal"; }

    @GetMapping("/sd/customer/ship_reg_modal")
    public String shipRegModal() { return "sd/ship_reg_modal"; }

    @GetMapping("/sd/customer/ship_mng_modal")
    public String shipMngModal() { return "sd/ship_mng_modal"; }
}
