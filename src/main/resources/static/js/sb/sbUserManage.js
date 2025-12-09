//ubUserManage.js
// src/main/resources/static/js/sbUserManage.js

document.addEventListener('DOMContentLoaded', function() {

	// 1) Toast Grid 테마 (깔끔한 테이블 느낌)
	tui.Grid.applyTheme('default', {
		grid: {
			border: '#e5e7eb'
		},
		row: {
			hover: {
				background: '#f9fafb'
			}
		},
		cell: {
			normal: {
				background: '#ffffff',
				border: '#e5e7eb'
			},
			header: {
				background: '#f9fafb',
				border: '#e5e7eb'
			}
		}
	});

	// 2) Grid 생성
	const grid = new tui.Grid({
		el: document.getElementById('paymentGrid'),
		scrollX: false,
		scrollY: false,        // 스크롤 없이 테이블처럼
		bodyHeight: 'auto',    // 데이터 길이만큼 자동 높이
		rowHeight: 40,
		header: {
			height: 40
		},
		showRowHeader: false,  // 왼쪽 번호/체크박스 숨김
		columns: [
			{ header: '결제일', name: 'paymentDate', width: 120, align: 'center' },
			{ header: '결제 금액', name: 'totalPrice', width: 120, align: 'right' },
			{ header: '결제 수단', name: 'paymentMethod', minWidth: 200 },
			{ header: '결제 상태', name: 'paymentStat', width: 100, align: 'center' },
			{ header: '기간', name: 'billingPeriod', minWidth: 220 }
		]
	});

	// 금액 포맷 함수
	function formatCurrency(value) {
		if (value === null || value === undefined) return '';
		try {
			return '₩ ' + Number(value).toLocaleString('ko-KR') + '원';
		} catch (e) {
			return value;
		}
	}

	// 결제 상태 한글 라벨 (원하면 코드 → 한글 매핑)
	function mapPaymentStat(stat) {
		if (!stat) return '';
		const map = {
			'DONE': '결제완료',
			'FAIL': '결제실패',
			'CANCEL': '취소',
			'REFUND': '환불'
		};
		return map[stat] || stat; // 매핑 없으면 원래 값
	}

	// 결제 수단 라벨
	function mapPaymentMethod(method, cardCompany) {
		// 예: "CARD" + "토스페이먼츠(****1234)"
		if (cardCompany) return cardCompany;
		if (!method) return '';
		const map = {
			'CARD': '신용카드',
			'VACCOUNT': '가상계좌',
			'TRANSFER': '계좌이체'
		};
		return map[method] || method;
	}

	// 3) 서버에서 결제 이력 가져오기
	fetch('/api/manage/payments')  // <-- 컨트롤러 URL 과 맞춰줘야 함
		.then(function(res) {
			if (!res.ok) {
				throw new Error('결제 이력 조회 실패');
			}
			return res.json();
		})
		.then(function(data) {
			// PaymentVO -> Grid row 변환
			const rows = data.map(function(item) {
				// item.paymentDate: "2025-01-24T10:43:00" 형태라고 가정
				const paymentDate = item.paymentDate
					? item.paymentDate.substring(0, 10)
					: '';

				const billingStart = item.billingStart || '';
				const billingEnd = item.billingEnd || '';
				const billingPeriod =
					billingStart && billingEnd
						? billingStart + ' ~ ' + billingEnd
						: '';

				return {
					paymentDate: paymentDate,
					totalPrice: formatCurrency(item.totalPrice),
					paymentMethod: mapPaymentMethod(item.paymentMethod, item.cardCompany),
					paymentStat: mapPaymentStat(item.paymentStat),
					billingPeriod: billingPeriod
				};
			});

			grid.resetData(rows);
		})
		.catch(function(err) {
			console.error(err);
		});
});
