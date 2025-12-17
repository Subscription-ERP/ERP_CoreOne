/**
 * 급여대장 관리 페이지 자바스크립트
 * @file payRoll.js
 * @description 매월 주기적으로 지급되는 급여 및 보너스 상여금을 등록하여 조회하고 계산, 확정하여 월급 및 상여금을 지급 할 수 있습니다.
 * @author 장준현
 */

/* ====================
 * 전역 변수
 * 현재 선택된 급여대상 코드 
 * ==================== */
let currentPayrollPeriodCode = null;

/* 인원수 반영 (대상 그리드의 전체 사원 수) */
const bonusPeopleNumberInput = document.querySelector("#bonusPeopleNumber");
function updatePeopleNumber() {
	const totalTargetRows = targetUserGrid.getData().length;
	bonusPeopleNumberInput.value = totalTargetRows;
}

// =========================================================================
// Grid 인스턴스: 2개로 분리 (userGrid -> allUserGrid, targetUserGrid)
// =========================================================================
const commonColumns = [
	{
		header: "사원번호",
		name: "userId",
		align: "center",
		width: 150, // 너비 축소
		sortable: true,
	},
	{
		header: "성명",
		name: "userName",
		width: 50, // 너비 축소
		sortable: true,
	},
	{
		header: "부서명",
		name: "dept",
		width: 100, // 너비 축소
		sortable: true,
	},
	// { header: '입사일', name: 'hireDate', align: 'center', sortable: true }, // 임시제거
	// { header: '직위', name: 'jobTitle', sortable: true } // 임시제거
];

/* 우측 그리드: 상여 등록 대상 목록 (저장 대상) */
const targetUserGrid = new tui.Grid({
	el: document.getElementById("targetUserGrid"),
	scrollX: false,
	scrollY: true,
	rowHeaders: ["checkbox"], // 제거를 위해 체크박스 유지
	data: [], // 초기 데이터는 빈 배열
	bodyHeight: 200,
	rowKey: "userId",
	columns: commonColumns,
});

// 숫자 한국형 포맷팅 함수
function formatKoreanNumber(value) {
	// Number()를 사용하여 value가 문자열인 경우에도 숫자로 변환을 시도합니다.
	return new Intl.NumberFormat('ko-KR').format(Number(value));
}

// 공통코드(선택박스)
function getCmCodeOptionsPayRoll(divId) {
	const keys = Object.keys(divId);
	const param = keys.map((k) => `code=${k}`).join("&");

	fetch(`/api/com/commonCodes?${param}`)
		.then((res) => res.json())
		.then((list) => {
			for (item in divId) {
				const select = document.querySelector(`#${divId[item]}`);

				if (select) {
					/* select박스에 전체 선택option 넣기 */
					const optDefault = document.createElement("option"); // 태그 생성
					optDefault.textContent = "전체"; // 화면에 보이는 글자
					optDefault.value = ""; // 실제 값
					select.appendChild(optDefault); // select박스에 추가

					// 요소가 존재할 때만 실행
					list[item].forEach((d) => {
						const opt = document.createElement("option");
						opt.value = d.code;
						opt.textContent = d.codeName;
						select.appendChild(opt);
					});
				}
			}
		})
		.catch((err) => console.error(err));
}

/* 공통코드 radio버튼 */
function getCmCodeRadio(divIdRadio) {
	const keys = Object.keys(divIdRadio);
	const param = keys.map((k) => `code=${k}`).join("&");

	fetch(`/api/com/commonCodes?${param}`)
		.then((res) => res.json())
		.then((list) => {
			for (item in divIdRadio) {
				const container = document.getElementById(divIdRadio[item]);

				if (container && list[item] && list[item].length > 0) {
					const radioName = "bonusType"; // 라디오 버튼 name을 'bonusType'으로 고정
					container.innerHTML = ""; // 컨테이너 초기화

					list[item].forEach((d, index) => {
						const input = document.createElement("input");
						input.className = "form-check-input";
						input.type = "radio";
						input.id = `${radioName}_${d.code}`;
						input.name = radioName;
						input.value = d.code;

						if (index === 0) {
							input.checked = true;
						}

						const label = document.createElement("label");
						label.htmlFor = input.id;
						label.textContent = d.codeName;
						label.className = "form-check-label";

						container.appendChild(input);
						container.appendChild(label);
					});
				} else if (!container) {
					console.error(
						`Error: Could not find container element with ID: ${divIdRadio[item]} for code ${item}`
					);
				}
			}
		})
		.catch((err) => console.error(err));
}

