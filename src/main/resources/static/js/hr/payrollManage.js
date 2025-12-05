/**
 * payrollManage.js
 */

// 숫자 한국형 포맷팅 함수
function formatKoreanNumber(value) {
	// Number()를 사용하여 value가 문자열인 경우에도 숫자로 변환을 시도합니다.
	return new Intl.NumberFormat('ko-KR').format(Number(value));
}

// 상세수당총액, 상세공제총액 값 변수 초기화
function resetSummaryTables() {
	// 수당 항목 초기화
	document.getElementById('overtime').textContent = 0;
	document.getElementById('night').textContent = 0;
	document.getElementById('holiday').textContent = 0;
	document.getElementById('family').textContent = 0;
	document.getElementById('meal').textContent = 0;
	document.getElementById('annual_leave').textContent = 0;
	document.getElementById('total_allowance').textContent = 0;

	// 공제 항목 초기화
	// document.getElementById('#').textContent = 0;
	document.getElementById('national_pension').textContent = 0;
	document.getElementById('employment_insurance').textContent = 0;
	document.getElementById('health_insurance').textContent = 0;
	document.getElementById('long_time_care_insurance').textContent = 0;
	// document.getElementById('#').textContent = 0;
	document.getElementById('total_deduction_amount').textContent = 0;
}

// 상세수당총액, 상세공제총액 값 테이블에 업데이트
function updateRowDetailSummary(rowData) {
	// ------------------ 수당 테이블 업데이트 ------------------
	// 서버 응답 VO 필드명: overtime, night, holiday, family, meal, annual_leave, total_allowance
	document.getElementById('overtime').textContent = rowData.overtime === null || rowData.overtime === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.overtime);
	document.getElementById('night').textContent = rowData.night === null || rowData.night === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.night);
	document.getElementById('holiday').textContent = rowData.holiday === null || rowData.holiday === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.holiday);
	document.getElementById('family').textContent = rowData.family === null || rowData.family === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.family);
	document.getElementById('meal').textContent = rowData.meal === null || rowData.meal === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.meal);
	document.getElementById('annual_leave').textContent = rowData.annual_leave === null || rowData.annual_leave === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.annualLeave);
	document.getElementById('total_allowance').textContent = rowData.total_allowance === null || rowData.total_allowance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.totalAllowance);

	// ------------------ 공제 테이블 업데이트 ------------------
	// 서버 응답 VO 필드명: national_pension, employment_insurance, health_insurance, long_time_care_insurance, total_deduction_amount
	// document.getElementById('sumIncomeTax').textContent = rawValue(rowData.income_tax || 0);  
	// document.getElementById('sumLocalIncomeTax').textContent = rawValue(rowData.local_income_tax || 0);  
	document.getElementById('national_pension').textContent = rowData.nationalPension === null || rowData.nationalPension === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.nationalPension);
	document.getElementById('employment_insurance').textContent = rowData.employmentInsurance === null || rowData.employmentInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.employmentInsurance);
	document.getElementById('health_insurance').textContent = rowData.healthInsurance === null || rowData.healthInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.healthInsurance);
	document.getElementById('long_time_care_insurance').textContent = rowData.longTimeCareInsurance === null || rowData.longTimeCareInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.longTimeCareInsurance);
	document.getElementById('total_deduction_amount').textContent = rowData.totalDeductionAmount === null || rowData.totalDeductionAmount === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.totalDeductionAmount);
}

