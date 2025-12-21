/**----------------------------------------------
 * 제목 : 매인 화면
 * @description 로그인 했을 경우의 메인화면 자바스크립트
 * @file dashBoard.js
 *-----------------------------------------------*/

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
		{ header: "이메일", name: "email", width: 300, sortable: true, },
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

	// 날씨 정보 가져오기 호출
	getWeatherData();

	// 1. 매출 현황 차트 (Line Chart)
	const salesCtx = document.getElementById('salesChart').getContext('2d');
	new Chart(salesCtx, {
		type: 'line',
		data: {
			labels: ['7월', '8월', '9월', '10월', '11월', '12월'],
			datasets: [{
				label: '매출액 (백만원)',
				data: [45, 52, 48, 61, 55, 70],
				borderColor: '#0d6efd',
				backgroundColor: 'rgba(13, 110, 253, 0.1)',
				fill: true,
				tension: 0.4
			}, {
				label: '수주액 (백만원)',
				data: [40, 45, 50, 55, 60, 65],
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
	new Chart(leaveCtx, {
		type: 'doughnut',
		data: {
			labels: ['(주)에이비씨', '국민상사', '삼성전자', '기타'],
			datasets: [{
				data: [45, 25, 20, 10], // 실제 DB 수주액 비율
				backgroundColor: ['#0d6efd', '#198754', '#ffc107', '#6c757d']
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
});