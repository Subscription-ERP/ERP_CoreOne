/* sbManage.js */

let selectedRowKey = null;
let companyGrid;
let subscribeGrid;

// 검색 버튼 클릭 시 호출할 함수
function searchCompany() {
	const companyName = document.getElementById('searchCompanyName').value;
	const ceoName = document.getElementById('searchCeoName').value;
	loadCompanyList(companyName, ceoName);
}

// 회사 목록(전체 + 검색) 조회
function loadCompanyList(companyName, ceoName) {
	const params = new URLSearchParams();

	if (companyName && companyName.trim() !== '') {
		params.append("companyName", companyName.trim());
	}
	if (ceoName && ceoName.trim() !== '') {
		params.append("ceoName", ceoName.trim());
	}

	const queryString = params.toString() ? ("?" + params.toString()) : "";

	fetch('/api/manage/list' + queryString)
		.then(res => res.json())
		.then(data => {
			companyGrid.resetData(data);
		})
		.catch(err => {
			console.error(err);
			alert('회사 목록 조회 중 오류가 발생했습니다.');
		});
}

// 페이지가 모두 로딩된 뒤 실행
document.addEventListener('DOMContentLoaded', function() {
	initCompanyGrid();
	initSubscribeGrid();

	// 페이지 진입 시 전체 조회
	loadCompanyList('', '');

	// 회사명 / 대표자명 인풋에서 Enter 눌렀을 때도 검색되게
	const companyNameInput = document.getElementById('searchCompanyName');
	const ceoNameInput = document.getElementById('searchCeoName');

	[companyNameInput, ceoNameInput].forEach(function(input) {
		if (input) {
			input.addEventListener('keydown', function(e) {
				if (e.key === 'Enter') {
					searchCompany();
				}
			});
		}
	});
});

// 회사 목록 Grid 초기화
function initCompanyGrid() {
	companyGrid = new tui.Grid({
		el: document.getElementById('companyGrid'),
		rowHeaders: ['checkbox'],
		bodyHeight: 400,
		columns: [
			{ name: 'companyName', header: '회사명' },
			{ name: 'ceoName', header: '대표자' },
			{ name: 'ceoPhone', header: '대표자번호' },
			{ name: 'companyEmail', header: '회사이메일' },
			{ name: 'managerName', header: '담당자' },
			{ name: 'managerPhone', header: '담당자 번호' },
			{ name: 'subsStatusName', header: '구독상태' },
			{
				name: 'actions',
				header: '구독이력',
				width: 120,
				align: 'center',
				formatter: function() {
					return '<button type="button">조회</button>';
				}
			}
		],
		data: []
	});


	// ✅ 체크박스 선택 시 회사 상세 가져오기
	companyGrid.on('check', function(ev) {
		const rowKey = ev.rowKey;	//행번호지정 지역변수
		selectedRowKey = rowKey;     // 🔥 전역 변수에 저장 (수정 기능에서 사용!)

		// 1. 현재 체크된 row들의 key 가져오기
		const checkedRowKeys = companyGrid.getCheckedRowKeys();

		// 2. 방금 체크한 rowKey를 제외하고 전부 uncheck
		checkedRowKeys.forEach(function(key) {
			if (key !== rowKey) {
				companyGrid.uncheck(key);
			}
		});
		const rowData = companyGrid.getRow(ev.rowKey);	//행번호로 데이터 꺼내기 
		if (rowData && rowData.companyCode) {
			loadCompanyDetail(rowData.companyCode);
		}
	});
	// actions 컬럼 클릭 시 모달 열기
	companyGrid.on('click', function(ev) {
		const columnName = ev.columnName;

		if (columnName === 'actions') {
			const rowData = companyGrid.getRow(ev.rowKey);
			if (rowData) {
				openSubscribeModal(rowData);
			}
		}
	});
}

