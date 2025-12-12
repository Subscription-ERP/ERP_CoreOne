/**
 * deptCodeManage.js
 */

/* ======
 * 전역변수
 * ====== */
const deptCode = document.querySelector('#deptCodeManageDeptCode'); // 부서 등록 - 부서코드
const DeptName = document.querySelector('#deptCodeManageDeptName'); // 부서 등록 - 부서이름
const UpperDeptCode = document.querySelector('#deptCodeManageUpperDeptCode'); // 부서 등록 - 상위부서코드
const DeptLevel = document.querySelector('#deptCodeManagedeptLevel'); // 부서 등록 - 부서레벨
const StartDate = document.querySelector('#deptCodeManageStartDate'); // 부서 등록 - 적용시작일
const EndDate = document.querySelector('#deptCodeManageEndDate'); // 부서 등록 - 적용종료일
const Status = document.querySelector('#deptCodeManageStatus'); // 부서 등록 - 상태
const Rm = document.querySelector('#deptCodeManageRm'); // 부서 등록 - 비고
const UpperDeptName = document.querySelector('#deptCodeManageUpperDeptName'); // 부서 등록 - 상위부서명

/* ====================================================================================
 * 부서 등록 - 부서 목록에서 부서를 선택 할 경우 부서 등록에 선택한 부서의 내용들이 나타나고 수정 할 수 있음
 * ==================================================================================== */
function selectRow() {
	deptCodeManageGrid.on('click', (ev) => {
		const rowKey = ev.rowKey; // 이벤트가 발생한 rowkey를 가져오고
		const rowData = deptCodeManageGrid.getRow(rowKey); // 그 rowkey에 해당하는 그리드의 행 데이터를 가져옴
		console.log(rowData);

		// 가져온 rowData를 오른쪽 등록쪽에 보내기
		if (!isDeptSelectionMode) { // 부서선택 상태값이 false일때만 작동되도록
			deptCode.value = rowData.deptCode; // 부서코드
			DeptName.value = rowData.deptName; // 부서이름
			UpperDeptCode.value = rowData.upperDeptNo; // 상위부서코드
			UpperDeptName.value = rowData.upperDeptName; // 상위부서이름
			DeptLevel.value = rowData.deptLevel; // 부서레벨
			StartDate.value = rowData.startDate; // 적용시작일
			EndDate.value = rowData.endDate; // 적용종료일
			Status.value = rowData.status; // 상태
			Rm.value = rowData.rm; // 비고
		}
	})
}

/* =======================================================
 * 부서 등록 - 적용종료일이 적용시작일보다 작게 반영할 경우 안된다고 막기
 * ======================================================= */
function endStartDate() {
	if (StartDate.value && EndDate.value && EndDate.value < StartDate.value) {
		showToast("적용종료일은 적용시작일보다 이전일 수 없습니다.", 'error');
		EndDate.value = '';
		return false; // 유효성검사 실패 반환
	}
	return true; // 유효성검사 성공 반환
}

/* ==============================================
 * 부서 등록 - 적용시작일이 입력되면 상태값을 '종료'으로 넣기
 * ============================================== */
function deptCodeManageEndDate() {
	if (endStartDate()) {
		Status.value = '종료';
	} else {
		// 유효성 검사에 실패하여 EndDate 값이 초기화되었으므로, 상태를 재검토
		handleStatusUpdate();
	}
}

/* ==============================================
 * 부서 등록 - 적용시작일이 입력되면 상태값을 '운영'으로 넣기
 * ============================================== */
function deptCodeManageStartDate() {
	endStartDate(); // 종료일이 이미 있다면 유효성만 체크하고 잘못된 EndDate를 초기화 (상태 변경은 아래 함수에서)
	handleStatusUpdate();
}

/* ================================================
 * 부서 등록 - StartDate와 EndDate에 기반하여 상태값을 결정
 * ================================================ */
