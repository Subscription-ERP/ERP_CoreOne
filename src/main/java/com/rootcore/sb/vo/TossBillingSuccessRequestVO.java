package com.rootcore.sb.vo;

import lombok.Data;

@Data
//successurl로 넘어올때 요청데이터 
public class TossBillingSuccessRequestVO {
	private String authKey;
	private String customerKey;
}
