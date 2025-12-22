/**----------------------------------------------
 * 제목 : 매인 화면
 * @description 로그인 했을 경우의 메인화면 자바스크립트
 * @file dashBoard.js
 *-----------------------------------------------*/

/**
 * 전역변수
 */
let nosuzu = 0;
let todaySuzu = 0;
let todaySuzuPrice = 0;

/**-----------------------------------------------------------------
 * @todo tb_annual_leave_detail 여기서 오늘 날짜로 조회를 하는데 b1, b2인거 
 * 컬럼은 이름 부서 휴가종류 비고 
 *-----------------------------------------------------------------*/
const DashBoardAnnualLeaveDetail = new tui.Grid({
	el: document.getElementById("DashBoardAnnualLeaveDetail"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/cm/DashBoardAnnualLeaveDetail",
				method: "GET",
			},
		},
	},
	bodyHeight: 200, // HTML에서 설정한 높이와 일치시킵니다.
	columns: [
		{ header: "이름", name: "userName", align: "center", sortable: true, },
		{ header: "부서명", name: "deptName", sortable: true, },
		{
			header: "휴가종류",
			name: "leaveType",
			align: "center",
		},
	],
}); // end of payrollDetailGrid

// 임직원 연락처
const userContact = new tui.Grid({
	el: document.getElementById("userContact"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/payrollEmpList",
				method: "GET",
			},
		},
	},
	bodyHeight: 200, // HTML에서 설정한 높이와 일치시킵니다.
	columns: [
		{ header: "성명", name: "userName", align: "center", width: 70, sortable: true, },
		{ header: "부서명", name: "dept", width: 120, sortable: true, },
		{ header: "연락처", name: "tel", width: 120, align: "center", sortable: true, },
		{ header: "이메일", name: "email", sortable: true, },
	],
}); // end of payrollDetailGrid

// 날씨 api
function getWeatherData() {
	// 내 서버의 Proxy 경로를 호출합니다.
	// 컨트롤러에서 /api/cm과 /weather를 합쳤으므로 주소는 아래와 같습니다.
	const url = `/api/cm/weather?city=Daegu`;

	fetch(url)
		.then(response => {
			if (!response.ok) throw new Error("날씨 데이터를 가져올 수 없습니다.");
			return response.json();
		})
		.then(data => {
			// 온도는 소수점 첫째자리까지 표시
			const temp = Math.round(data.main.temp * 10) / 10;
			const weatherDesc = data.weather[0].description;
			const iconCode = data.weather[0].icon;
			const iconUrl = `https://openweathermap.org/img/wn/${iconCode}@2x.png`;

			// HTML 요소에 데이터 매핑
			document.querySelector('.card.bg-info h2').innerText = `${temp}°C`;
			document.querySelector('.card.bg-info .small').innerText = weatherDesc;

			// 아이콘 변경
			const iconContainer = document.querySelector('.card.bg-info .text-center');
			iconContainer.innerHTML = `<img src="${iconUrl}" alt="weather icon" style="width: 80px;"> <div class="small">${weatherDesc}</div>`;
		})
		.catch(error => {
			console.error("날씨 데이터를 가져오는 중 오류 발생:", error);
			document.querySelector('.card.bg-info .small').innerText = "날씨 정보 호출 실패";
		});
}

