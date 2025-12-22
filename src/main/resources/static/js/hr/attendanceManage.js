/**
 * attendanceManage.js
 */



let currentMonthAttendance = [];   // 월 전체 데이터
let currentSelectedDate = null;    // 현재 선택된 날짜 (yyyy-MM-dd)
let currentSelectedType = null;    // h1~h10
let empGrid = null;                // Toast Grid 인스턴스
let calendar = null;


document.addEventListener('DOMContentLoaded', function() {

	/* ------------------------------------------------------------------
	 * common.js : 공통코드, 부서
	 * ------------------------------------------------------------------ */
	//const divId = { '0H': 'attendType' }
	//getCmCodeOptions(divId);

	// 부서 select 두 군데: 상세폼 + 검색폼
	getDeptOptions2([".form-grid #dept", "#dept-search"]);


	/* ------------------------------------------------------------------
	 * Toast UI Calendar 
	 * ------------------------------------------------------------------ */
	// 사원목록
	initEmpGrid();

	// Calendar 생성
	const Calendar = tui.Calendar; // CDN 전역 객체
	calendar = new Calendar('#attendanceCalendar', {
		defaultView: 'month',
		usageStatistics: false,
		useDetailPopup: false,    // 상세 팝업 숨김
		useCreationPopup: false,  // 생성 팝업 숨김
		isReadOnly: true,
		month: {
			startDayOfWeek: 0,    // 0=일요일, 1=월요일
		},
		calendars: ATTEND_CALENDARS,
		template: {
			// 시간 이벤트(우리가 만든 sqrt 타입) 렌더링 커스터마이징
			time: function(schedule) {
				// schedule.title 에 이미 "정상 2건" 이런게 들어있음
				return schedule.title || '';
			}
		}

	});


	calendar.on("clickEvent", (ev) => {
		const event = ev.event;
		// event.id가 'h1_2025-12-04' 이런 형태
		const m = String(event.id).match(/_(\d{4}-\d{2}-\d{2})$/);
		if (!m) return;

		const dateStr = m[1];
		console.log("[clickEvent dateStr]", dateStr);

		currentSelectedDate = dateStr;
		currentSelectedType = null;
		setSelectedDateLabel(dateStr);
		updateTodaySummary(currentMonthAttendance, dateStr);

		clearEmpGrid("근태상황에서 항목을 선택하세요");
		clearSummaryActive();
	});

	// 현재 년월 텍스트 표시 함수
	const currentMonth = document.getElementById('calCurrentMonth');

	function renderCurrentMonth() {
		const date = calendar.getDate();      // TZDate 객체
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		currentMonth.textContent = `${year}-${month}`;
	}

	// 최초 1번 표시
	renderCurrentMonth();

	// 최초 진입 시 현재 달 데이터 로드
	loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));

	// 3) 버튼 이벤트
	document.getElementById('btnCalToday').addEventListener('click', function() {
		calendar.today();
		renderCurrentMonth();
		// Today로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});

	document.getElementById('btnCalPrev').addEventListener('click', function() {
		calendar.prev();
		renderCurrentMonth();
		// 이전 달로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});

	document.getElementById('btnCalNext').addEventListener('click', function() {
		calendar.next();
		renderCurrentMonth();
		// 다음 달로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});



	/* ------------------------------------------------------------------
	 * 모달
	 * ------------------------------------------------------------------ */

	// 화면에 보이는 사원명 input
	const UserName = document.getElementById("keyName");
	// 실제 선택된 사원 ID (hidden)
	const UserId = document.getElementById("UserId");

	// 사원명 input 클릭 시 모달 열기
	if (UserName) {
		UserName.addEventListener("click", (e) => {
			if (typeof openUserSearchModal === 'function') {
				openUserSearchModal(e);
			} else {
				console.error("에러가 발생했습니다.");
			}
		});
	}

	// 모달에서 row 선택 시 호출되는 콜백 (전역)
	window.handleSelectedEmp = function(row) {
		if (UserId) {
			UserId.value = row.userId;
		}
		if (UserName) {
			UserName.value = row.userName;
		}
	};




});

const ATTEND_CALENDARS = [
	{ id: 'h1', name: '정상', backgroundColor: '#A0A0A0', borderColor: '#A0A0A0' },
	{ id: 'h2', name: '지각', backgroundColor: '#FB8C00', borderColor: '#FB8C00' },
	{ id: 'h3', name: '조퇴', backgroundColor: '#FFB74D', borderColor: '#FFB74D' },
	{ id: 'h4', name: '결근', backgroundColor: '#E53935', borderColor: '#E53935' },
	{ id: 'h5', name: '연차', backgroundColor: '#4FC3F7', borderColor: '#4FC3F7' },
	{ id: 'h6', name: '반차', backgroundColor: '#AED581', borderColor: '#AED581' },
	{ id: 'h7', name: '병가', backgroundColor: '#9575CD', borderColor: '#9575CD' },
	{ id: 'h8', name: '외근', backgroundColor: '#BA68C8', borderColor: '#BA68C8' },
	{ id: 'h9', name: '출장', backgroundColor: '#64B5F6', borderColor: '#64B5F6' },
	{ id: 'h10', name: '휴무', backgroundColor: '#90A4AE', borderColor: '#90A4AE' },
];

// 현재 달(YYYY-MM) 문자열로 변환하는 헬퍼
function getCurrentMonthString(calendar) {
	const date = calendar.getDate();
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	// console.log(year - month);
	return `${year}-${month}`;

}

// 순서 고정 배열
const ATTEND_TYPE_ORDER = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10'];

// 월별 근태 데이터조회 + 집계 + 달력 반영 + 금일요약
async function loadAttendanceByMonth(calendar, month) {
	try {
		// 검색조건 함께 보내기
		const userId = document.getElementById("UserId")?.value?.trim() || "";
		const deptCode = document.getElementById("dept-search")?.value || "";

		const qs = new URLSearchParams({ month, userId, deptCode });

		// 월별 데이터 조회
		const res = await fetch(`/api/att/attMonthList?${qs.toString()}`);
		const list = await res.json();  // AttendanceVO 리스트

		// 월 데이터 저장
		currentMonthAttendance = list;
		// 날짜별 + 근태유형별로 집계
		const grouped = {};

		list.forEach(item => {
			const date = toYmd(item.workDate);
			const type = item.attendType;      // "h1" 같은 코드
			const typeName = item.attendTypeName;

			if (!grouped[date]) {
				grouped[date] = {
					total: 0,
					types: {}
				};
			}

			grouped[date].total++;

			if (!grouped[date].types[type]) {
				grouped[date].types[type] = {
					name: typeName,
					count: 0
				};
			}
			grouped[date].types[type].count++;
		});

		// ToastUI 캘린더에 넣을 이벤트 배열 만들기
		const events = [];

		Object.entries(grouped).forEach(([date, info]) => {
			// date: "2025-12-01" 형식이라고 가정
			const [y, m, d] = date.split('-').map(Number);

			// 하루 안에 찍히는 time 이벤트로 (예: 09:00 ~ 09:05)
			const startDate = new Date(y, m - 1, d, 9, 0, 0);
			const endDate = new Date(y, m - 1, d, 9, 5, 0);

			// 우리가 정한 순서대로만 돌기
			ATTEND_TYPE_ORDER.forEach(typeCode => {
				const typeInfo = info.types[typeCode];   // 예: { name: '정상', count: 5 }
				if (!typeInfo) return;                  // 해당 날에 그 근태가 없으면 건너뜀

				events.push({
					id: `${typeCode}_${date}`,
					calendarId: typeCode,                          // h1 ~ h10
					title: `${typeInfo.name} ${typeInfo.count}건`, // "정상 5건" 같은 문자열
					start: startDate,
					end: endDate,
					category: 'time',
					isAllday: false
				});
			});
		});


		// 기존 이벤트 지우고 새로 생성
		calendar.clear();          // 기존 이벤트 전체 삭제
		calendar.createEvents(events);

		// 금일 근태 상황 요약 갱신
		/*const today = new Date();
		const todayStr =
			`${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;*/

		const targetDate = currentSelectedDate || toYmd(new Date());
		setSelectedDateLabel(targetDate);
		updateTodaySummary(list, targetDate);
		clearEmpGrid("근태상황에서 항목을 선택하세요");

		/*		currentSelectedDate = todayStr;
				setSelectedDateLabel(todayStr);
				updateTodaySummary(list, todayStr);
				//renderAttendanceDetail(list, todayStr); 
				clearEmpGrid("근태상황에서 항목을 선택하세요");
		*/
	} catch (err) {
		console.error("근태 월별 조회 실패", err);
	}
}

// 금일 근태상황 요약
function updateTodaySummary(list, targetDate) {
	const ids = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10'];
	ids.forEach(id => {
		const el = document.getElementById(id);
		if (el) el.textContent = 0;
	});

	const dailyList = list.filter(item => toYmd(item.workDate) === targetDate);

	let totalCount = 0;
	dailyList.forEach(item => {
		const id = item.attendType; // h1~h10
		const el = document.getElementById(id);
		if (!el) return;

		el.textContent = Number(el.textContent || 0) + 1;
		totalCount++;
	});

	const totalEl = document.getElementById('todayTotal');
	if (totalEl) totalEl.textContent = totalCount;
}


// 라벨,초기화 함수 
function setSelectedDateLabel(dateStr) {
	const el = document.getElementById("selectedDateLabel");
	if (el) el.textContent = dateStr;
}

function clearEmpGrid(message) {
	const label = document.getElementById("empListLabel");
	if (label) label.textContent = message || "";

	if (empGrid) {
		empGrid.resetData([]);
	}
}


// 근태상황 클릭 이벤트
const rows = document.querySelectorAll('.attendance-summary__table tbody tr');

rows.forEach(row => {
	const typeCode = row.dataset.type;
	if (!typeCode) return;

	row.addEventListener('click', async () => {
		if (!currentSelectedDate) return;

		currentSelectedType = typeCode;
		setSummaryActive(typeCode); // 선택 강조(선택사항)

		const typeName = row.querySelector('th span:last-child')?.textContent?.trim() || typeCode;

		// 사원목록 로드
		await loadEmpListByDateAndType(currentSelectedDate, typeCode);

		const label = document.getElementById("empListLabel");
		if (label) label.textContent = `${currentSelectedDate} / ${typeName} 사원 목록`;
	});
});


function setSummaryActive(typeCode) {
	document.querySelectorAll('.attendance-summary__table tbody tr')
		.forEach(tr => tr.classList.toggle('is-active', tr.dataset.type === typeCode));
}
function clearSummaryActive() {
	document.querySelectorAll('.attendance-summary__table tbody tr')
		.forEach(tr => tr.classList.remove('is-active'));
}




/* 사원목록 Toast Grid */
function initEmpGrid() {
	empGrid = new tui.Grid({
		el: document.querySelector('#attendanceEmpGrid'),
		bodyHeight: 'fitToParent',
		scrollX: false,
		scrollY: true,
		columns: [
			{ header: "사원번호", name: "USER_ID", align: "center" },
			{ header: "성명", name: "USER_NAME", align: "center" },
			{ header: "부서", name: "DEPT_NAME", align: "center" },
			{ header: "근태", name: "ATTEND_TYPE_NAME", align: "center" },
		],
	});

	empGrid.resetData([]);
}

/* 목록 로드 함수 */
async function loadEmpListByDateAndType(dateStr, typeCode) {
	const userId = document.getElementById("UserId")?.value?.trim() || "";
	const deptCode = document.getElementById("dept-search")?.value || "";

	const qs = new URLSearchParams({
		date: dateStr,
		type: typeCode,
		userId,
		deptCode
	});

	const res = await fetch(`/api/att/manage/list?${qs.toString()}`);
	if (!res.ok) {
		console.error("사원목록 조회 실패", res.status);
		empGrid.resetData([]);
		return;
	}
	const list = await res.json();
	console.log(list[0]); // console로 확인
	empGrid.resetData(list);
}

/* 달력 선택시 */
document.getElementById("btnSearch")?.addEventListener("click", async () => {
	// 달력(월 데이터) 다시 로드
	await loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));

	if (currentSelectedDate && currentSelectedType) {

		await loadEmpListByDateAndType(currentSelectedDate, currentSelectedType);
	} else {
		clearEmpGrid("달력 날짜 선택 → 근태상황 항목을 선택하세요");
	}
});


// 초기화 버튼
document.getElementById("btnResetSearch")?.addEventListener("click", () => {
	document.getElementById("keyName").value = "";
	document.getElementById("UserId").value = "";
	document.getElementById("dept-search").value = "";

	// 사원목록 초기화
	clearEmpGrid("근태상황에서 항목을 선택하세요");
	currentSelectedType = null;
	clearSummaryActive();

	// 달력 재로드
	loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));

	if (currentSelectedDate && currentSelectedType) {
		loadEmpListByDateAndType(currentSelectedDate, currentSelectedType);
	} else {
		clearEmpGrid("근태상황에서 항목을 선택하세요");
	}
});



// 날짜 일치
function toYmd(v) {
	if (!v) return "";
	// 1) 이미 "YYYY-MM-DD"면 그대로
	if (typeof v === "string" && /^\d{4}-\d{2}-\d{2}$/.test(v)) return v;

	// 2) "YYYY-MM-DD ..." (시간 붙은 문자열)
	if (typeof v === "string") {
		const m = v.match(/^(\d{4}-\d{2}-\d{2})/);
		if (m) return m[1];
	}

	// 3) ISO (예: 2025-12-21T15:00:00.000+00:00)
	// → Date로 파싱한 뒤 로컬 기준 YYYY-MM-DD
	const d = new Date(v);
	if (!isNaN(d.getTime())) {
		const y = d.getFullYear();
		const m = String(d.getMonth() + 1).padStart(2, "0");
		const day = String(d.getDate()).padStart(2, "0");
		return `${y}-${m}-${day}`;
	}

	return "";
}