function handleStatusUpdate() {
	const start = StartDate.value;
	const end = EndDate.value;

	if (end) {
		// EndDate가 있으면 무조건 '종료' 상태로 간주
		Status.value = '종료';
	} else if (start) {
		// StartDate는 있으나 EndDate가 없으면 '운영' 상태로 간주
		Status.value = '운영';
	} else {
		// StartDate도 없으면 상태 초기화 (또는 기본값 설정)
		Status.value = '';
	}
}

/* ===================
 * 상태값 저장 및 버튼 변수
 * =================== */
let isDeptSelectionMode = false; // 부서 선택 모드 상태(기본값 : false)
const DeptSelectBtnElement = document.querySelector('#DeptSelectBtn'); // 부서선택
const DeptCancelBtnElement = document.querySelector('#DeptCancelBtn'); // 부서취소

/* ============================
 * 부서 등록 - 부서선택 버튼 기능 구현
 * ============================ */
function DeptSelectBtn() {
	if (isDeptSelectionMode) return; // 부서 선택 모드 상태가 true면 빠져나가기(중복 실행 막는거임)

	// 상태 플래그 활성화
	isDeptSelectionMode = true; // 상태 활성화
	DeptSelectBtnElement.disabled = true; // 부서선택 버튼 비활성화
	// DeptCancelBtnElement.style.display = 'inline'; // 취소 버튼 활성화 

	// 그리드에 이벤트 리스너 추가
	deptCodeManageGrid.on('click', onDeptGridSelect);

	showToast("부서 목록에서 상위 부서로 지정할 부서를 선택하세요. (부서 선택 모드 활성화)", 'info');
}

/* ====================================================================
 * 부서 등록 - 부서선택 버튼 기능 구현 - 부서 선택 모드를 해제하고 리스너를 제거하는 함수
 * ==================================================================== */
function disableDeptSelectionMode() {
	if (!isDeptSelectionMode) return; // 모드가 true면 빠져나가기

	// 1. 상태 플래그 비활성화 및 버튼 상태 변경
	isDeptSelectionMode = false;
	DeptSelectBtnElement.disabled = false; // 부서선택 버튼 재활성화
	// DeptSelectCancelBtnElement.style.display = 'none'; // 취소 버튼 비활성화 (선택 사항)

	// 2. 그리드 이벤트 리스너 제거
	// TUI Grid의 off() 함수를 사용하여 'click' 이벤트에 연결된 리스너 해제
	deptCodeManageGrid.off('click', onDeptGridSelect);
}

/* =====================================================
 * 그리드 행 선택 할때 실행하는 함수
 * (이 함수는 DeptSelectBtn()이 호출했을 때만 TUI Grid에 연결됨)
 * ===================================================== */
function onDeptGridSelect(ev) {
	if (!isDeptSelectionMode) return; // 모드가 true면 빠져나가기

	const rowKey = ev.rowKey; // 선택한 행의 key값을 저장
	const selectedDept = deptCodeManageGrid.getRow(rowKey); // key값의 데이터를 저장

	if (selectedDept) { // 데이터가 있으면
		// 선택한 데이터를 오른쪽 '부서 등록' 영역의 필드에 반영
		UpperDeptCode.value = selectedDept.deptCode;
		UpperDeptName.value = selectedDept.deptName;
		DeptLevel.value = parseInt(selectedDept.deptLevel) + 1; // 필요하다면 레벨 자동 설정

		showToast(`상위 부서 '${selectedDept.deptName}'가 설정되었습니다.`, 'success');

		// 데이터 반영 후, 선택 모드 해제
		disableDeptSelectionMode();
	}
}

/* ========================
 * 부서 등록 초기화 버튼 기능 구현
 * ======================== */
function deptCodeManageInsertBtnReset() {
	deptCode.value = ''; // 부서코드
	DeptName.value = '';
	UpperDeptCode.value = '';
	UpperDeptName.value = '';
	DeptLevel.value = '';
	StartDate.value = '';
	EndDate.value = '';
	Status.value = '';
	Rm.value = '';
	disableDeptSelectionMode(); // 상위 부서 선택 상태값 false로
}

/* ========================
 * 부서 등록 저장 버튼 기능 구현
 * ======================== */
