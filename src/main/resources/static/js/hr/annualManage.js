/**
 * 연차 관리 페이지
 * @file annualManage.js
 * @description 사원 개인별로 연차이력을 조회 할 수 있으며 또한 연차신청도 할 수 있습니다.
 */

/* =============
 * 조회 초기화 기능
 * ============= */
function resetAnnualSearchForm() {
	document.querySelector('#annualStartDateSearch').value = '';
	document.querySelector('#annualStartEndSearch').value = '';
	document.querySelector('#leaveApplyStartDateSearch').value = '';
	document.querySelector('#leaveApplyEndDateSearch').value = '';
}

/* ===========
 * 조건 조회 기능
 * =========== */
function annualSearch() {
	const annualStartDateSearch = document.querySelector('#annualStartDateSearch').value;
	const annualStartEndSearch = document.querySelector('#annualStartEndSearch').value;
	const leaveApplyStartDateSearch = document.querySelector('#leaveApplyStartDateSearch').value;
	const leaveApplyEndDateSearch = document.querySelector('#leaveApplyEndDateSearch').value;

	const data = {
		annualStartDateSearch: annualStartDateSearch,
		annualStartEndSearch: annualStartEndSearch,
		leaveApplyStartDateSearch: leaveApplyStartDateSearch,
		leaveApplyEndDateSearch: leaveApplyEndDateSearch,
	}
	annualDetailGrid.readData(1, data, true);
}

/* ============
 * 연차신청이력조회
 * ============ */
const annualDetailGrid = new tui.Grid({
	el: document.getElementById("annualDetailGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/annualManageList",
				method: "GET",
			},
		},
	},
	bodyHeight: 200,
	rowKey: "user_id",
	columns: [
		{ header: "사번", name: "userId", align: "center", sortable: true, width: 150 },
		{ header: "성명", name: "userName", align: "center", sortable: true, width: 120 },
		{ header: "부서명", name: "deptName", align: "center", sortable: true, width: 150 },
		{ header: "직급", name: "jobTitle", align: "center", sortable: true, width: 120 },
		{ header: "신청구분", name: "leaveType", align: "center", sortable: true, width: 120 },
		{ header: "사용일수", name: "usedDays", align: "center", sortable: true, width: 100 },
		{
			header: "연차시작일", name: "annualStartDate", align: "center", sortable: true, width: 150,
			formatter: ({ value }) => value ? value.substring(0, 10) : ''
		},
		{
			header: "연차종료일", name: "annualEndDate", align: "center", sortable: true, width: 150,
			formatter: ({ value }) => value ? value.substring(0, 10) : ''
		},
		{ header: "연차신청일", name: "leaveApplyDate", align: "center", sortable: true, width: 150 },
		{
			header: "사유", name: "rm", minWidth: 500,whiteSpace: 'pre-wrap'
		},
	],
}); // end of payrollDetailGrid


/* ==============
 * 연차일때 반차일때
 * ============== */
function annualDateChange() {
	const leaveType = document.getElementById("leaveType");

	// 만약 select 박스가 아직 없다면 console에 찍고 중단
	if (!leaveType) {
		console.error("leaveType 요소를 찾을 수 없습니다. HTML이 생성된 후 실행하세요.");
		return;
	}

	leaveType.addEventListener('change', (e) => {
		const type = e.target.value;
		console.log("선택된 값:", type);
		const startInput = document.getElementById("annualStartDate");
		const endInput = document.getElementById("annualEndDate");

		if (type === 'b1') { // 연차
			startInput.type = "date";
			endInput.type = "date";
			document.querySelector('#annualStartDateLabel').textContent = "연차시작일";
			document.querySelector('#annualEndDateLabel').textContent = "연차종료일";
			document.getElementById("usedDays").value = 0; // 연차일 경우 사용일수 초기화
			document.querySelector('#annualEndDate').removeAttribute('readonly'); // 사용일수 readonly 설정
			document.querySelector('#annualEndDate').removeAttribute('disabled'); // 사용일수 disabled 설정
		} else if (type === 'b2' || type === 'b3') { // b2: 오전반차, b3: 오후반차
			document.querySelector('#annualStartDateLabel').textContent = "반차시작일";
			document.querySelector('#annualEndDateLabel').textContent = "-";
			document.querySelector('#usedDays').value = 0.5; // 반차일 경우 사용일수 0.5로 자동 설정
			document.querySelector('#annualEndDate').setAttribute('readonly', 'readonly'); // 사용일수 readonly 설정
			document.querySelector('#annualEndDate').setAttribute('disabled', 'disabled'); // 사용일수 disabled 설정
		}

		// 타입 변경 시 기존 입력값 초기화 (포맷이 맞지 않아 오류가 날 수 있음)
		startInput.value = '';
		endInput.value = '';

	});
}

