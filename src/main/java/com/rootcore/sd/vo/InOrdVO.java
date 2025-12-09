package com.rootcore.sd.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class InOrdVO {
    private String companyCode;         // 회사코드
    private String inordNo;             // 수주번호

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date inordDate;             // 수주일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;               // 납기일

    private String dept;                // 부서
    private String pic;                 // 담당자
    private String custCode;            // 거래처코드
    private String custName;            // 거래처명
    private double totalSupplyPrice;    // 총공급가액
    private double totalSurtax;         // 총부가세
    private double totalPrice;          // 총액
    private String outputStatus;        // 출고여부
    private String outputStatusName;    // 출고여부 이름
    private String createdBy;           // 생성자
    private Date createDate;            // 생성일자
    private String updatedBy;           // 수정자
    private Date updateDate;            // 수정일자
}