// ✅ 특정 회사의 상세 정보 조회
function loadCompanyDetail(companyCode) {
	console.log('loadCompanyDetail 호출됨, companyCode:', companyCode);

	if (!companyCode) return;

	const url = '/api/manage/companyDetail?companyCode=' + encodeURIComponent(companyCode);

	fetch(url)
		.then(function(response) {
			if (!response.ok) {
				throw new Error('회사 상세 조회 실패 (status: ' + response.status + ')');
			}
			return response.json();
		})
		.then(function(data) {
			// 상세 섹션 보이기
			const section = document.getElementById('companyDetailSection');
			if (section) {
				section.hidden = false;
			}

			// 각 필드에 값 바인딩 (null 방지용 || '')
			setInputValue('detailCompanyCode', data.companyCode);
			setInputValue('detailCompanyName', data.companyName);
			setInputValue('detailCeoName', data.ceoName);
			setInputValue('detailBizNo', data.bno);
			setInputValue('detailTel', data.ceoPhone);
			setInputValue('detailManagerTel', data.managerPhone);
			setInputValue('detailEmail', data.companyEmail);
			setInputValue('detailManagerName', data.managerName);
			setInputValue('detailAddress', data.companyAddress);
		})
		.catch(function(err) {
			console.error(err);
			alert('회사 상세 정보를 불러오는 중 오류가 발생했습니다.');
		});
}

// ✅ 상세값 세팅 공통 함수
function setInputValue(id, value) {
	const el = document.getElementById(id);
	if (el) {
		el.value = value != null ? value : '';
	}
}

//회사 정보 수정
document.getElementById('btnCompanySave').addEventListener('click', updateCompany);
function updateCompany() {

	// 1️⃣ 수정할 rowKey 선택됐는지 확인
	if (selectedRowKey === null) {
		alert('수정할 회사를 먼저 선택하세요.');
		return;
	}

	// 2️⃣ input 값 읽어 payload 만들기
	const payload = {
		companyCode: document.getElementById('detailCompanyCode').value,
		companyName: document.getElementById('detailCompanyName').value,
		ceoName: document.getElementById('detailCeoName').value,
		bno: document.getElementById('detailBizNo').value,       
		ceoPhone: document.getElementById('detailTel').value,         
		managerPhone: document.getElementById('detailManagerTel').value,  
		companyEmail: document.getElementById('detailEmail').value,       
		managerName: document.getElementById('detailManagerName').value,
		companyAddress: document.getElementById('detailAddress').value     
	};

	// 3️⃣ 서버로 수정 요청 (URL은 네 API 주소에 맞게 변경)
	fetch('/api/manage/company', {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(payload)
	})
		.then(res => res.json())
		.then(() => {              // ← data 안 받음
			alert('저장되었습니다.');

			companyGrid.setRow(selectedRowKey, {
				...companyGrid.getRow(selectedRowKey),
				...payload
			});
		})
		.catch(err => {
			console.error(err);
			alert('오류가 발생했습니다.');
		});
}

// 구독 이력 Grid 초기화
function initSubscribeGrid() {
	subscribeGrid = new tui.Grid({
		el: document.getElementById('subscribeGrid'),
		rowHeaders: ['rowNum'],
		bodyHeight: 300,
		columns: [
			{ name: 'planCode', header: '플랜명', minWidth: 150 },
			{ name: 'subsStart', header: '구독시작일', width: 120 },
			{ name: 'subsEnd', header: '구독종료일', width: 120 },
			{ name: 'currentPrice', header: '가격', width: 100 },
			{ name: 'currentUserCount', header: '사용자수', width: 100 },
			{ name: 'subsStatus', header: '구독상태', width: 100 },
			{ name: 'inactiveReasonName', header: '비고', width: 100 }
		],
		data: []
	});
}

// 구독 이력 모달 열기
function openSubscribeModal(company) {
	document.getElementById('modalComCode').innerText = company.companyCode || '';	// 모달의 회사코드 출력
	document.getElementById('modalComName').innerText = company.companyName || '';	// 모달의 회사명

	const modal = document.getElementById('subscribeModal');
	modal.removeAttribute('hidden');

	loadSubscribeData(company.companyCode);
}

// 특정 회사의 구독 이력 조회
function loadSubscribeData(companyCode) {
	const url = '/api/manage/subscribes?companyCode=' + encodeURIComponent(companyCode);

	fetch(url)
		.then(function(response) {
			if (!response.ok) {
				throw new Error('구독 이력 조회 실패 (status: ' + response.status + ')');
			}
			return response.json();
		})
		.then(function(data) {
			subscribeGrid.resetData(data);
			subscribeGrid.refreshLayout();
		})
		.catch(function(err) {
			console.error(err);
			alert('구독 이력을 불러오는 중 오류가 발생했습니다.');
		});
}

// 구독 이력 모달 닫기
function closeSubscribeModal() {
	const modal = document.getElementById('subscribeModal');
	modal.setAttribute('hidden', true);
	subscribeGrid.resetData([]);
}
