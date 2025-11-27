/**
 * payRoll.js
 */

/* 급여대장-상여등록 */
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
	console.log("payrollPeriod:", payrollPeriod);
	console.log("bonusType:", bonusType);
	console.log("bonusValue:",bonusValue);
	console.log("payrollBonusName:",payrollBonusName);
	console.log("payrollStartDate:",payrollStartDate);
	console.log("payrollEndDate:",payrollEndDate);
	console.log("payrollBonusDate:",payrollBonusDate);
	console.log("checkedEmployees:",checkedEmployees);
	console.log("employeeIds:",employeeIds);
	
	// 서버전송할때 보낼 데이터 객체 생성
	const data = {
		payrollPeriod: payrollPeriod, // 귀속연월
		bonusType: bonusType, // 상여지급방법
		bonusRate: (bonusType === 'rate') ? bonusValue : null, // 상여지급율
		bonusAmount: (bonusType === 'amount') ? bonusValue : null, // 상여지급액
		payrollBonusName: payrollBonusName, // 대장명칭
		payrollStartDate: payrollStartDate, // 대장기간시작일
		payrollEndDate: payrollEndDate, // 대장기간종료일
		payrollBonusDate: payrollBonusDate, // 지급일
		peopleNumber: employeeIds.length, // 인원수
		employeeIds: employeeIds // 사원ID배열들
	}
	// ajax를 이용해서 데이터 전송
	fetch('/api/hr/bonusRegister', {
		method: 'POST',
		headers: {
			'Content-Type':'application/json'
		},
		body: JSON.stringify(data)
	})
	.then(response => response.json())
	.then(result => {
		if(result.success) {
			alert('상여 등록이 완료되었습니다.');
		} else {
			alert('등록 실패 : ' + result.message);
		}
	})
	.catch(error => console.error('Error:', error));
})

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
	bodyHeight: 380,
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
// 전체 체크박스를 포함한 모든 체크/언체크 이벤트에 리스너 등록
userGrid.on('check',updatePeopleNumber);
userGrid.on('uncheck',updatePeopleNumber);
userGrid.on('checkAll',updatePeopleNumber);
userGrid.on('uncheckAll',updatePeopleNumber);
// Grid 데이터 로드 완료 하고나서 초기 인원 수 설정
userGrid.on('response',updatePeopleNumber);

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