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

	// 공통코드
	//const divId = { '0Q': 'workPlaceType' };
	//getCmCodeOptions(divId);

	/* ------------------------------------------------------------------
	 * Toast UI Grid (근태 조회)
	 * ------------------------------------------------------------------ */
	let grid;                    // ToastUIGrid 
	let todayAttendance = null;  // 오늘 내 근태정보 저장용

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
	 * 퇴근
	 * ------------------------------------------------------------------ */
	const btnLeaveWork = document.querySelector("#btnLeaveWork");

	if (btnLeaveWork) {
		btnLeaveWork.addEventListener("click", async () => {

			try {
				const res = await fetch('/api/att/my/checkout', {
					method: "POST",
					headers: {
						"Content-Type": "application/json",
					},
					body: JSON.stringify({})
				});

				if (!res.ok) {
					throw new Error("서버오류");
				}

				const data = await res.json();

				if (data.success) {
					showToast("퇴근처리가 완료되었습니다.", 'success');

					// 오늘 근태 상태 새로고침(퇴근시간 갱신)
					await loadTodayMyAttendance();

					// 근태 목록 전체 새로고침
					await loadMyAttendanceList();

					// 퇴근 버튼 비활성화
					btnLeaveWork.disabled = true;
					btnLeaveWork.classList.add("disabled");
				} else {
					showToast("퇴근 처리 실패하였습니다.", "warning");
				}
			} catch (err) {
				console.error(err);
				showToast("퇴근처리 중 에러가 발생했습니다.", "error");
			}

		});
	};
	


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

			todayAttendance = data; // 근태정보 저장

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

			updateTodayWorkStatusBadge(inTimeRaw, outTimeRaw); // 배지업데이트

			const btnLeaveWork = document.querySelector("#btnLeaveWork");

			// 출근/퇴근 상태에 따라 퇴근 버튼 활성/비활성
			if (btnLeaveWork) {
				if (data.outTime) {
					// 이미 퇴근 했으므로 비활성
					btnLeaveWork.disabled = true;
					btnLeaveWork.classList.add("disabled");
				} else if (data.inTime) {
					// 출근은 했고 퇴근은 안 한 경우 → 활성화
					btnLeaveWork.disabled = false;
					btnLeaveWork.classList.remove("disabled");
				} else {
					// 오늘 출근도 안 했으면 비활성
					btnLeaveWork.disabled = true;
					btnLeaveWork.classList.add("disabled");
				}
			}

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
	function updateWorkTimeline() {
		const progress = document.getElementById('workTimelineProgress');
		const nowDot = document.getElementById('timelineNowDot');
		const tip = document.getElementById('timelineNowTip');
		const lunchRange = document.getElementById('timelineLunchRange');

		if (!progress || !nowDot || !tip) return;

		const now = new Date();

		const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 9, 0, 0);
		const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 18, 0, 0);

		const totalMs = end - start;
		if (totalMs <= 0) return;

		const clamp = (v) => Math.min(100, Math.max(0, v));
		const nowPct = clamp(((now - start) / totalMs) * 100);

		// 진행바
		progress.style.width = `${nowPct}%`;

		// 현재 동그라미
		nowDot.style.left = `${nowPct}%`;

		// 툴팁 위치 동기화
		tip.style.left = `${nowPct}%`;

		// 시간 텍스트
		const hh = String(now.getHours()).padStart(2, '0');
		const mm = String(now.getMinutes()).padStart(2, '0');
		tip.textContent = `${hh}:${mm}`;

		// 출근 전 / 퇴근 후 색상
		nowDot.classList.toggle('is-off', now < start || now > end);
	}





	updateWorkTimeline();
	setInterval(updateWorkTimeline, 60 * 1000); 	// 1분마다 갱신


	/* ------------------------------------------------------------------
	 * 근무상태 배지
	 * ------------------------------------------------------------------ */

	function updateTodayWorkStatusBadge(inTimeRaw, outTimeRaw) {
	  const badge = document.querySelector("#todayWorkStatusBadge");
	  if (!badge) return;

	  const now = new Date();
	  const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 18, 0, 0);

	  // 초기화
	  badge.className = "pill pill--status";

	  const dot = badge.querySelector(".pill__dot");
	  const textEl = badge.querySelector(".pill__text");

	  if (outTimeRaw) {
	    badge.classList.remove("pill--status");
	    badge.classList.add("pill--off");
	    if (textEl) textEl.textContent = "퇴근";
	  } else if (!inTimeRaw) {
	    badge.classList.remove("pill--status");
	    badge.classList.add("pill--before");
	    if (textEl) textEl.textContent = "출근 전";
	  } else {
	    // 출근했고 퇴근 안 함
	    if (now > end) {
	      badge.classList.remove("pill--status");
	      badge.classList.add("pill--night");
	      if (textEl) textEl.textContent = "야근중";
	    } else {
	      badge.classList.add("pill--status");
	      if (textEl) textEl.textContent = "근무중";
	    }
	  }
	}


	/* ------------------------------------------------------------------
	 * 근무형태 배지
	 * ------------------------------------------------------------------ */
	// 근무장소 배지 동기화 함수
	function syncWorkPlaceBadge() {
	  const select = document.querySelector('#workPlaceType');
	  const badge = document.querySelector('#todayWorkPlaceBadge');
	  if (!select || !badge) return;

	  const opt = select.options[select.selectedIndex];
	  const text = opt ? opt.textContent.trim() : '';

	  if (!text) {
	    badge.classList.add('d-none');
	    return;
	  }

	  // 아이콘 매핑 (원하면 더 추가)
	  const iconClass =
	    text.includes('사무실') ? 'bi-building' :
	    text.includes('외근')   ? 'bi-geo-alt' :
	    text.includes('출장')   ? 'bi-briefcase' :
	    text.includes('재택')   ? 'bi-house' :
	                              'bi-dot';

	  const icon = badge.querySelector('.pill__icon');
	  const label = badge.querySelector('.pill__text');

	  if (icon) icon.className = `bi ${iconClass} pill__icon`;
	  if (label) label.textContent = text;

	  badge.classList.remove('d-none');
	}


	// 근무장소 코드
	async function loadWorkPlaceType() {
		try {
			const res = await fetch('/api/com/commonCodes?code=0Q');
			if (!res.ok) throw new Error('공통코드 조회 실패');

			const data = await res.json();   // 예: { "0Q": [ {code:"q1", codeName:"사무실"}, ... ] }
			const list = data['0Q'] || [];

			const select = document.querySelector('#workPlaceType');
			if (!select) return;

			// 기존 옵션 초기화
			select.innerHTML = '';

			// 옵션 채우기
			list.forEach(item => {
				const opt = document.createElement('option');
				opt.value = item.code;
				opt.textContent = item.codeName;
				select.appendChild(opt);
			});

			// 기본값: q1(사무실)로 선택 시도
			const hasQ1 = Array.from(select.options).some(o => o.value === 'q1');
			if (hasQ1) {
				select.value = 'q1';
			} else if (select.options.length > 0) {
				// q1이 없으면 첫 번째 항목
				select.selectedIndex = 0;
			}

			// 현재 선택한 값 기억(나중에 되돌릴 용도)
			let currentWorkPlaceType = select.value;

			// 배지도 바로 한 번 동기화
			syncWorkPlaceBadge();

			// 변경될 때마다 배지 업데이트
			select.addEventListener('change', async (e) => {

				const newValue = e.target.value;

				// 1) 출근 기록 없거나 이미 퇴근했으면 → 변경 막기
				const hasIn = todayAttendance && todayAttendance.inTime;
				const hasOut = todayAttendance && todayAttendance.outTime;

				if (!hasIn || hasOut) {
					// 셀렉트 값을 원래 값으로 되돌리기
					e.target.value = currentWorkPlaceType;
					syncWorkPlaceBadge();

					if (typeof showToast === 'function') {
						if (!hasIn) {
							showToast('출근 기록이 없어 근무형태를 변경할 수 없습니다.', 'warning');
						} else if (hasOut) {
							showToast('이미 퇴근하여 근무형태를 변경할 수 없습니다.', 'warning');
						}
					} else {
						alert('출근 기록이 없거나 이미 퇴근하여 근무형태를 변경할 수 없습니다.');
					}
					return;
				}


				// 화면 배지 먼저 업데이트
				syncWorkPlaceBadge();

				// 서버에 오늘 근무형태 업데이트 요청
				// 출근은 했고 퇴근은 하지 않은 상태 -> 서버에 변경 요청
				try {
					const res = await fetch('/api/att/my/workPlace', {
						method: 'POST',
						headers: {
							'Content-Type': 'application/json',
						},
						body: JSON.stringify({
							workPlaceType: e.target.value, // q1, q2 ...
						}),
					});

					const data = await res.json();

					if (typeof showToast === 'function') {
						showToast("근무형태가 변경되었습니다.", 'success');
					} else {
						alert(result.message || '근무형태가 변경되었습니다.');
					}

				} catch (err) {
					console.error(err);
					if (typeof showToast === 'function') {
						showToast('오늘 출근기록이 없거나 이미 퇴근처리 되었습니다..', 'error');
					} else {
						alert(result.message || '오늘 출근기록이 없거나 이미 퇴근 처리되었습니다.');
					}
				}

			});

		} catch (err) {
			console.error(err);

			e.target.value = currentWorkPlaceType;
			syncWorkPlaceBadge();

			if (typeof showToast === 'function') {
				showToast('근무형태 변경 중 오류가 발생했습니다.', 'error');
			} else {
				alert('근무형태 변경 중 오류가 발생했습니다.');
			}

		}
	}
	await loadWorkPlaceType();


	/* ------------------------------------------------------------------
	 * 검색
	 * ------------------------------------------------------------------ */
	const btnSearch = document.querySelector("#btnSearch");

	if (btnSearch) {
		btnSearch.addEventListener("click", async () => {
			const start = document.querySelector("#reviewStartDate-search")?.value || '';
			const end = document.querySelector("#reviewEndDate-search")?.value || '';

			// 둘다 비어져 있으면 -> 전체조회로 되돌리기
			if (!start && !end) {
				await loadMyAttendanceList();
				return;
			}

			//하나만 있을 경우 경고
			if ((start && !end) || (!start && end)) {
				if (typeof showToast === 'function') {
					showToast('조회 기간은 시작일과 종료일을 모두 입력해 주세요.', 'warning');
				} else {
					alert('조회 기간은 시작일과 종료일을 모두 입력해 주세요.');
				}
				return;
			}

			try {
				// 스피너 ON
				if (globalLoader) globalLoader.style.display = 'flex';

				const params = new URLSearchParams();
				params.append('startDate', start); // 예: 2025-12-01
				params.append('endDate', end);     // 예: 2025-12-10

				const res = await fetch(`/api/att/my/search?${params.toString()}`);
				if (!res.ok) throw new Error('근태 검색 실패');

				const data = await res.json();
				grid.resetData(data);
				grid.refreshLayout();

			} catch (err) {
				console.error(err);
				if (typeof showToast === 'function') {
					showToast('근태 목록 조회 중 오류가 발생했습니다.', 'error');
				}
			} finally {
				// 스피너 OFF
				if (globalLoader) globalLoader.style.display = 'none';
			}
		});
	}

	/* ------------------------------------------------------------------
	 * 초기화
	 * ------------------------------------------------------------------ */
	const btnResetSearch = document.querySelector("#btnResetSearch");

	if (btnResetSearch) {
		btnResetSearch.addEventListener("click", async () => {

			// input 초기화
			const startInput = document.querySelector("#reviewStartDate-search");
			const endInput = document.querySelector("#reviewEndDate-search");

			if (startInput) startInput.value = null;
			if (endInput) endInput.value = null;

			// 조회 초기화
			await loadMyAttendanceList();

		})
	}












});
