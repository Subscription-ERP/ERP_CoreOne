package com.rootcore.sd.vo;

import lombok.Data;

import java.util.List;

@Data
public class InOrdSave {
    private InOrdVO info;
    private List<InOrdDetailVO> detail;
    private InOrdVO inordNo;
}