/* ======================================
 * 급여관리 모달창
 * 사원별 급여 상세 Grid
 * ====================================== */
const payrollDetailGrid = new tui.Grid({
	el: document.getElementById("payrollDetailGrid"),
	scrollX: true,
	scrollY: true,
	data: [], // 초기 데이터는 비어있음
	bodyHeight: 200, // HTML에서 설정한 높이와 일치시킵니다.
	rowkey: "payrollPeriodCode",
	columns: [
		// { header: "귀속연월", name: "payrollPeriod", align: "center", width: 80 },
		{ header: "사번", name: "userId", align: "center", width: 150 },
		{ header: "성명", name: "userName", width: 10 },
		{ header: "부서명", name: "deptName", width: 100 },
		{ header: "지급일", name: "payrollDate", align: "center", width: 100 },
		{
			header: "기본급", name: "salary", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		},
		{
			header: "상여금", name: "bonus", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		},
		{
			header: "수당총액", name: "totalAllowance", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		},
		{
			header: "총 지급액", name: "totalPaymentAmount", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		},
		{
			header: "공제 총액", name: "totalDeductionAmount", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		},
		{
			header: "실 수령액", name: "netPay", align: "right", width: 100, formatter: function(e) {
				return formatKoreanNumber(e.value);
			}
		}
	],
}); // end of payrollDetailGrid

/* 상세수당총액, 상세공제총액 값 변수 초기화 */
function resetSummaryTables() {
	// 수당 항목 초기화
	document.getElementById('overtime').textContent = 0;
	document.getElementById('night').textContent = 0;
	document.getElementById('holiday').textContent = 0;
	document.getElementById('family').textContent = 0;
	document.getElementById('meal').textContent = 0;
	document.getElementById('total_allowance').textContent = 0;

	// 공제 항목 초기화
	document.getElementById('income_tax').textContent = 0;
	document.getElementById('national_pension').textContent = 0;
	document.getElementById('employment_insurance').textContent = 0;
	document.getElementById('health_insurance').textContent = 0;
	document.getElementById('long_time_care_insurance').textContent = 0;
	document.getElementById('local_income_tax').textContent = 0;
	document.getElementById('absence').textContent = 0;
	document.getElementById('total_deduction_amount').textContent = 0;
}

/* 상세수당총액, 상세공제총액 값 테이블에 업데이트 */
function updateRowDetailSummary(rowData) {
	// ------------------ 수당 테이블 업데이트 ------------------
	document.getElementById('overtime').textContent = rowData.overtime === null || rowData.overtime === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.overtime);
	document.getElementById('night').textContent = rowData.night === null || rowData.night === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.night);
	document.getElementById('holiday').textContent = rowData.holiday === null || rowData.holiday === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.holiday);
	document.getElementById('family').textContent = rowData.family === null || rowData.family === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.family);
	document.getElementById('meal').textContent = rowData.meal === null || rowData.meal === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.meal);
	document.getElementById('total_allowance').textContent = rowData.totalAllowance === null || rowData.totalAllowance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.totalAllowance);

	// ------------------ 공제 테이블 업데이트 ------------------
	document.getElementById('income_tax').textContent = rowData.incomeTax === null || rowData.incomeTax === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.incomeTax);
	document.getElementById('local_income_tax').textContent = rowData.localIncomeTax === null || rowData.localIncomeTax === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.localIncomeTax);
	document.getElementById('national_pension').textContent = rowData.nationalPension === null || rowData.nationalPension === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.nationalPension);
	document.getElementById('employment_insurance').textContent = rowData.employmentInsurance === null || rowData.employmentInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.employmentInsurance);
	document.getElementById('health_insurance').textContent = rowData.healthInsurance === null || rowData.healthInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.healthInsurance);
	document.getElementById('long_time_care_insurance').textContent = rowData.longTimeCareInsurance === null || rowData.longTimeCareInsurance === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.longTimeCareInsurance);
	document.getElementById('absence').textContent = rowData.absence === null || rowData.absence === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.absence);
	document.getElementById('total_deduction_amount').textContent = rowData.totalDeductionAmount === null || rowData.totalDeductionAmount === undefined ? 0 : new Intl.NumberFormat('ko-KR').format(rowData.totalDeductionAmount);
}

