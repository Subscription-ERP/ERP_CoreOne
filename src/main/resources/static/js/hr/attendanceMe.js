/**
 * attendanceMe.js (내근태관리)
 */

document.addEventListener("DOMContentLoaded", async () => {

	/* ------------------------------------------------------------------
	 * common.js 
	 * ------------------------------------------------------------------ */

	// input 클릭시 달력 선택창 뜨게 하기
	setupNativeDatePicker('reviewStartDate-wrapper', 'reviewStartDate-search');
	setupNativeDatePicker('reviewEndDate-wrapper', 'reviewEndDate-search');


	/* ------------------------------------------------------------------
	 * Toast UI Grid (근태 조회)
	 * ------------------------------------------------------------------ */
	let grid;

	function initGrid() {
		grid = new tui.Grid({
			el: document.querySelector('#myAttendanceGrid'),
			bodyHeight: 'fitToParent',
			scrollX: false,
			scrollY: true,
			columns: [
				{ header: "근무날짜", name: "workDate", align: 'center', sortable: true },
				{
					header: "출근시간",
					name: "inTime",
					align: 'center',
					formatter: ({ value }) => value ? value.substring(11, 19) : "-"
				},
				{
					header: "퇴근시간",
					name: "outTime",
					align: 'center',
					formatter: ({ value }) => value ? value.substring(11, 19) : "-"
				},
				{
					header: "근태구분",
					name: "attendTypeName",
					align: "center",
					formatter({ value }) {
						let color = "#e9e9e9";   // 기본 배경색
						let textColor = "#333";  // 기본 글자색

						// ATTEND_CALENDARS 기준 색 매핑
						if (value === "정상") {
							color = "#a0a0a0";      // h1
							textColor = "#fff";
						} else if (value === "지각") {
							color = "#FB8C00";      // h2
							textColor = "#fff";
						} else if (value === "조퇴") {
							color = "#FFB74D";      // h3
							textColor = "#fff";
						} else if (value === "결근") {
							color = "#E53935";      // h4
							textColor = "#fff";
						} else if (value === "연차") {
							color = "#4FC3F7";      // h5
							textColor = "#fff";
						} else if (value === "반차") {
							color = "#AED581";      // h6
							textColor = "#fff";
						} else if (value === "병가") {
							color = "#9575CD";      // h7
							textColor = "#fff";
						} else if (value === "외근") {
							color = "#BA68C8";      // h8
							textColor = "#fff";
						} else if (value === "출장") {
							color = "#64B5F6";      // h9
							textColor = "#fff";
						} else if (value === "휴무") {
							color = "#90A4AE";      // h10
							textColor = "#fff";
						}

						return `
				        <span style="
				          display:inline-block;
				          padding:4px 5px;
				          border-radius:14px;
				          background:${color};
				          color:${textColor};
				          font-size:12px;
				          font-weight:500;
				          min-width:56px;
				          text-align:center;
				        ">
				          ${value ?? ""}
				        </span>
				      `;
					}
				}
				,
				{ header: "총 근무시간", name: "totalWorkTime", align: 'right' },
				{ header: "연장근무", name: "overWorkTime", align: 'right' },
				{ header: "야간근무", name: "nightWorkTime", align: 'right' },
				{ header: "휴일근무", name: "holidayWorkTime", align: 'right' },
				{ header: "비고", name: "remark" }

			]
		});
	}


	/*	function highlightAnnualRows() {
		  const rows = grid.getData();
	
		  rows.forEach(row => {
			if (row.attendTypeName === '연차' || row.attendTypeName === '반차') {   // 값이 '연차' 맞는지 주의!
			  grid.addRowClassName(row.rowKey, 'row-annual');
			} else {
			  grid.removeRowClassName(row.rowKey, 'row-annual');
			}
		  });
		}*/


	async function loadMyAttendanceList() {
		//globalLoader.style.display = "flex";
		const response = await fetch('/api/att/my');
		const data = await response.json();
		grid.resetData(data);
		grid.refreshLayout();
		//highlightAnnualRows();
		globalLoader.style.display = "none";
	}

	initGrid();
	loadMyAttendanceList();



	/* ------------------------------------------------------------------
	 * 오늘 날짜/시간 표시 
	 * ------------------------------------------------------------------ */

	// 날짜 포맷팅 함수
	function formatKoreanDateTime(date) {
		const days = ["일", "월", "화", "수", "목", "금", "토"];

		const year = date.getFullYear();
		const month = date.getMonth() + 1;
		const day = date.getDate();
		const week = days[date.getDay()];

		let hours = date.getHours();
		const minutes = String(date.getMinutes()).padStart(2, "0");
		const seconds = String(date.getSeconds()).padStart(2, "0");

		const ampm = hours >= 12 ? "오후" : "오전";
		hours = hours % 12 || 12; // 0시일경우 12시로 표시
		hours = String(hours).padStart(2, "0");

		return `${year}년 ${month}월 ${day}일 (${week}) ${ampm} ${hours}:${minutes}:${seconds}`;
	}

	// 화면에 적용
	function updateCurrentDateTime() {
		const el = document.querySelector("#todayWorkDateTime");
		if (!el) return;

		const now = new Date();
		el.textContent = formatKoreanDateTime(now);
	}

	updateCurrentDateTime();
	setInterval(updateCurrentDateTime, 1000); // 매초 자동 업데이트


	/* ------------------------------------------------------------------
	 * 연차현황
	 * ------------------------------------------------------------------ */
	async function loadMyAnnualStatus() {
		try {
			const res = await fetch('/api/hr/myAnnualStatus');
			if (!res.ok) {
				throw new Error("연차 현황 조회 실패");
			}

			const data = await res.json();

			const total = data.totalGrantDays ?? 0;
			const used = data.totalUsedDays ?? 0;
			const remain = data.remainingDays ?? (total - used);

			document.getElementById('totalAnnualLeave').textContent = total;
			document.getElementById('usedAnnualLeave').textContent = used;
			document.getElementById('remainAnnualLeave').textContent = remain;

		} catch (err) {
			console.error(err)
			if (typeof showToast === 'function') {
				showToast('연차 현황을 불러오지 못했습니다.', 'error');
			}
		}
	}

	loadMyAnnualStatus();


	/* ------------------------------------------------------------------
	 * 오늘의 근태상태
	 * ------------------------------------------------------------------ */
	async function loadTodayMyAttendance() {
		try {
			const res = await fetch('/api/att/my/today');
			if (!res.ok) {
				throw new Error("오늘의 연차현황 조회 실패")
			}
			const data = await res.json();

			const inTimeRaw = data.inTime;
			const outTimeRaw = data.outTime;
			const overWorkTime = data.overWorkTime ?? 0;
			const nightWorkTime = data.nightWorkTime ?? 0;
			const totalWorkTime = data.totalWorkTime ?? 0;

			// 시간 형식 변경
			const inTime = formatTimeOnly(inTimeRaw);   // "09:00:00"
			const outTime = formatTimeOnly(outTimeRaw); // "18:00:00"
			const totalWorkText = formatWorkDuration(totalWorkTime); // "8시간 00분"

			document.querySelector("#todayOnTime").textContent = inTime;
			document.querySelector("#todayOffTime").textContent = outTime;
			document.querySelector("#todayOverTime").textContent = overWorkTime;
			document.querySelector("#todayNightTime").textContent = nightWorkTime;
			document.querySelector("#todayTotalWorkTime").textContent = totalWorkText;

		} catch (err) {
			console.error(err);
			if (typeof showToast === 'function') {
				showToast('오늘의 근태를 불러오지 못했습니다.', 'error');
			}
		}
	}

	loadTodayMyAttendance();


	/* ------------------------------------------------------------------
	 * 날짜형식 변경 함수
	 * ------------------------------------------------------------------ */

	// 시간만 나타내기
	function formatTimeOnly(dateTimeStr) {
		if (!dateTimeStr) return '-';

		// 공백 기준으로 자르기 (2025-12-10 09:00:00)
		if (dateTimeStr.includes(' ')) {
			return dateTimeStr.split(' ')[1];
		}

		// ISO 형식 대비 (2025-12-10T09:00:00)
		if (dateTimeStr.includes('T')) {
			return dateTimeStr.split('T')[1];
		}

		// 안전장치: 그냥 뒤에서 8글자 자르기
		return dateTimeStr.slice(-8);

	}

	// 총 근무시간 00시간 00분
	function formatWorkDuration(hoursValue) {
		const h = Math.floor(hoursValue || 0);              // 정수시간
		const m = Math.round(((hoursValue || 0) - h) * 60); // 소수점이 있으면 분으로

		const hh = `${h}시간`;
		const mm = `${m.toString().padStart(2, '0')}분`;

		return `${hh} ${mm}`;
	}


	/* ------------------------------------------------------------------
	 * 오늘 진행바
	 * ------------------------------------------------------------------ */

	// 09:00 ~ 18:00 기준으로 오늘 하루 근무 진행률 계산해서 바에 반영
	function updateWorkTimeline() {
		const bar = document.getElementById('workTimelineProgress');
		if (!bar) return;

		const now = new Date();

		// 오늘 날짜의 09:00, 18:00
		const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 9, 0, 0);
		const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 18, 0, 0);

		let percent = 0;

		if (now <= start) {
			// 출근 전
			percent = 0;
		} else if (now >= end) {
			// 퇴근 이후
			percent = 100;
		} else {
			// 근무 중: 오늘 9시부터 지금까지 / 전체 근무시간
			percent = ((now - start) / (end - start)) * 100;
		}

		bar.style.width = `${percent}%`;
	}

	updateWorkTimeline();
	setInterval(updateWorkTimeline, 60 * 1000); 	// 1분마다 갱신






});