/**=====================================================================================================
 * 연차신청 저장 버튼
 * @description 연차신청부분에서 신청구분, 연차시작일, 연차종료일, 사유를 입력하고 저장버튼을 누르면 연차 등록을 할 수 있습니다.
 * @author 장준현
 * ===================================================================================================== */
function submitAnnualForm() {
	// 데이터 보내기
	document.querySelector('#btnSave').addEventListener('click', () => {
		// 성명
		const userId = document.getElementById("userId").value;
		// 부서명
		const myAnuualdeptName = document.getElementById("myAnuualdeptName").value;
		// 직급
		const myAnuualjobTitle = document.getElementById("myAnuualjobTitle").value;
		// 신청구분
		const leaveType = document.getElementById("leaveType").value;
		// 사용일수
		const usedDays = Number(document.getElementById("usedDays").value);
		// 연차시작일
		const annualStartDate = document.getElementById("annualStartDate").value;
		// 연차종료일
		const annualEndDate = document.getElementById("annualEndDate").value;
		// 사유
		const rm = document.getElementById("rm").value;
		// 잔여연차
		const remainingDays = Number(document.querySelector('#remainingDays').textContent);

		// 연차 시작일 유효성 검사 알림창
		if (annualStartDate === '' || !annualStartDate) {
			showToast('시작일을 작성해주세요!', 'warning');
			return;
		}
		// 연차 종료일 유효성 검사 알림창
		if (leaveType === 'b1' && (annualEndDate === '' || !annualEndDate)) {
			showToast('종료일을 작성해주세요!', 'warning');
			return;
		}
		// 사유 유효성 검사 알림창
		if (rm === '') {
			showToast('사유를 작성해주세요!', 'warning');
			return;
		}

		// 작성한 사용일수가 잔여연차보다 많을 경우 알림창뜨면서 막기
		if (usedDays > remainingDays) {
			showToast('사용일수가 잔여연차보다 많습니다!', 'warning');
			return;
		}
		// 잔여연차가 0일경우 연차를 사용할 수 없다고 알림창 띄우기
		if (remainingDays === 0) {
			showToast('잔여연차가 없습니다.', 'warning');
			return;
		}

		// 서버전송할때 보낼 데이터 객체 생성
		const data = {
			userId: userId,
			myAnuualdeptName: myAnuualdeptName,
			myAnuualjobTitle: myAnuualjobTitle,
			leaveType: leaveType,
			usedDays: usedDays,
			annualStartDate: annualStartDate,
			annualEndDate: annualEndDate,
			rm: rm,
		};
		console.log("보낼 데이터:", data);
		fetch("/api/hr/myAnnualApply", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(data),
		})
			.then((response) => response.text())
			.then((result) => {
				if (result > 2) {
					showToast("연차 신청이 완료되었습니다.", 'success');
					resetAnnualForm();
					// 내 연차 현황 다시 불러오기
					getmyAnnualStatus();
					// 그리드 데이터 다시 불러오기
					annualDetailGrid.readData();

				} else {
					showToast("등록 실패 : " + result.message, 'error');
				}
			})
			.catch((error) => console.error("Error:", error));
	})
}

/* ===================
 * 사용일수 데이터 계산하기 
 * =================== */
