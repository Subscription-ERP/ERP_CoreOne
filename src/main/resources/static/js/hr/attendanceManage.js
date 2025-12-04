/**
 * attendanceManage.js
 */


/* ------------------------------------------------------------------
 * common.js : 공통코드, 부서
 * ------------------------------------------------------------------ */
const divId = { '0H': 'attendType' }
getCmCodeOptions(divId);

// 부서 select 두 군데: 상세폼 + 검색폼
getDeptOptions2([".form-grid #dept", "#dept-search"]);


let currentMonthAttendance = [];   // 월 전체 데이터
let currentSelectedDate = null;    // 현재 선택된 날짜 (yyyy-MM-dd)


/* ------------------------------------------------------------------
 * Toast UI Calendar 
 * ------------------------------------------------------------------ */

document.addEventListener('DOMContentLoaded', function() {
	// Calendar 생성
	const Calendar = tui.Calendar; // CDN 전역 객체
	const calendar = new Calendar('#attendanceCalendar', {
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

	
	calendar.on('selectDateTime', (ev) => {
	  const date = ev.start;
	  const y = date.getFullYear();
	  const m = String(date.getMonth() + 1).padStart(2, '0');
	  const d = String(date.getDate()).padStart(2, '0');
	  const dateStr = `${y}-${m}-${d}`;

	  currentSelectedDate = dateStr;  // ⭐ 선택 날짜 갱신
	  updateTodaySummary(currentMonthAttendance, dateStr);
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
		// ⭐ Today로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});

	document.getElementById('btnCalPrev').addEventListener('click', function() {
		calendar.prev();
		renderCurrentMonth();
		// ⭐ 이전 달로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});

	document.getElementById('btnCalNext').addEventListener('click', function() {
		calendar.next();
		renderCurrentMonth();
		// ⭐ 다음 달로 이동 후 다시 로드
		loadAttendanceByMonth(calendar, getCurrentMonthString(calendar));
	});
	
	
	
	// ⭐ 금일 근태 상황 행 hover → 툴팁
	  const rows = document.querySelectorAll('.attendance-summary__table tbody tr');

	  rows.forEach(row => {
	    const typeCode = row.dataset.type;  // h1, h2 ...
	    if (!typeCode) return;

	    row.addEventListener('mouseenter', (e) => {
	      showAttendanceTooltip(typeCode, e.clientX, e.clientY);
	    });

	    row.addEventListener('mousemove', (e) => {
	      // 마우스 움직일 때 위치 업데이트
	      showAttendanceTooltip(typeCode, e.clientX, e.clientY);
	    });

	    row.addEventListener('mouseleave', () => {
	      hideAttendanceTooltip();
	    });
	  });


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
	console.log(year - month);
	return `${year}-${month}`;

}

// 순서 고정 배열
			const ATTEND_TYPE_ORDER = ['h1','h2','h3','h4','h5','h6','h7','h8','h9','h10'];

// 월별 근태 데이터조회 + 집계 + 달력 반영 + 금일요약
async function loadAttendanceByMonth(calendar, month) {
	try {
		// 월별 데이터 조회
		const res = await fetch(`/api/att/attMonthList?month=${month}`);
		const list = await res.json();  // AttendanceVO 리스트

		// ⭐ 월 데이터 저장
		currentMonthAttendance = list;
		// 날짜별 + 근태유형별로 집계
		const grouped = {};
		

		list.forEach(item => {
			const date = item.workDate;        // "2025-12-01"
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
		  const endDate   = new Date(y, m - 1, d, 9, 5, 0);

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
		const today = new Date();
		const todayStr =
			`${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;

		currentSelectedDate = todayStr;
		updateTodaySummary(list, todayStr);
		//renderAttendanceDetail(list, todayStr); 

	} catch (err) {
		console.error("근태 월별 조회 실패", err);
	}
}

// 금일 근태상황 요약
function updateTodaySummary(list, targetDate) {
	// 1) h1 ~ h10 전부 0으로 초기화
	const ids = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10'];

	ids.forEach(id => {
		const el = document.getElementById(id);
		if (el) el.textContent = 0;
	});

	// 2) 해당 날짜 데이터만 필터링
	const dailyList = list.filter(item => item.workDate === targetDate);

	
	let totalCount = 0;

	  // 4) 개별 타입 카운트 증가
	// 3) attendType (h1~h10) 그대로 id로 사용해서 카운트 증가
	  dailyList.forEach(item => {
	    const id = item.attendType;   // h1 ~ h10
	    const el = document.getElementById(id);
	    if (!el) return;

	    const current = Number(el.textContent || 0);
	    el.textContent = current + 1;

	    totalCount++;
	  });
	
	// 5) 총합 표시
	  const totalEl = document.getElementById('todayTotal');
	  if (totalEl) {
	    totalEl.textContent = totalCount;   
	  }
}


function showAttendanceTooltip(typeCode, clientX, clientY) {
  const tooltip = document.getElementById('attendanceDetailTooltip');
  if (!tooltip || !currentSelectedDate || !currentMonthAttendance.length) return;

  // 1) 현재 선택된 날짜 + 근태코드에 해당하는 데이터만 필터링
  const dailyList = currentMonthAttendance.filter(item =>
    item.workDate === currentSelectedDate && item.attendType === typeCode
  );

  if (dailyList.length === 0) {
    tooltip.style.display = 'none';
    return;
  }

  // attendTypeName은 list 안에 동일한 값이므로 첫 번째 것 사용
  const typeName = dailyList[0].attendTypeName || typeCode;

  // 2) HTML 구성
  let html = `
    <div style="font-weight:600; margin-bottom:4px;">
      ${currentSelectedDate} / ${typeName} (${dailyList.length}명)
    </div>
    <ul style="padding-left:14px; margin:0;">
      ${dailyList.map(it => `
        <li style="font-size:11px; line-height:1.4;">
          ${it.userId}${it.userName ? ` / ${it.userName}` : ''}
        </li>
      `).join('')}
    </ul>
  `;

  tooltip.innerHTML = html;

  // 3) 위치 지정 (마우스 위치 기준)
  const offsetX = 10;
  const offsetY = 10;
  tooltip.style.left = (clientX + offsetX) + 'px';
  tooltip.style.top  = (clientY + offsetY) + 'px';

  tooltip.style.display = 'block';
}

function hideAttendanceTooltip() {
  const tooltip = document.getElementById('attendanceDetailTooltip');
  if (tooltip) {
    tooltip.style.display = 'none';
  }
}