/**========================================================================
 * 상여등록 - 초기화 버튼 기능
 * @description 상여등록 버튼을 누르면 해당 폼에 있는 input박스에 있는 text들이 지워진다.
 * @returns input박스 초기화
 * ======================================================================== */
function resetBonusRegisterForm() {
	document.querySelector("#payrollPeriod").value = ""; // 귀속연월
	document.querySelector('#bonusType_k1').checked = true; // 지급액(amount) 버튼 초기화 추가
	document.querySelector('#bonusType_k2').checked = false; // 상여지급방법 라디오버튼 초기화
	document.querySelector("#bonus").value = ""; // 지급률 및 지급액
	document.querySelector("#payrollBonusName").value = "";
	document.querySelector("#payrollStartDate").value = "";
	document.querySelector("#payrollEndDate").value = "";
	document.querySelector("#payrollBonusDate").value = "";
	targetUserGrid.clear(); // 대상 사원 목록 초기화
	updatePeopleNumber(); // 인원수 초기화
}

/**====================================
 * 제목 : 모달창 닫기
 * @description 모달창 닫기위해 실행하는 함수
 * @returns 모달닫기, 그리드 초기화
 * @author 장준현
 * ==================================== */
function closePayrollManageModal() {
	const payrollManageModal = document.querySelector('#payrollManageModal');
	if (payrollManageModal) {
		payrollManageModal.hidden = true;
	}
	const totalBreakdown = document.querySelector('#totalBreakdownWrapper'); // element가져오기
	totalBreakdown.removeAttribute('hidden');
	payrollDetailGrid.resetData([]);
	resetSummaryTables();
}