document.addEventListener('DOMContentLoaded', function() {
	let leaveChart;
	let salesChart;
	// 1. 매출 현황 차트 (Line Chart)
	const salesCtx = document.getElementById('salesChart').getContext('2d');
	salesChart = new Chart(salesCtx, {
		type: 'line',
		data: {
			labels: [],
			datasets: [{
				label: '수주액 (백만원)',
				data: [],
				borderColor: '#198754',
				borderDash: [5, 5],
				fill: false
			}]
		},
		options: {
			responsive: true,
			maintainAspectRatio: false
		}
	});

	// 주요 거래처별 매출 비중
	const leaveCtx = document.getElementById('leaveChart').getContext('2d');
	leaveChart = new Chart(leaveCtx, {
		type: 'doughnut',
		data: {
			labels: [],
			datasets: [{
				data: [], // 실제 DB 수주액 비율
				backgroundColor: []
			}]
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			plugins: {
				legend: { position: 'bottom' }
			}
		}
	});

	/**---------------------
	 * 수주건수, 가격 불러오기
	 *----------------------*/
	fetch('/api/inOrd/detail')
		.then(response => response.json())
		.then(data => {
			console.log(data);

			/**
			 * 오늘 수주 구하기
			 */
			// 1. 오늘 날짜 구하기 (YYYY-MM-DD 형식)
			const today = new Date().toISOString().split('T')[0];

			// 2. 오늘 날짜인 데이터만 필터링
			// item.inordDate가 "2025-12-22 10:30" 처럼 시간이 포함되어 있을 수 있으므로 includes를 사용합니다.
			const todayData = data.filter(item => {
				return item.inordDate && item.inordDate.includes(today);
			});
			todayData.forEach(item => {
				todaySuzuPrice += (item.price || 0);
				if (item.outputStatus != '2') {
					nosuzu++;
				} else {
					todaySuzu++;
				}
			});
			// 필요하다면 여기서 HTML KPI 카드에 값을 넣어줄 수 있습니다.
			document.querySelector('#nosuzu').innerText = `${nosuzu}건`;
			document.querySelector('#todaysuzu').innerText = `${todaySuzu}건`;
			document.querySelector('#todaySuzuPrice').innerText = `₩ ${todaySuzuPrice.toLocaleString()}`;

			/**
			 * 월별 수주액 합산 로직
			 */
			const monthlyData = {};

			data.forEach(item => {
				if (item.inordDate) {
					const m = item.inordDate.split('-')[1] + "월"; // "2025-12-22" -> "12" 추출
					if (!monthlyData[m]) {
						monthlyData[m] = 0;
					}
					monthlyData[m] += (item.price || 0) / 1000000;
				}
			});

			// 추출된 월들을 정렬 (07월, 08월... 순서대로)
			const sortedMonths = Object.keys(monthlyData).sort();
			// 정렬된 월에 해당하는 금액 배열 생성
			const sortedPrices = sortedMonths.map(month => monthlyData[month]);

			// --- [차트 데이터 업데이트] ---
			if (salesChart) {
				// 앞뒤로 빈 칸을 만들어 12월이 가운데 오게 배치
				const displayMonths = ["", ...sortedMonths, ""];
				const displayPrices = [null, ...sortedPrices, null];

				salesChart.data.labels = displayMonths;
				salesChart.data.datasets[0].data = displayPrices;

				// x축 선이 중간에서 시작하도록 설정 유지
				salesChart.options.scales = {
					x: {
						offset: true // 라벨을 양 끝에서 띄워주는 옵션
					}
				};

				salesChart.update();
			}

			/**
			 * 지금까지의 거래처별 거래 횟수
			 */
			const custCounts = {};

			data.forEach(item => {
				if (item.custName) {
					custCounts[item.custName] = (custCounts[item.custName] || 0) + 1;
				}
			});

			// 차트에 넣을 형식으로 변환
			const custLabels = Object.keys(custCounts); // ["거래처A", "거래처B", ...]
			const custData = Object.values(custCounts); // [5, 3, ...]

			// 랜덤 색상 또는 고정 색상 배열 생성 (거래처 개수만큼)
			const backgroundColors = [
				'#0d6efd', '#198754', '#ffc107', '#6c757d', '#dc3545',
				'#6610f2', '#fd7e14', '#20c997', '#0dcaf0'
			];

			// --- [도넛 차트 업데이트] ---
			// 먼저 차트 객체가 생성되어 있어야 합니다 (아래 2번 참고)
			if (leaveChart) {
				leaveChart.data.labels = custLabels;
				leaveChart.data.datasets[0].data = custData;
				leaveChart.data.datasets[0].backgroundColor = backgroundColors.slice(0, custLabels.length);
				leaveChart.update();
			}
		})
		.catch(error => {
			console.error("데이터 로드 중 에러:", error);
		});

	// 날씨 정보 가져오기 호출
	getWeatherData();



});