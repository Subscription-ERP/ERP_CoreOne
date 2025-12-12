/**
 * annualStatus.js
 */

/* ======
 * 전역변수
 * ====== */
const annualStatusUserName = document.querySelector('#annualStatusUserName'); // 성명
const dept = document.querySelector('#dept'); // 부서명
const annualStatusRemainingDaysStart = document.querySelector('#annualStatusRemainingDaysStart'); // 잔여연차 시작일 
const annualStatusRemainingDaysEnd = document.querySelector('#annualStatusRemainingDaysEnd'); // 잔여연차 종료일

/* ===========
 * 부서조회(공통)
 * =========== */
getDeptOptions2(["#dept"]);

/* ==================
 * 연차현황조회 초기화버튼
 * ================== */
function annualStatusBtnReset() {
	annualStatusUserName.value = '';
	dept.value = '';
	annualStatusRemainingDaysStart.value = '';
	annualStatusRemainingDaysEnd.value = '';
}

/* ================
 * 연차현황조회 조회버튼
 * ================ */
function annualStatusBtnSearch() {
	const annualStatusUserNameValue = annualStatusUserName.value;
	const deptValue = dept.value;
	const annualStatusRemainingDaysStartValue = Number(annualStatusRemainingDaysStart.value);
	const annualStatusRemainingDaysEndValue = Number(annualStatusRemainingDaysEnd.value);

	const data = {
		userName: annualStatusUserNameValue,
		deptCode: deptValue,
		remainingDaysStart: annualStatusRemainingDaysStartValue,
		remainingDaysEnd: annualStatusRemainingDaysEndValue
	}

	annualStatusGrid.readData(1, data, true);
}

/* ==================
 * 회사내사원들연차현황조회
 * ================== */
const annualStatusGrid = new tui.Grid({
	el: document.getElementById("annualStatusGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/annualStatusList",
				method: "GET",
			},
		},
	},
	bodyHeight: 570,
	columns: [
		{ header: "사번", name: "userId", align: "center", sortable: true },
		{ header: "성명", name: "userName", sortable: true },
		{ header: "부서명", name: "deptName", sortable: true },
		{ header: "직급", name: "jobTitle", sortable: true },
		{ header: "부여연도", name: "grantYear", align: "center", sortable: true },
		{ header: "발생연차", name: "totalGrantDays", align: "right", sortable: true },
		{ header: "사용일수", name: "totalUsedDays", align: "right", sortable: true },
		{ header: "잔여연차", name: "remainingDays", align: "right", sortable: true },
		{ header: "소멸 예정일", name: "expiryDate", align: "center", sortable: true },
	],
}); // end of payrollDetailGrid

document.addEventListener('DOMContentLoaded', () => {
	/* ================
	 * 연차현황조회 조회버튼
	 * ================ */
	document.querySelector('#annualStatusBtnSearch').addEventListener('click', annualStatusBtnSearch);
	
	/* ==================
	 * 연차현황조회 초기화버튼
	 * ================== */
	document.querySelector('#annualStatusBtnReset').addEventListener('click', annualStatusBtnReset);
})