function calculateUsedDays() {
	const leaveType = document.getElementById("leaveType").value;
	const annualStartDate = document.querySelector('#annualStartDate').value;
	const annualEndDate = document.querySelector('#annualEndDate').value;

	// 두 날짜가 모두 입력되었는지 확인
	if (annualStartDate && annualEndDate) {

		const startDate = new Date(annualStartDate);
		const endDate = new Date(annualEndDate);
		// 종료일이 시작일 보다 빠른 경우
		if (endDate < startDate) {
			showToast("종료일이 시작일보다 빠를 수 없습니다.", 'warning');
			document.querySelector('#annualStartDate').value = ''; // 연차시작일
			document.querySelector('#annualEndDate').value = ''; // 연차종료일
			return;
		}

		let annualOfUse;
		// 반차일 경우 시간 차이에 따라 0.5일 계산
		if (leaveType === 'b2') { // 반차
			return;
		} else {
			annualOfUse = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
			document.querySelector('#usedDays').value = annualOfUse;
		}
	}
}

/**============================================================================
 * 연차신청 초기화 버튼
 * @description 연차신청에서 초기화 버튼을 누르면 신청할때 작성한 내용들이 모두 초기화가 됩니다.
 * ============================================================================ */
function resetAnnualForm() {
	document.querySelector('#leaveType').value = 'b1'; // 신청구분
	document.querySelector('#annualStartDate').value = ''; // 연차시작일
	document.querySelector('#annualEndDate').value = ''; // 연차종료일
	document.querySelector('#usedDays').value = ''; // 사용일수
	document.querySelector('#rm').value = ''; // 사유
}


/* ==============
 * 내 연차 현황 함수
 * ============== */
function getmyAnnualStatus() {
	fetch(`/api/hr/myAnnualStatus`)
		.then((res) => res.json())
		.then((response) => {
			if (response) {
				// 현재 내 연차 현황 데이터 넣기
				document.getElementById('totalGrantDays').innerText = response.totalGrantDays;
				document.getElementById('totalUsedDays').innerText = response.totalUsedDays;
				document.getElementById('remainingDays').innerText = response.remainingDays;
				document.getElementById('expiryDate').innerText = response.expiryDate;

				// 연차신청부분에 readonly데이터 넣기
				document.getElementById('userId').value = response.userId; // 사번
				document.getElementById('myAnnualUserName').value = response.userName; // 성명
				document.getElementById('myAnuualdeptName').value = response.deptName; // 부서명
				document.getElementById('myAnuualjobTitle').value = response.jobTitle; // 지위직급
			}
		})
		.catch((err) => console.error(err));
}

document.addEventListener("DOMContentLoaded", async () => {

	/* ==================
	 * 연차신청 초기화 버튼
	 * ================== */
	document.querySelector('#btnReset').addEventListener('click', () => {
		resetAnnualForm();
	});

	/* ==================
	 * 신청구분 (공통코드)
	 * ================== */
	const divId = { "0B": "leaveType" };
	getCmCodeOptions(divId);

	/* ==================
	 * 내 연차 현황 함수 호출
	 * ================== */
	getmyAnnualStatus();

	/* =============================
	 * 날짜 변경 시 자동 계산 이벤트 리스너
	 * ============================= */
	const startInput = document.querySelector('#annualStartDate');
	const endInput = document.querySelector('#annualEndDate');
	startInput.addEventListener('change', calculateUsedDays);
	endInput.addEventListener('change', calculateUsedDays);

	/* ===========
	 * 오늘 날짜 출력
	 * =========== */
	document.querySelector('#leaveApplyDate').value = new Date().toLocaleDateString('ko-KR');

	/* =============================
	 * INPUT 클릭시 달력 선택창 뜨게 하기
	 * ============================= */
	setupNativeDatePicker('annualStartDateWrapper', 'annualStartDate')
	setupNativeDatePicker('annualEndDateWrapper', 'annualEndDate')

	/* ===========================================
	 * 연차일때는 date 반차일때는 datetime-local 로 설정
	 * =========================================== */
	annualDateChange();

	/* ===============
	 * 연차신청 저장 버튼
	 * =============== */
	submitAnnualForm();

	/* ===================
	 * 연차신청이력조회 조회버튼
	 * =================== */
	document.querySelector('#btnAnnualManageSearch').addEventListener('click', () => {
		console.log('클릭');
		annualSearch();
	})

	/* ====================
	 * 조회에 초기화 버튼 활성화
	 * ==================== */
	document.querySelector('#btnAnnualManageReset').addEventListener('click', () => {
		resetAnnualSearchForm();
	})

});