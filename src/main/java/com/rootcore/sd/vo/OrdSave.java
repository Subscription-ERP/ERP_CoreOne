package com.rootcore.sd.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrdSave {
    
    // 수주 저장
    private InOrdVO inordInfo;
    private List<InOrdDetailVO> inordDetail;
    
    // 발주 저장
    private OutordVO outordInfo;
    private List<OutordDetailVO> outordDetail;
}
