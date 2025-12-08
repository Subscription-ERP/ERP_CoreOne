/**
 * annualManage.js
 */

/* ================
 * 연차신청 저장 버튼
 * ================ */
function submitAnnualForm() {
	// 성명
	const userId = document.getElementById("userId").value;
	// 부서명
	const myAnuualdeptName = document.getElementById("myAnuualdeptName").value;
	// 직급
	const myAnuualjobTitle = document.getElementById("myAnuualjobTitle").value;
	// 신청구분
	const leaveType = document.getElementById("leaveType").value;
	// 사용일수
	const usedDays = document.getElementById("usedDays").value;
	// 연차시작일
	const annualStartDate = document.getElementById("annualStartDate").value;
	// 연차종료일
	const annualEndDate = document.getElementById("annualEndDate").value;
	// 사유
	const rm = document.getElementById("rm").value;
	
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
	
	// 데이터 보내기
	document.querySelector('#btnSave').addEventListener('click', () => {
		fetch("/api/hr/myAnnualApply", {
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
	const annualStartDate = document.querySelector('#annualStartDate').value;
	const annualEndDate = document.querySelector('#annualEndDate').value;

	// 두 날짜가 모두 입력되었는지 확인
	if (annualStartDate && annualEndDate) {

		const startDate = new Date(annualStartDate);
		const endDate = new Date(annualEndDate);
		// 종료일이 시작일 보다 빠른 경우
		if (endDate < startDate) {
			showToast("종료일이 시작일보다 빠를 수 없습니다.", 'warning');
			return;
		}

		const annualOfUse = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
		document.querySelector('#usedDays').value = annualOfUse;
	}
}

/* ================
 * 연차신청 초기화 버튼 
 * ================ */
function resetAnnualForm() {
	document.querySelector('#btnReset').addEventListener('click', () => {
		document.querySelector('#leaveType').value = 'b1'; // 신청구분
		document.querySelector('#annualStartDate').value = ''; // 연차시작일
		document.querySelector('#annualEndDate').value = ''; // 연차종료일
		document.querySelector('#rm').value = ''; // 사유
	})
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



document.addEventListener("DOMContentLoaded", () => {

	/* ==================
	 * 연차신청 초기화 버튼
	 * ================== */
	resetAnnualForm();

	/* ==================
	 * 신청구분 (공통코드)
	 * ================== */
	const divId = { "0B": "leaveType" };
	getCmCodeOptions(divId);

	/* ==================
	 * 내 연차 현황 함수 호출
	 * ================== */
	getmyAnnualStatus();

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
		bodyHeight: 240,
		rowKey: "user_id",
		columns: [
			{ header: "사번", name: "userId", align: "center" },
			{ header: "성명", name: "userName" },
			{ header: "부서명", name: "deptName" },
			{ header: "직급", name: "jobTitle" },
			{ header: "신청구분", name: "leaveType" },
			{ header: "사용일수", name: "usedDays", align: "right" },
			{ header: "연차시작일", name: "leaveStartDate", align: "center" },
			{ header: "연차종료일", name: "leaveEndDate", align: "center" },
			{ header: "연차신청일", name: "leaveApplyDate", align: "center" },
			{ header: "사유", name: "rm" },
		],
	}); // end of payrollDetailGrid

	/* ===================
	 * 날짜 변경 시 자동 계산 이벤트 리스너
	 * =================== */
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

});