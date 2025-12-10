package com.rootcore.sd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OutordVO {
    private String companyCode;
    private String outordNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date outordDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    private String dept;
    private String pic;
    private String custCode;
    private String custName;
    private int totalQty;
    private double totalSupplyPrice;
    private double totalSurtax;
    private double totalPrice;

    private String createdBy;           // 생성자
    private Date createDate;            // 생성일자
    private String updatedBy;           // 수정자
    private Date updateDate;            // 수정일자
}
