package com.rootcore.sd.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OutordDetailVO {
    private String createdBy;           // 생성자
    private Date createDate;            // 생성일자
    private String updatedBy;           // 수정자
    private Date updateDate;            // 수정일자
}