function deptCodeManageBtnSave() {
	// 각각 입력한 값들 가져오기
	const deptCodeValue = deptCode.value; // 부서코드
	const DeptNameValue = DeptName.value; // 부서이름
	const UpperDeptCodeValue = UpperDeptCode.value; // 상위부서코드
	const DeptLevelValue = DeptLevel.value || 1; // 부서레벨
	const StartDateValue = StartDate.value; // 적용시작일
	const EndDateValue = EndDate.value; // 적용종료일
	const StatusValue = (Status.value === '운영' ? 0 : 1); // 상태
	const RmValue = Rm.value; // 비고
	
	// data로 하나로 묶기
	const data = {
		deptCode: deptCodeValue || null,
		deptName: DeptNameValue,
		upperDeptNo: UpperDeptCodeValue,
		deptLevel: DeptLevelValue,
		startDate: StartDateValue,
		endDate: EndDateValue,
		status: StatusValue,
		rm: RmValue
	}

	// fetch로 data값 POST로 보내기
	fetch("/api/cm/deptRegister", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(data),
	})
		.then(res => res.text())
		.then(result => {
			if (result === '등록완료') {
				showToast(result + "되었습니다.");
				deptCodeManageGrid.reloadData();
			} else if (result === '수정완료') {
				showToast(result + "되었습니다.");
				deptCodeManageGrid.reloadData();
			} else {
				showToast("등록 실패 : " + result.message, 'error');
			}
		})
		.catch((error) => console.error("Error: ", error));
}

/* =====================
 * 검색 초기화 버튼 기능 구현
 * ===================== */
function deptCodeManageBtnReset() {
	document.querySelector('#deptName').value = '';
	document.querySelector('#upperDeptNameSearch').value = '';
	document.querySelector('#status').value = '';
	document.querySelector('#deptCodeManageStartLevel').value = '';
	document.querySelector('#deptCodeManageEndLevel').value = '';
}

/* ===========================================
 * 검색 부서레벨 범위 비교
 * 일단 기본적으로 숫자 1밑으로는 안들어가도록 만들기
 * =========================================== */
function deptLevelStartLowLimit() {
	let deptCodeManageStartLevel = document.querySelector('#deptCodeManageStartLevel');
	if (deptCodeManageStartLevel.value < 1) {
		deptCodeManageStartLevel.value = 1;
	}
}
function deptLevelEndLowLimit() {
	let deptCodeManageEndLevel = document.querySelector('#deptCodeManageEndLevel');
	if (deptCodeManageEndLevel.value < 1) {
		deptCodeManageEndLevel.value = 1;
	}
}

/* ========================
 * 검색 상태값 select 기능 구현
 * ======================== */
function getCmCodeOptionsPayRoll2(divId) {
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

/* ===================
 * 검색 조회 버튼 기능 구현
 * =================== */
function deptCodeManageBtnSearch() {
	// 각각 value값 가져오기
	const deptName = document.querySelector('#deptName').value; // 부서명
	const upperDeptNameSearch = document.querySelector('#upperDeptNameSearch').value; // 상위부서명
	const status = document.querySelector('#status').value; // 상태값
	const deptCodeManageStartLevel = document.querySelector('#deptCodeManageStartLevel').value; // 부서레벨시작값
	const deptCodeManageEndLevel = document.querySelector('#deptCodeManageEndLevel').value; // 부서레벨종료값

	// 조건 param값 data로 모으기
	const data = {
		deptName: deptName,
		upperDeptName: upperDeptNameSearch,
		status: status,
		deptCodeManageStartLevel: deptCodeManageStartLevel,
		deptCodeManageEndLevel: deptCodeManageEndLevel
	}

	// 조건 param값 data값으로 조회하고 그리드 새로고침
	deptCodeManageGrid.readData(1, data, true);
}

/* ===========================
 * 부서목록조회, 부서목록그리드(Grid)
 * =========================== */
const deptCodeManageGrid = new tui.Grid({
	el: document.getElementById("deptCodeManageGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/cm/selectDeptCodeManage",
				method: "GET",
			},
		},
	},
	bodyHeight: 590,
	columns: [
		{ header: "부서코드", name: "deptCode", align: "center", sortable: true },
		{ header: "부서명", name: "deptName", sortable: true, },
		{ header: "부서 레벨", name: "deptLevel", align: "right", sortable: true, },
		{ header: "상위 부서코드", name: "upperDeptNo", align: "center", sortable: true, },
		{ header: "상위 부서명", name: "upperDeptName", sortable: true, },
		{ header: "적용시작일", name: "startDate", align: "center", sortable: true, },
		{ header: "적용종료일", name: "endDate", align: "center", sortable: true, },
		{ header: "상태", name: "status", sortable: true, },
	],
}); // end of deptGrid