document.addEventListener("DOMContentLoaded", () => {
	// 부서 조회(공통코드)
	getDeptOptions2(["#dept"]);

	/* =========
	 * 사원급여조회
	 * ========= */

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
		rowHeaders: ["checkbox"],
		columns: [
			{ header: "사번", name: "userId", align: "center" },
			{ header: "성명", name: "userName" },
			{ header: "부서명", name: "deptName" },
			{ header: "귀속연월", name: "payPeriod", align: "center" },
			{ header: "지급일", name: "payrollDate", align: "center" },
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
				header: "수당총액", name: "totalAllowance", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "총 지급액", name: "totalPaymentAmount", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "공제 총액", name: "totalDeductionAmount", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
			{
				header: "실 수령액", name: "netPay", align: "right", formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			}
		],
	}); // end of payrollDetailGrid

	/* =============================================
	 * Tui Grid 행 클릭 이벤트: 선택된 행의 상세 정보를 표시
	 * ============================================= */

	payrollDetailGrid.on('click', (ev) => {
		// 클릭된 행의 rowKey를 가져옵니다.
		const rowKey = ev.rowKey;
		if (rowKey !== undefined && rowKey !== null) {
			// 해당 rowKey에 해당하는 데이터를 가져옵니다.
			const rowData = payrollDetailGrid.getRow(rowKey);
			if (rowData) {
				console.log("선택된 행 데이터:", rowData);
				// 상세 정보를 Summary 테이블에 업데이트
				updateRowDetailSummary(rowData);
			}
		} else {
			// 그리드 헤더 등을 클릭했을 경우
			resetSummaryTables();
		}
	}); // end of payrollDetailGrid

	/* ========================
	 * 급여관리-조건조회-사원급여관리
	 * ======================== */

	document.querySelector("#btnPayrollManageSearch").addEventListener("click", function() {
		const dept = document.querySelector("#dept").value; // 부서명
		const userName = document.querySelector("#userName").value; // 성명
		const payPeriodStart = document.querySelector("#payPeriodStart").value; // 귀속연월 시작
		const payPeriodEnd = document.querySelector("#payPeriodEnd").value; // 귀속연월 종료

		// 조건 조회 할때 필요한 데이터들
		const data = {
			dept: dept,
			userName: userName,
			payPeriodStart: payPeriodStart,
			payPeriodEnd: payPeriodEnd,
		};

		// 조건 조회 실행
		payrollDetailGrid.readData(1, data, true);
	});

	/*==========
	 * 초기화 버튼
	 * ========= */

	document.querySelector('#btnPayrollManageReset').addEventListener('click', function() {
		document.querySelector("#dept").value = '';
		document.querySelector("#userName").value = '';
		document.querySelector("#payPeriodStart").value = '';
		document.querySelector("#payPeriodEnd").value = '';
	})

	/* ------------------------------------------------------------------
	 * 급여명세서(인쇄) 모달
	 * ------------------------------------------------------------------ */

	const printPreviewModal = document.getElementById('printPreviewModal'); // 인쇄 모달 가장 큰 틀
	const btnPrint = document.getElementById('btnPrint'); // 인쇄 버튼 태그
	const btnPrintPreviewClose = document.getElementById('btnPrintPreviewClose'); // 모달창에서 창 닫는 버튼 태그
	const btnPrintPreviewConfirm = document.getElementById('btnPrintPreviewConfirm'); // 모달창에서 확인버튼 태그

	const btnPrintPrev = document.getElementById('btnPrintPrev'); // 하단에 페이지 이동 버튼, 이전
	const btnPrintNext = document.getElementById('btnPrintNext'); // 하단에 페이지 이동 버튼, 이후
	const printPageCurrent = document.getElementById('printPageCurrent'); // 하단에 현재 페이지
	const printPageTotal = document.getElementById('printPageTotal'); // 하단에 전체 페이지 수
	const printPreviewPage = document.getElementById('printPreviewPage'); // 실제 a4 html이 들어가는 태그

	// 선택된 사원 카드 미리보기용 배열
	let printPages = [];
	let printPageIndex = 0;

	// 인쇄 버튼 클릭 시
	btnPrint?.addEventListener('click', () => {
		// 1) 체크된 사원들 가져오기
		const checkedRows = payrollDetailGrid.getCheckedRows();
		console.log(checkedRows);

		if (!checkedRows || checkedRows.length === 0) {
			showToast('인쇄할 사원을 먼저 선택해 주세요.', 'warning');
			return;
		}

		// 2) 각 userPayManagementCode 로 PDF URL 만들기 → iframe HTML 생성
		printPages = checkedRows.map(row => {

			const userPayManagementCode = row.userPayManagementCode;
			const url = `/api/hr/payslip/preview?userPayManagementCode=${encodeURIComponent(userPayManagementCode)}`;

			/* ===================================================================================================
			 * iframe 태그
			 * <iframe> 태그는 웹 페이지 내에 독립적인 틀(inline frame)을 만들어 다른 문서를 삽입하여 탐색할 수 있게 하는 태그입니다.
			 * 예를 들어, 유튜브 동영상을 웹 페이지에 삽입하거나, 구글 지도를 삽입하는 데 사용할 수 있습니다.
			 * =================================================================================================== */
			return `
	      <iframe
	        src="${url}"
	        style="width:100%;height:100%;border:none;"
	      ></iframe>
	    `;
		});

		// 3) 첫 페이지로 세팅 후 모달 오픈
		printPageIndex = 0;
		renderPrintPage(); // 현재 페이지 렌더링
		printPreviewModal.removeAttribute('hidden'); // hidden태그를 지우고 모달창 띄우기
	});

	// 현재 페이지 렌더링
	function renderPrintPage() {
		const total = printPages.length;
		const html = printPages[printPageIndex];

		printPreviewPage.innerHTML = html;
		printPageCurrent.textContent = printPageIndex + 1;
		printPageTotal.textContent = total;

		btnPrintPrev.classList.toggle('is-disabled', printPageIndex === 0);
		btnPrintNext.classList.toggle('is-disabled', printPageIndex === total - 1);
	}

	// 모달창 닫기 함수
	function closePrintPreview() {
		printPreviewModal.setAttribute('hidden', '');
	}
	btnPrintPreviewClose.addEventListener('click', closePrintPreview);

	// 확인버튼 : 선택된 사원카드 PDF 모두 다운로드
	btnPrintPreviewConfirm.addEventListener('click', () => {
		// 현재 체크된 행 다시 조회
		const checkedRows = payrollDetailGrid.getCheckedRows();

		// 각 사원에 대해 a 태그를 만들어 클릭 -> 브라우저가 다운로드 처리
		checkedRows.forEach(row => {
			const userPayManagementCode = row.userPayManagementCode;
			const url = `/api/hr/payslip/download?userId=${encodeURIComponent(userPayManagementCode)}`;

			const a = document.createElement('a');
			a.href = url;
			a.download = `userCard-${userPayManagementCode}.pdf`;
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
		});

		// 다운로드 후 모달 닫기
		closePrintPreview();
	});

	// 페이지네이션
	btnPrintPrev?.addEventListener('click', () => {
		if (printPageIndex > 0) {
			printPageIndex--;
			renderPrintPage();
		}
	});

	btnPrintNext?.addEventListener('click', () => {
		if (printPageIndex < printPages.length - 1) {
			printPageIndex++;
			renderPrintPage();
		}
	});

});