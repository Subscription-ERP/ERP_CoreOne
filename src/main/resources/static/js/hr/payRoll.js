/**
 * payRoll.js
 */

document.addEventListener('DOMContentLoaded', () => {
	/* 급여대장-상여등록-초기화버튼 */
	document.querySelector('#btnReset').addEventListener('click', function() {
		document.querySelector('#payrollPeriod').value = '';
		/* 상여지급방법 초기화 좀있다가 */
		document.querySelector('#bonus').value = '';
		document.querySelector('#payrollBonusName').value = '';
		document.querySelector('#payrollStartDate').value = '';
		document.querySelector('#payrollEndDate').value = '';
		document.querySelector('#payrollBonusDate').value = '';
	})

	/* 급여대장-대장조회-초기화버튼 */
	document.querySelector('#btnPayrollReset').addEventListener('click', function() {
		document.querySelector('#payrollName').value = '';
		document.querySelector('#payrollDateStartDate').value = '';
		document.querySelector('#payrollDateEndDate').value = '';
	})

	/* 급여대장-대장조회버튼 */
	document.querySelector('#btnPayrollSearch').addEventListener('click', function() {

	})

	/* 급여대장-상여등록-초기화버튼 기능 */
	document.querySelector('#btnEmpReset').addEventListener('click', function() {
		document.querySelector('#payrollUserName').value = '';
		document.querySelector('#payrollDeptName').value = '';
		document.querySelector('#payrollEmpStartDate').value = '';
		document.querySelector('#payrollEmpEndDate').value = '';
	})

	// 전역변수로 설정.
	let checkedUserIds = [];

	/* 급여대장-상여등록-사원조회 */
	const userGrid = new tui.Grid({
		el: document.getElementById('userGrid'),
		scrollX: false,
		scrollY: true,
		rowHeaders: ['checkbox'],
		data: {
			api: {
				readData: {
					url: '/api/hr/payrollEmpList',
					method: 'GET'
				}
			}
		},
		bodyHeight: 260,
		rowKey: 'userId',
		columns: [{
			header: '사원번호',
			name: 'userId',
			align: 'center',
			width: 200
		}, {
			header: '성명',
			name: 'userName'
		}, {
			header: '부서명',
			name: 'dept'
		}, {
			header: '입사일',
			name: 'hireDate',
			align: 'center'
		}, {
			header: '직위',
			name: 'jobTitle'
		}]
	});

	/* 급여대장-상여등록-사원조회-조회버튼 */
	document.getElementById('btnPayrollEmpSearch').addEventListener('click', function() {
		// 성명
		const payrollUserName = document.querySelector('#payrollUserName').value;
		// 부서명
		const payrollDeptName = document.querySelector('#payrollDeptName').value;
		// 입사일 범위 시작일
		const payrollEmpStartDate = document.querySelector('#payrollEmpStartDate').value;
		// 입사일 범위 종료일
		const payrollEmpEndDate = document.querySelector('#payrollEmpEndDate').value;
		// 조회 할 검색조건 데이터 객체 생성
		const data = {
			userName: payrollUserName,
			dept: payrollDeptName,
			payrollEmpStartDate: payrollEmpStartDate,
			payrollEmpEndDate: payrollEmpEndDate
		}

		// 조건 조회 실행을 한다
		// 이 메서드가 '/api/hr/payrollEmpList?userName=...&dept=...' 형태로 요청을 보냄
		userGrid.readData(1, data, true);
	})

	// Grid 데이터 로드가 완료되면은 발생하는 이벤트 리스너를 등록한다.
	userGrid.on('response', function(ev) {

		// ⏱️ Grid 렌더링 후 실행되도록 0ms 지연 적용 (필수 유지)
		setTimeout(function() {
			if (checkedUserIds.length > 0) {
				console.log('복원 대상 ID:', checkedUserIds)

				const gridData = userGrid.getData();

				gridData.forEach(function(row) {
					// 만약에 이전에 저장한 ID가 현재 조회된 데이터에 포함되어 있다면
					if (checkedUserIds.includes(row.userId)) {
						// 2. 해당 행을 체크합니다.
						userGrid.check(row.rowKey);
					}
				});
			}

			// 3. 인원수 업데이트
			updatePeopleNumber();
		}, 0);
	})

	/* 
	 * 급여대장-상여등록
	 * 저장버튼을 누르면 상여등록이 이루어진다.
	 */
	document.getElementById('btnSave').addEventListener('click', function() {
		// 1. 데이터 수집
		// 귀속연월
		const payrollPeriod = document.getElementById('payrollPeriod').value;
		// 상여지급방법
		const bonusType = document.querySelector('input[name="bonusType"]:checked').value;
		// 지급률 및 지급액
		const bonusValue = document.querySelector('#bonus').value;
		// 대장명칭
		const payrollBonusName = document.querySelector('#payrollBonusName').value;
		// 대장기간시작일
		const payrollStartDate = document.querySelector('#payrollStartDate').value;
		// 대장기간종료일
		const payrollEndDate = document.querySelector('#payrollEndDate').value;
		// 지급일
		const payrollBonusDate = document.querySelector('#payrollBonusDate').value;
		// 선택된 사원 ID 목록 추출
		const checkedEmployees = userGrid.getCheckedRows();
		// 사원 객체 배열에서 'userId'필드만 추출
		const employeeIds = checkedEmployees.map(row => row.userId);
		/*console.log("payrollPeriod:", payrollPeriod);
		console.log("bonusType:", bonusType);
		console.log("bonusValue:", bonusValue);
		console.log("payrollBonusName:", payrollBonusName);
		console.log("payrollStartDate:", payrollStartDate);
		console.log("payrollEndDate:", payrollEndDate);
		console.log("payrollBonusDate:", payrollBonusDate);
		console.log("checkedEmployees:", checkedEmployees);
		console.log("employeeIds:", employeeIds);*/

		// 서버전송할때 보낼 데이터 객체 생성
		const data = {
			payrollPeriod: payrollPeriod, // 귀속연월
			bonusMethod: bonusType, // 상여지급방법
			bonusRate: (bonusType === 'rate') ? bonusValue : null, // 상여지급율
			bonusAmount: (bonusType === 'amount') ? bonusValue : null, // 상여지급액
			payrollName: payrollBonusName, // 대장명칭
			payrollStartDate: payrollStartDate, // 대장기간시작일
			payrollEndDate: payrollEndDate, // 대장기간종료일
			payrollDate: payrollBonusDate, // 지급일
			peopleNumber: employeeIds.length, // 인원수
			employeeIds: employeeIds // 사원ID배열들
		}
		// ajax를 이용해서 데이터 전송
		fetch('/api/hr/bonusRegister', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(data)
		})
			.then(response => response.json())
			.then(result => {
				if (result.success) {
					alert(result.count + '건의 상여 등록이 완료되었습니다.');
				} else {
					alert('등록 실패 : ' + result.message);
				}
			})
			.catch(error => console.error('Error:', error));
	})

	/* 인원수 반영 */
	// 인원수 태그
	const bonusPeopleNumberInpur = document.querySelector('#bonusPeopleNumber');
	// Grid에서 체크박스 상태 변경될때마다 실행하는 함수
	function updatePeopleNumber() {
		// getCheckedRows()를 이용해서 현재 체크된 모든 행 데이터를 가져온
		const checkedRows = userGrid.getCheckedRows();
		// 가져온 데이터의 개수를 필드에 반영
		bonusPeopleNumberInpur.value = checkedRows.length;
	}

	// 개별 체크/언체크 이벤트
	userGrid.on('check', function(ev) {
		const userId = ev.rowKey;
		if (!checkedUserIds.includes(userId)) {
			checkedUserIds.push(userId); // 누적 추가
		}
		updatePeopleNumber();
	});

	userGrid.on('uncheck', function(ev) {
		const userId = ev.rowKey;
		const index = checkedUserIds.indexOf(userId);
		if (index > -1) {
			checkedUserIds.splice(index, 1); // 제거
		}
		updatePeopleNumber();
	});

	// 전체 체크/언체크 이벤트 (전체 사원 목록이 checkedUserIds에 반영되어야 함)
	userGrid.on('checkAll', function(ev) {
		const gridData = userGrid.getData();
		gridData.forEach(row => {
			if (!checkedUserIds.includes(row.userId)) {
				checkedUserIds.push(row.userId); // 현재 Grid의 모든 ID를 누적 추가
			}
		});
		updatePeopleNumber();
	});

	userGrid.on('uncheckAll', function(ev) {
		const gridData = userGrid.getData();
		gridData.forEach(row => {
			const index = checkedUserIds.indexOf(row.userId);
			if (index > -1) {
				checkedUserIds.splice(index, 1); // 현재 Grid의 모든 ID를 제거
			}
		});
		updatePeopleNumber();
	});


	/* 급여대장-조회 */
	const payrollGrid = new tui.Grid(
		{
			el: document.getElementById('payrollGrid'),
			scrollX: false,
			scrollY: true,

			data: {
				api: {
					readData: {
						url: '/api/hr/payrollList',
						method: 'GET'
					}
				}
			},
			bodyHeight: 570,
			columns: [
				{
					header: '귀속연월',
					name: 'payrollPeriod',
					align: 'center',
					width: 80
				},
				{
					header: '급여구분',
					name: 'payrollType',
					width: 70
				},
				{
					header: '대장명칭',
					name: 'payrollName',
					width: 200
				},
				{
					header: '지급일',
					name: 'payrollDate',
					align: 'center'
				},
				{
					header: '인원수',
					name: 'peopleNumber',
					width: 70
				},
				{
					header: '급여계산',
					name: 'salaryCalculation',
					align: 'center',
					formatter: function(data) {
						const payrollCode = data.row.payrollCode;
						return `<a href=# data-paroll-code="${payrollCode}">계산하기</a>`;
					}
				}, {
					header: '지급총액',
					name: 'totalAmount'
				}]
		});
});