document.addEventListener("DOMContentLoaded", () => {
	/* =================================
	 * 검색조건 부서명 공통코드 SELECT박스 반영
	 * ================================= */
	getDeptOptions2(["#deptName"]);

	/* =====================================
	 * 검색조건 상위 부서명 공통코드 SELECT박스 반영
	 * ===================================== */
	getDeptOptions2(["#upperDeptNameSearch"]);

	/* =================================
	 * 검색조건 공통코드 상태값 SELECT박스 반영
	 * ================================= */
	getCmCodeOptionsPayRoll2({ "0R": "status" });

	/* ==================
	 * 검색 조회버튼 기능 구현
	 * ================== */
	document.querySelector('#deptCodeManageBtnSearch').addEventListener('click', deptCodeManageBtnSearch)

	/* ==========================================
	 * 검색 부서레벨 범위 비교
	 * 1. 일단 기본적으로 숫자 1밑으로는 안들어가도록 만들기
	 * ========================================== */
	document.querySelector('#deptCodeManageStartLevel').addEventListener('change', deptLevelStartLowLimit);
	document.querySelector('#deptCodeManageEndLevel').addEventListener('change', deptLevelEndLowLimit);

	/* =====================
	 * 검색 초기화 버튼 기능 구현
	 * ===================== */
	document.querySelector('#deptCodeManageBtnReset').addEventListener('click', deptCodeManageBtnReset);

	/* ========================
	 * 부서 등록 - 저장 버튼 기능 구현
	 * ======================== */
	document.querySelector('#deptCodeManageBtnSave').addEventListener('click', deptCodeManageBtnSave);

	/* ========================
	 * 부서 등록 - 초기화 버튼 기능 구현
	 * ======================== */
	document.querySelector('#deptCodeManageInsertBtnReset').addEventListener('click', deptCodeManageInsertBtnReset);

	/* ============================
	 * 부서 등록 - 부서선택 버튼 기능 구현
	 * ============================ */
	document.querySelector('#DeptSelectBtn').addEventListener('click', DeptSelectBtn);

	/* ==============================================
	 * 부서 등록 - 적용시작일이 입력되면 상태값을 '운영'으로 넣기
	 * ============================================== */
	StartDate.addEventListener('change', deptCodeManageStartDate);

	/* ==============================================
	 * 부서 등록 - 적용시작일이 입력되면 상태값을 '종료'으로 넣기
	 * ============================================== */
	EndDate.addEventListener('change', deptCodeManageEndDate);

	/* ==================================================
	 * 부서 등록 - 적용시작일 달력 input상자만 눌러도 달력이 나타나게
	 * ================================================== */
	setupNativeDatePicker('deptCodeManageStartWrapper', 'deptCodeManageStartDate'); // 부서코드관리-부서등록-적용시작일
	setupNativeDatePicker('deptCodeManageEndWrapper', 'deptCodeManageEndDate'); // 부서코드관리-부서등록-적용종료일

	/* ===================================================================================
	 * 부서 등록 - 부서 목록에서 부서를 선택 할 경우 부서 등록에 선택한 부서의 내용들이 나타나고 수정 할 수 있음
	 * =================================================================================== */
	selectRow();

}); // end of DOMContentLoaded