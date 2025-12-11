package com.rootcore.hr.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HrPageController {

    @GetMapping("/hr/user")
    public String user() { return "hr/user"; }

    @GetMapping("/hr/attendance")
    public String attendance() { return "hr/attendance/attendanceManage"; }

    @GetMapping("/hr/my_attendance")
    public String myAttendance() { return "hr/attendance/attendanceMe"; }

    @GetMapping("/hr/eval/standard")
    public String evalStandard() { return "hr/review/reviewMaster"; }

    @GetMapping("/hr/eval/manage")
    public String evalManage() { return "hr/review/reviewManage"; }

    @GetMapping("/hr/cert")
    public String cert() { return "hr/cert/cert"; }

    @GetMapping("/hr/cert/user_modal")
    public String certUserModal() { return "hr/cert/user_modal"; }

    @GetMapping("/hr/payroll/print_modal")
    public String payrollPrintModal() { return "hr/payroll/print_modal"; }


}
