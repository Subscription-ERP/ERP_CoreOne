package com.rootcore.sd.vo;

import lombok.Data;

import java.util.List;

@Data
public class InOrdOutPut {
    private String inordNo;
    private List<InOrdDetailVO> details;
}