document.addEventListener("DOMContentLoaded", () => {
	// 급여구분 
	const divId = { "0J": "payrollType" };
	getCmCodeOptionsPayRoll(divId);

	// 상여지급방법 (라디오 버튼)
	const divIdRadio = { "0K": "bonusTypeContainer" };
	getCmCodeRadio(divIdRadio);

	// 부서 조회(공통코드)
	getDeptOptions2(["#dept"]);

	// 급여대장-상여등록-초기화버튼
	document.querySelector("#btnReset").addEventListener("click", function() {
		resetBonusRegisterForm(); // 초기화 함수 호출
	});

	/* 급여대장-대장조회-초기화버튼 */
	document
		.querySelector("#btnPayrollReset")
		.addEventListener("click", function() {
			document.querySelector("#payrollType").value = "";
			document.querySelector("#payrollName").value = "";
			document.querySelector("#paymentStartDate").value = "";
			document.querySelector("#paymentEndDate").value = "";
		});

	/* 급여대장-대장조회버튼 */
	document
		.querySelector("#btnPayrollSearch")
		.addEventListener("click", function() {
			const payrollType = document.querySelector("#payrollType").value; // 급여구분
			const payrollName = document.querySelector("#payrollName").value; // 대장명칭
			const paymentStartDate =
				document.querySelector("#paymentStartDate").value; // 지급일 시작
			const paymentEndDate = document.querySelector("#paymentEndDate").value; // 지급일 종료

			const data = {
				payrollType: payrollType,
				payrollName: payrollName,
				paymentStartDate: paymentStartDate,
				paymentEndDate: paymentEndDate,
			};

			// 조건 조회 실행
			payrollGrid.readData(1, data, true);
		});

	/* 급여대장-상여등록-사원조회 초기화버튼 기능 */
	document.querySelector("#btnEmpReset").addEventListener("click", function() {
		document.querySelector("#payrollUserName").value = "";
		document.querySelector("#dept").value = ""; // 부서 select 초기화
	});

	/* 1. 좌측 그리드: 전체 사원 목록 조회 (검색 적용) */
	const allUserGrid = new tui.Grid({
		el: document.getElementById("allUserGrid"),
		scrollX: false,
		scrollY: true,
		rowHeaders: ["checkbox"],
		data: {
			api: {
				readData: {
					url: "/api/hr/payrollEmpList",
					method: "GET",
				},
			},
		},
		bodyHeight: 200,
		rowKey: "userId", // 사원번호를 rowKey로 사용
		columns: commonColumns,
	});

	// =========================================================================
	// 데이터 이동 로직
	// =========================================================================

	/* + 버튼 (전체 사원 -> 대상 사원) */
	document
		.getElementById("btnAddSelected")
		.addEventListener("click", function() {
			const selectedRows = allUserGrid.getCheckedRows();

			if (selectedRows.length === 0) {
				showToast("추가할 사원을 선택해주세요.", 'info');
				return;
			}

			// 우측 그리드에 이미 존재하는 사원 ID 목록
			const existingUserIds = targetUserGrid.getData().map((row) => row.userId);

			// 중복되지 않은 새로운 행만 필터링
			const newRowsToAdd = selectedRows.filter(
				(row) => !existingUserIds.includes(row.userId)
			);

			if (newRowsToAdd.length === 0) {
				showToast("선택된 사원 중 추가할 수 있는 사원이 없습니다.", 'info');
				allUserGrid.uncheckAll(); // 체크 해제
				return;
			}

			// 대상 그리드에 데이터 추가
			targetUserGrid.appendRows(newRowsToAdd);

			// 좌측 그리드에서 체크 해제
			allUserGrid.uncheckAll();

			// 인원수 업데이트
			updatePeopleNumber();
		});

	/* - 버튼 (대상 사원 -> 목록에서 제거) */
	document
		.getElementById("btnRemoveSelected")
		.addEventListener("click", function() {
			// 우측 그리드에서 체크된 행의 rowKey를 가져옵니다.
			const checkedTargetRows = targetUserGrid.getCheckedRowKeys();

			if (checkedTargetRows.length === 0) {
				showToast("제외할 대상 사원을 선택해주세요.", 'info');
				return;
			}

			// 대상 그리드에서 해당 행들을 삭제합니다.
			targetUserGrid.removeRows(checkedTargetRows);

			// 인원수 업데이트
			updatePeopleNumber();
		});

	/* 급여대장-상여등록-사원조회-조회버튼 */
	document
		.getElementById("btnPayrollEmpSearch")
		.addEventListener("click", function() {
			// 성명
			const payrollUserName = document.querySelector("#payrollUserName").value;
			// 부서명 (select 태그이므로 #dept 사용)
			const payrollDeptName = document.querySelector("#dept").value;
			// 입사일 범위 시작일 (임시제거)
			// const payrollEmpStartDate = document.querySelector('#payrollEmpStartDate').value;
			// 입사일 범위 종료일 (임시제거)
			// const payrollEmpEndDate = document.querySelector('#payrollEmpEndDate').value;

			const data = {
				userName: payrollUserName,
				dept: payrollDeptName,
				// payrollEmpStartDate: payrollEmpStartDate, // (임시제거)
				// payrollEmpEndDate: payrollEmpEndDate // (임시제거)
			};

			// 조건 조회 실행을 한다 (좌측 그리드에만 적용)
			allUserGrid.readData(1, data, true);
		});

	/* ======================================
	 * 급여대장-상여등록
	 * 설명 : 저장버튼을 누르면 상여등록이 이루어진다.
	 * ====================================== */
	document.getElementById("btnSave").addEventListener("click", () => {
		// 대상 그리드(targetUserGrid)의 모든 사원을 가져옴
		const checkedEmployees = targetUserGrid.getData();
		const employeeIds = checkedEmployees.map((row) => row.userId);

		if (employeeIds.length === 0) {
			showToast("상여 등록 대상 사원을 한 명 이상 추가해야 합니다.", 'info');
			return;
		}

		// 귀속연월
		const payrollPeriod = document.getElementById("payrollPeriod").value;
		// 상여지급방법
		// 주의: 라디오 버튼의 name은 'bonusType'으로 고정
		const bonusType = document.querySelector(
			'input[name="bonusType"]:checked'
		).value;
		// 지급률 및 지급액
		const bonusValue = document.querySelector("#bonus").value;
		// 대장명칭
		const payrollBonusName = document.querySelector("#payrollBonusName").value;
		// 대장기간시작일
		const payrollStartDate = document.querySelector("#payrollStartDate").value;
		// 대장기간종료일
		const payrollEndDate = document.querySelector("#payrollEndDate").value;
		// 지급일
		const payrollBonusDate = document.querySelector("#payrollBonusDate").value;

		// 서버전송할때 보낼 데이터 객체 생성
		const data = {
			payrollPeriod: payrollPeriod,
			bonusMethod: bonusType,
			bonusRate: bonusType === "k1" ? bonusValue : null,
			bonusAmount: bonusType === "k2" ? bonusValue : null,
			payrollName: payrollBonusName,
			payrollStartDate: payrollStartDate,
			payrollEndDate: payrollEndDate,
			payrollDate: payrollBonusDate,
			peopleNumber: employeeIds.length,
			employeeIds: employeeIds,
		};

		/* ======
		 * 상여등록
		 * ====== */
		fetch("/api/hr/bonusRegister", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(data),
		})
			.then((response) => response.json())
			.then((result) => {
				if (result.success) {
					showToast(result.count + "건의 상여 등록이 완료되었습니다.", 'success');
					targetUserGrid.resetData([]); // 성공 시 대상 목록 초기화
					updatePeopleNumber();
					resetBonusRegisterForm(); // 상여등록 폼 초기화
					payrollGrid.reloadData(); // 상여등록 성공하면 급여대장목록 한번 리로드되게하기
				} else {
					showToast("등록 실패 : " + result.message, 'error');
				}
			})
			.catch((error) => console.error("Error:", error));
	});

	// 대상 그리드에서 행이 추가/제거될 때마다 인원수 업데이트
	targetUserGrid.on("afterRemoveRow", updatePeopleNumber);
	targetUserGrid.on("afterAppendRow", updatePeopleNumber);

	/* 급여대장-급여대장목록조회, 급여대장목록 */
	const payrollGrid = new tui.Grid({
		el: document.getElementById("payrollGrid"),
		scrollX: true,
		scrollY: true,
		data: {
			api: {
				readData: {
					url: "/api/hr/payrollList",
					method: "GET",
				},
			},
		},
		rowKey: "payrollPeriodCode",
		bodyHeight: 570,
		columns: [
			{ header: "귀속연월", name: "payrollPeriod", align: "center", width: 80, sortable: true, },
			{ header: "급여구분", name: "payrollType", width: 70, sortable: true, },
			{ header: "대장명칭", name: "payrollName", width: 200, sortable: true, },
			{ header: "지급일", name: "payrollDate", align: "center", sortable: true, },
			{ header: "인원수", name: "peopleNumber", width: 70, align: "right", sortable: true, },
			{
				header: "급여계산",
				name: "salaryCalculation",
				align: "center",
				formatter: function(data) {
					const payrollPeriodCode = data.row.payrollPeriodCode;
					return `<a class="btn-calculate" href=# data-payroll-period-code="${payrollPeriodCode}">계산하기</a>`;
				},
			},
			{
				header: "지급총액", name: "totalPayment", align: "right", sortable: true, formatter: function(e) {
					return formatKoreanNumber(e.value);
				}
			},
		],
	}); // end of payrollGrid

	/**=============================
	 * 계산하기 클릭했을때 모달창이 나타나게
	 * ============================= */
	payrollGrid.on('click', (ev) => {
		// ev 객체에서 필요한 정보를 바로 추출합니다.
		const { rowKey, nativeEvent } = ev;
		const targetElement = nativeEvent.target; // 실제 클릭된 HTML 요소

		// 행 데이터 가져오기
		const targetRowkey = payrollGrid.getRow(rowKey);
		if (!targetRowkey) return; // 클릭한 곳에 데이터가 없으면 종료

		// 상여일 경우 레이아웃 처리
		if (targetRowkey.payrollType === "상여") {
			const totalBreakdown = document.querySelector('#totalBreakdownWrapper');
			if (totalBreakdown) totalBreakdown.setAttribute('hidden', '');
		} else {
			const totalBreakdown = document.querySelector('#totalBreakdownWrapper');
			if (totalBreakdown) totalBreakdown.removeAttribute('hidden');
		}

		// 클릭된 요소가 계산 버튼('btn-calculate')인지 확인
		if (targetElement.classList.contains('btn-calculate')) {
			nativeEvent.preventDefault();

			const payrollPeriodCode = targetElement.dataset.payrollPeriodCode;
			currentPayrollPeriodCode = payrollPeriodCode;

			// 모달 열기 및 그리드 새로고침
			const payrollManageModal = document.querySelector('#payrollManageModal');
			if (payrollManageModal) {
				payrollManageModal.hidden = false;
			}
			payrollDetailGrid.refreshLayout();

			// API 호출
			fetch(`/api/hr/UserPayList?payroll_period_code=${payrollPeriodCode}`)
				.then(res => res.ok ? res.json() : Promise.reject(res))
				.then(responseMap => {
					console.log("[급여계산 전체 응답]", responseMap);
					if (responseMap.result && responseMap.data?.contents) {
						const payList = responseMap.data.contents;
						console.log("[추출된 급여 목록]", payList);
						payrollDetailGrid.resetData(payList);
						if (payList.length === 0) showToast("데이터가 없습니다.", 'info');
					} else {
						showToast("실패: " + (responseMap.message || "오류"), 'error');
						payrollDetailGrid.resetData([]);
					}
				})
				.catch(error => {
					console.error("Error:", error);
					showToast("오류가 발생했습니다.", 'error');
				});
		}
	}); // end of payrollGrid 클릭 이벤트

	/**===============================
	 * 급여관리 모달창에서 x버튼 누르면 초기화
	 * =============================== */
	document.querySelector('#btnPayrollManageClose').addEventListener('click', closePayrollManageModal);

	/* ============================================
	 * Tui Grid 행 클릭 이벤트: 선택된 행의 상세 정보를 표시
	 * ============================================ */
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

	/* =====================================
	 * 확정 버튼
	 * 급여대장-급여관리 모달창-확정버튼
	 * 확정 버튼을 눌렀을때 사원급여관리 테이블에 삽입
	 * 확정버튼 누르면 지급총액도 같이 나오게 만듬
	 * ===================================== */
	document.querySelector('#btnPayrollSave').addEventListener('click', function() {
		// 급여관리 테이블 데이터 가져오기
		const data = payrollDetailGrid.getData();
		// console.log("data1:",data);
		// console.log("data[0].payroll_code:", data[0].payrollCode);
		// 지금 화면 데이터가 tb_user_pay_management에 있는 데이터인지 확인
		fetch(`/api/hr/checkUserPay?payroll_code=${data[0].payrollCode}`)
			.then(res => res.text())
			.then(responsetext => {
				if (parseInt(responsetext, 10) > 0) { // parseInt해서 10진수로 교체하고나서 확인하는거임
					showToast("이미 확정된 급여대장입니다.", 'warning');
					// console.log("data2:",data);
					return;
				} else {
					// console.log("data3:",data);
					// ajax로 데이터 전송
					fetch("/api/hr/registerUserPay", {
						method: "POST",
						headers: {
							"Content-Type": "application/json",
						},
						body: JSON.stringify(data),
					})
						.then((response) => response.text())
						.then((textresult) => {
							// 텍스트를 정수로 변환
							const successCount = parseInt(textresult, 10);

							// 성공했을때
							if (successCount > 0) {
								showToast("건의 등록이 확정되었습니다.", 'success');
								// 성공하고나서 급여대장 그리드 새로고침
								payrollGrid.reloadData();
								closePayrollManageModal();
							} else {
								showToast("등록 실패 : " + textresult.message, 'error');
								console.log(textresult.message);
							}
						})
						.catch((error) => console.error("Error:", error));
				}
			})

	});

	// 날짜 input박스만 눌러도 달력 선택창 뜨게 하기
	setupNativeDatePicker('payrollPeriodParent', 'payrollPeriod'); // 급여대장-상여등록-귀속연월
	setupNativeDatePicker('payrollStartDateWrapper', 'payrollStartDate'); // 급여대장-상여등록-대장기간시작일
	setupNativeDatePicker('payrollEndDateWrapper', 'payrollEndDate'); // 급여대장-상여등록-대장기간종료일
	setupNativeDatePicker('payrollBonusDateWrapper', 'payrollBonusDate'); // 급여대장-상여등록-지급일
	setupNativeDatePicker('paymentStartDateWrapper', 'paymentStartDate'); // 급여대장-대장조회-지급일시작
	setupNativeDatePicker('paymentEndDateWrapper', 'paymentEndDate'); // 급여대장-대장조회-지급일종료


}); // end of DOMContentLoaded
