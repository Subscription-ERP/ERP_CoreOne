/**
 * payrollManage.js
 */

// 숫자 한국형 포맷팅 함수
function formatKoreanNumber(value) {
	// Number()를 사용하여 value가 문자열인 경우에도 숫자로 변환을 시도합니다.
	return new Intl.NumberFormat('ko-KR').format(Number(value));
}
document.addEventListener("DOMContentLoaded", () => {
	// 사원급여조회
	const payrollDetailGrid = new tui.Grid({
		el: document.getElementById("payrollDetailGrid"),
		scrollX: true,
		scrollY: true,
		data: {
			api: {
				readData: {
					url: "/api/hr/payrollManageList",
					method: "GET",
				},
			},
		},
		bodyHeight: 300, // HTML에서 설정한 높이와 일치시킵니다.
		rowKey: "user_id",
		columns: [
			{ header: "사번", name: "user_id", align: "center" },
			{ header: "성명", name: "user_name" },
			{ header: "부서명", name: "dept_name" },
			{ header: "지급일", name: "payroll_date", align: "center" },
			{
				header: "기본급", name: "salary", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "상여금", name: "bonus", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "수당총액", name: "total_allowance", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "총 지급액", name: "total_payment_amount", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "공제 총액", name: "total_deduction_amount", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "실 수령액", name: "net_pay", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			}
		],
	}); // end of payrollDetailGrid

});