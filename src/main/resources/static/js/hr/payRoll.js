/**
 * payRoll.js
 */

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

			console.log(`[급여계산 클릭] 대상 Payroll Code: ${payrollPeriodCode}`);

			// 모달 열기
			const payrollManageModal = document.querySelector('#payrollManageModal');
			if (payrollManageModal) {
				payrollManageModal.hidden = false;
			} else {
				console.error("Modal element #historyModal not found.");
			}

			payrollDetailGrid.refreshLayout();
			// totalAllowances.refreshLayout();
			
			// 3. ✨ 급여 계산 API 호출 및 Grid 데이터 로드 로직 추가 ✨
			// 백엔드 컨트롤러(/api/hr/UserPayList)에 정의한 API 경로와 파라미터를 사용합니다.
			fetch(`/api/hr/UserPayList?payroll_period_code=${payrollPeriodCode}`)
				.then(res => {
					// HTTP 응답이 200 OK가 아니면 에러 처리
					if (!res.ok) {
						throw new Error(`HTTP error! status: ${res.status}`);
					}
					return res.json();
				})
				.then(data => {
					console.log("[급여계산 결과]", data);

					// Tui Grid에 계산된 사원별 급여 결과 데이터 설정
					// 가정: 계산된 결과는 payrollDetailGrid에 표시됩니다.
					payrollDetailGrid.resetData(data);

					// 필요하다면, 첫 번째 행을 선택하고 상세 정보를 업데이트하는 로직 추가
					if (data.length > 0) {
						// (예시) totalAllowances Grid에 첫 번째 사원의 상세 데이터를 로드하는 추가 로직
						// totalAllowances.resetData([data[0].allowanceDetails]);
					}
				})
				.catch(error => {
					console.error("Error loading payroll calculation result:", error);
					alert("급여 계산 결과를 불러오는 중 오류가 발생했습니다.");
				});
		}
	}); // end of payrollGrid 클릭 이벤트

	document.querySelector('#btnPayrollManageClose').addEventListener('click', function() {
		const payrollManageModal = document.querySelector('#payrollManageModal');
		if (payrollManageModal) {
			payrollManageModal.hidden = true;
		}
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
			{ header: "사번", name: "userId", align: "center" },
			{ header: "성명", name: "userName", width: 10 },
			{ header: "부서명", name: "deptName", width: 10 },
			{ header: "지급일", name: "payrollDate", align: "center", width: 10 },
			{ header: "기본급", name: "baseSalary", align: "right", formatter: 'money' },
			{ header: "상여금", name: "bonusAmount", align: "right", formatter: 'money' },
			{ header: "수당총액", name: "totalAllowance", align: "right", formatter: 'money' },
			{ header: "총 지급액", name: "totalPayment", align: "right", formatter: 'money' },
			{ header: "공제 총액", name: "totalDeduction", align: "right", formatter: 'money' },
			{ header: "실 수령액", name: "netPay", align: "right", formatter: 'money' }
		],
	}); // end of payrollDetailGrid



}); // end of DOMContentLoaded
