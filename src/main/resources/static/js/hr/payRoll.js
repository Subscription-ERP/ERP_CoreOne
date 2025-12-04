/**
 * payRoll.js
 */

let currentPayrollPeriodCode = null; 

/* 공통코드(선택박스) */
function getCmCodeOptions(divId) {
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
						console.log('d.code : ', d.code);
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

/* 부서 기준정보 코드 */
function getDeptOptions() {
	fetch("/api/hr/getDeptName")
		.then((res) => res.json())
		.then((list) => {
			const select = document.querySelector("#dept");

			if (select) {
				const optDefault = document.createElement("option");
				optDefault.textContent = "전체";
				optDefault.value = "";
				select.appendChild(optDefault);

				list.forEach((d) => {
					const opt = document.createElement("option");
					opt.value = d.deptCode;
					opt.textContent = d.deptName;
					select.appendChild(opt);
				});
			}
		})
		.catch((err) => console.error(err));
}

/* 상세수당총액, 상세공제총액 값 변수 초기화 */
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

/* 상세수당총액, 상세공제총액 값 테이블에 업데이트 */
function updateRowDetailSummary(rowData) {
	// ------------------ 수당 테이블 업데이트 ------------------
	// 서버 응답 VO 필드명: overtime, night, holiday, family, meal, annual_leave, total_allowance
	document.getElementById('overtime').textContent = rowData.overtime === null || rowData.overtime === undefined ? 0 : rowData.overtime;
	document.getElementById('night').textContent = rowData.night === null || rowData.night === undefined ? 0 : rowData.night;
	document.getElementById('holiday').textContent = rowData.holiday === null || rowData.holiday === undefined ? 0 : rowData.holiday;
	document.getElementById('family').textContent = rowData.family === null || rowData.family === undefined ? 0 : rowData.family;
	document.getElementById('meal').textContent = rowData.meal === null || rowData.meal === undefined ? 0 : rowData.meal;
	document.getElementById('annual_leave').textContent = rowData.annual_leave === null || rowData.annual_leave === undefined ? 0 : rowData.annual_leave;
	document.getElementById('total_allowance').textContent = rowData.total_allowance === null || rowData.total_allowance === undefined ? 0 : rowData.total_allowance;

	// ------------------ 공제 테이블 업데이트 ------------------
	// 서버 응답 VO 필드명: national_pension, employment_insurance, health_insurance, long_time_care_insurance, total_deduction_amount
	// document.getElementById('sumIncomeTax').textContent = rawValue(rowData.income_tax || 0);  
	// document.getElementById('sumLocalIncomeTax').textContent = rawValue(rowData.local_income_tax || 0);  
	document.getElementById('national_pension').textContent = rowData.national_pension === null || rowData.national_pension === undefined ? 0 : rowData.national_pension;
	document.getElementById('employment_insurance').textContent = rowData.employment_insurance === null || rowData.employment_insurance === undefined ? 0 : rowData.employment_insurance;
	document.getElementById('health_insurance').textContent = rowData.health_insurance === null || rowData.health_insurance === undefined ? 0 : rowData.health_insurance;
	document.getElementById('long_time_care_insurance').textContent = rowData.long_time_care_insurance === null || rowData.long_time_care_insurance === undefined ? 0 : rowData.long_time_care_insurance;
	document.getElementById('total_deduction_amount').textContent = rowData.total_deduction_amount === null || rowData.total_deduction_amount === undefined ? 0 : rowData.total_deduction_amount;
}

document.addEventListener("DOMContentLoaded", () => {
	// 급여구분 공통코드
	const divId = { "0J": "payrollType" };
	getCmCodeOptions(divId);

	// 상여지급방법 공통코드 (라디오 버튼)
	const divIdRadio = { "0K": "bonusTypeContainer" };
	getCmCodeRadio(divIdRadio);

	// 부서 조회
	getDeptOptions();

	/* 상여등록 후 초기화 하는 함수 */
	function resetBonusRegisterForm() {
		document.querySelector("#payrollPeriod").value = ""; // 귀속연월
		document.querySelector('#bonusType_k1').checked = true; // 지급액(amount) 버튼 초기화 추가
		document.querySelector('#bonusType_k2').checked = false; // 상여지급방법 라디오버튼 초기화
		document.querySelector("#bonus").value = ""; // 지급률 및 지급액
		document.querySelector("#payrollBonusName").value = "";
		document.querySelector("#payrollStartDate").value = "";
		document.querySelector("#payrollEndDate").value = "";
		document.querySelector("#payrollBonusDate").value = "";
		targetUserGrid.resetData([]); // 대상 사원 목록 초기화
		updatePeopleNumber(); // 인원수 초기화
	}

	/* 급여대장-상여등록-초기화버튼 */
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

	/* 2. 우측 그리드: 상여 등록 대상 목록 (저장 대상) */
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

	// =========================================================================
	// 데이터 이동 로직
	// =========================================================================

	/* + 버튼 (전체 사원 -> 대상 사원) */
	document
		.getElementById("btnAddSelected")
		.addEventListener("click", function() {
			const selectedRows = allUserGrid.getCheckedRows();

			if (selectedRows.length === 0) {
				alert("추가할 사원을 선택해주세요.");
				return;
			}

			// 우측 그리드에 이미 존재하는 사원 ID 목록
			const existingUserIds = targetUserGrid.getData().map((row) => row.userId);

			// 중복되지 않은 새로운 행만 필터링
			const newRowsToAdd = selectedRows.filter(
				(row) => !existingUserIds.includes(row.userId)
			);

			if (newRowsToAdd.length === 0) {
				alert("선택된 사원 중 추가할 수 있는 사원이 없습니다.");
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
				alert("제외할 대상 사원을 선택해주세요.");
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

	/* 
	 * 급여대장-상여등록
	 * 저장버튼을 누르면 상여등록이 이루어진다.
	 */
	document.getElementById("btnSave").addEventListener("click", function() {
		// 대상 그리드(targetUserGrid)의 모든 사원을 가져옴
		const checkedEmployees = targetUserGrid.getData();
		const employeeIds = checkedEmployees.map((row) => row.userId);

		if (employeeIds.length === 0) {
			alert("상여 등록 대상 사원을 한 명 이상 추가해야 합니다.");
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
					alert(result.count + "건의 상여 등록이 완료되었습니다.");
					targetUserGrid.resetData([]); // 성공 시 대상 목록 초기화
					updatePeopleNumber();
					resetBonusRegisterForm(); // 상여등록 폼 초기화
				} else {
					alert("등록 실패 : " + result.message);
				}
			})
			.catch((error) => console.error("Error:", error));
	});

	/* 인원수 반영 (대상 그리드의 전체 사원 수) */
	const bonusPeopleNumberInput = document.querySelector("#bonusPeopleNumber");
	function updatePeopleNumber() {
		const totalTargetRows = targetUserGrid.getData().length;
		bonusPeopleNumberInput.value = totalTargetRows;
	}

	// 대상 그리드에서 행이 추가/제거될 때마다 인원수 업데이트
	targetUserGrid.on("afterRemoveRow", updatePeopleNumber);
	targetUserGrid.on("afterAppendRow", updatePeopleNumber);

	/* 급여대장-조회 */
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
		bodyHeight: 570,
		columns: [
			{ header: "귀속연월", name: "payrollPeriod", align: "center", width: 80 },
			{ header: "급여구분", name: "payrollType", width: 70 },
			{ header: "대장명칭", name: "payrollName", width: 200 },
			{ header: "지급일", name: "payrollDate", align: "center" },
			{ header: "인원수", name: "peopleNumber", width: 70, align: "right" },
			{
				header: "급여계산",
				name: "salaryCalculation",
				align: "center",
				formatter: function(data) {
					const payrollPeriodCode = data.row.payrollPeriodCode;
					return `<a class="btn-calculate" href=# data-payroll-period-code="${payrollPeriodCode}">계산하기</a>`;
				},
			},
			{ header: "지급총액", name: "totalAmount" },
		],
	}); // end of payrollGrid

	/* 계산하기 클릭했을때 모달창이 나타나게 */
	document.querySelector('#payrollGrid').addEventListener('click', function(event) {

		const targetElement = event.target;
		// 클릭된 요소가 'btn-calculate' 클래스를 가졌는지 확인
		if (targetElement.classList.contains('btn-calculate')) {
			event.preventDefault(); // href="#"의 기본 동작(페이지 상단 이동)을 막습니다.

			// 데이터(payrollCode)를 가져옵니다.
			const payrollPeriodCode = targetElement.dataset.payrollPeriodCode;
			currentPayrollPeriodCode = payrollPeriodCode;

			console.log(`[급여계산 클릭] 대상 payrollPeriodCode: ${payrollPeriodCode}`);

			// 모달 열기
			const payrollManageModal = document.querySelector('#payrollManageModal');
			if (payrollManageModal) {
				payrollManageModal.hidden = false;
			} else {
				console.error("Modal element #historyModal not found.");
			}

			payrollDetailGrid.refreshLayout();
			// totalAllowances.refreshLayout();

			// 3. 급여 계산 API 호출 및 Grid 데이터 로드 로직 추가
			// 백엔드 컨트롤러(/api/hr/UserPayList)에 정의한 API 경로와 파라미터를 사용합니다.
			fetch(`/api/hr/UserPayList?payroll_period_code=${payrollPeriodCode}`)
				.then(res => {
					// HTTP 응답이 200 OK가 아니면 에러 처리
					if (!res.ok) {
						throw new Error(`HTTP error! status: ${res.status}`);
					}
					return res.json();
				})
				.then(responseMap => { // 응답 전체(Map)를 받습니다.
					console.log("[급여계산 전체 응답]", responseMap);

					if (responseMap.result && responseMap.data && responseMap.data.contents) {
						// 성공 시, 실제 데이터 리스트는 responseMap.data.contents에 담겨 있습니다.
						const payList = responseMap.data.contents;
						console.log("[추출된 급여 목록]", payList);

						// Tui Grid에 계산된 사원별 급여 결과 데이터 설정
						payrollDetailGrid.resetData(payList); // 실제 데이터 리스트(payList)를 Grid에 설정

						if (payList.length === 0) {
							alert("계산된 급여 데이터가 없습니다.");
						}
					} else {
						// 실패 시 처리 (result: false 인 경우)
						alert("급여 계산 결과를 불러오는 데 실패했습니다: " + (responseMap.message || "알 수 없는 오류"));
						payrollDetailGrid.resetData([]); // Grid 초기화
					}
				})
				.catch(error => {
					console.error("Error loading payroll calculation result:", error);
					alert("급여 계산 결과를 불러오는 중 오류가 발생했습니다.");
				});
		}
	}); // end of payrollGrid 클릭 이벤트

	/* 급여관리 모달창에서 x버튼 누르면 초기화 */
	document.querySelector('#btnPayrollManageClose').addEventListener('click', function() {
		const payrollManageModal = document.querySelector('#payrollManageModal');
		if (payrollManageModal) {
			payrollManageModal.hidden = true;
		}
		payrollDetailGrid.resetData([]);
		resetSummaryTables();
	});

	/* 급여관리 모달 - 사원별 급여 상세 Grid */
	const payrollDetailGrid = new tui.Grid({
		el: document.getElementById("payrollDetailGrid"),
		scrollX: true,
		scrollY: true,
		data: [], // 초기 데이터는 비어있음
		bodyHeight: 200, // HTML에서 설정한 높이와 일치시킵니다.
		rowKey: "userId",
		columns: [
			// { header: "귀속연월", name: "payrollPeriod", align: "center", width: 80 },
			{ header: "사번", name: "user_id", align: "center", width: 150 },
			{ header: "성명", name: "user_name", width: 10 },
			{ header: "부서명", name: "dept_name", width: 100 },
			{ header: "지급일", name: "payroll_date", align: "center", width: 100 },
			{ header: "기본급", name: "salary", align: "right", width: 100 },
			{ header: "상여금", name: "bonus", align: "right", width: 100 },
			{ header: "수당총액", name: "total_allowance", align: "right", width: 100 },
			{ header: "총 지급액", name: "total_payment_amount", align: "right", width: 100 },
			{ header: "공제 총액", name: "total_deduction_amount", align: "right", width: 100 },
			{ header: "실 수령액", name: "net_pay", align: "right", width: 100 }
		],
	}); // end of payrollDetailGrid

	/* Tui Grid 행 클릭 이벤트: 선택된 행의 상세 정보를 표시 */
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

	// 확정 버튼을 눌렀을때 사원급여관리 테이블에 삽입
	document.querySelector('#btnPayrollSave').addEventListener('click', function(){
		
		if (!currentPayrollPeriodCode) {
			console.log("확정 대상 급여 대장을 찾을 수 없습니다. 다시 시도해 주세요.", currentPayrollPeriodCode);
			return;
		}
		const data = payrollDetailGrid.getData();
		// console.log('a : ',a);
		
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
					alert(successCount + "건의 등록이 확정되었습니다.");
					
				} else {
					alert("등록 실패 : " + textresult.message);
					console.log(textresult.message);
				}
			})
			.catch((error) => console.error("Error:", error));
		
	})	

}); // end of DOMContentLoaded
