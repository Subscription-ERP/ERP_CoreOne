package com.rootcore.hr.vo;

import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**===============================
 *제목:급여조회VO
 *설명:급여조회하는데 필요한 데이터 VO정의
 *@author 장준현
 *================================*/
@Data
public class UserPayManageVO {
	private String userPayManagementCode; // 사원급여관리코드
	private String companyCode; // 회사코드
	private String payrollCode; // 급여대장코드
	private String payPeriod; // 귀속연월
	private String userId; // 사번
	private String userName; // 사원이름
	private Long incomeTax; // 소득세
	private Long localIncomeTax; // 지방소득세
	private String deptName; // 부서이름
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date payrollDate; // 지급일
	private Long salary; // 기본급
	private Long bonus; // 상여
	private Long overtime; // 총연장근무시간
	private Long night; // 총야간근무시간
	private Long holiday; // 총휴일근무시간
	private Long family; // 가족수
	private Long meal; // 식대
	private Long totalAllowance; // 총 수당총액(상여 미포함)
	private Long totalPaymentAmount; // 총 지급액(상여포함)
	private Long nationalPension; // 국민연금
	private Long employmentInsurance; // 고용보험
	private Long healthInsurance; // 건강보험
	private Long longTimeCareInsurance; // 장기요양보험
	private Long totalDeductionAmount; // 총 4대보험(소득세랑 지방소득세가 없어서 지금은 이게 공제 총액)
	private Long netPay; // 실 수령액
	private Long absence; // 결근, 병가 공제액
}
