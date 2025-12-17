// ==========================
// 공통 DOM 요소
// ==========================
const custCodeSearch  = document.getElementById("custCode");
const custNameSearch  = document.getElementById("custName");
const btnOpenCustModal = document.getElementById("btnOpenCustModal"); // 거래처검색 버튼
// ==========================
// 거래처 모달 관련 요소 (기존 모달 재사용)
// ==========================
const btnCustClose = document.getElementById("btnCustClose");          // 모달 닫기 버튼
const backdrop   = document.querySelector(".modal-layer__backdrop");   // 공통 백드롭
const btnCustSelect = document.getElementById("btnCustSelect");        // 모달 내 '선택' 버튼(있다면)
const companyCode = document.getElementById("loginCompanyCode").value;

const grid = new tui.Grid({
  el: document.getElementById('credit-grid'),
  data: [],
  rowHeaders: ['rowNum'],
  scrollX: false,
  scrollY: true,
  bodyHeight: 'fitToParent',
  columns: [
    { header:'거래처코드', name:'custCode' },
    { header:'거래처명', name:'custName' },
    { header:'여신구분', name:'creditTypeName' },
    { header:'여신한도', name:'creditMax', align:'right'},
    { header:'외상한도일', name:'creditDueDay', align:'right'}
  ]
});



// 모달 열기
function openCustModalOnly(e) {
	
    e.preventDefault();
	openCustModal();
    getCustList();    // 전체 목록 or 필요한 대로

}


// 거래처 모달 검색 결과 전달 받음
function searchCustModal() {
    const custCodeKeyword = custCodeSearch.value.trim();
    // 1) 모달 쪽 검색 키워드 입력
    const schCustCode = document.getElementById('schCustCode');

    if (schCustCode) schCustCode.value = custCodeKeyword;

    // 2) 모달 JS의 검색 함수 호출
    if (typeof searchCust === 'function') {
        custSearchByEnter = true;
        searchCust();
    }
}

function closeCustModal() {
	custModal.hidden = true;
	custModal.classList.add('hidden');
}

// Enter 입력 시 검색
function handleEnter(e) {
    if(e.key === 'Enter') {
        e.preventDefault();
        searchCustModal();
    }
}

function getList(){
const selectParam ={
	companyCode : '0000',
	custCode : document.getElementById('selectcustcode').value,
	custName : document.getElementById('selectcustname').value
}
fetch("/api/fi/credit?"+new URLSearchParams(selectParam))
.then(res => res.json())
.then(result => {
  console.log(result); // JSON 데이터 확인

  grid.resetData(result);
  grid.refreshLayout();
})
.catch(err => console.error("조회 중 오류:", err));
}

function setCreditType(){
	fetch("/api/com/type?groupCode=CREDIT_TYPE")
	.then(res => res.json())
	.then(result => {
		result.forEach(item => {
			const select = document.querySelector("select[name='creditType']");
			const option = document.createElement("option");
			option.value = item.code;
			option.textContent = item.codeName; 
			select.appendChild(option);
		});
	})
}

function saveCredit(){
	const raw = Object.fromEntries(new FormData(document.getElementById("creditForm")));
	const selectParam = {
		companyCode : '0000',
	    custCode: raw.custCode,
	    creditType: raw.creditType,
	    creditMax: raw.creditLimit,
	    creditDueDay: raw.dueDays,
	};

	fetch("/api/fi/credit", {
	    method: "PUT",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(selectParam)
	})
	.then(res => res.json())
	.then(result => {
	    alert("등록되었습니다.");
	    getList();
	});

}

function setSelectedRow(row){
	if (!row) {
	  // 선택 없을 때 초기화
	  resetData();
	  return;
	}
	document.querySelector("input[name='custCode']").value  = row.custCode;
	document.querySelector("input[name='custName']").value  = row.custName;
	document.querySelector("select[name='creditType']").value  = row.creditType;
	document.querySelector("input[name='creditLimit']").value  = row.creditMax;
	document.querySelector("input[name='dueDays']").value  = row.creditDueDay;
}
function resetData(){
	document.querySelector("input[name='custCode']").value  = '';
	document.querySelector("input[name='custName']").value  = '';
	document.querySelector("select[name='creditType']").value  = '';
	document.querySelector("input[name='creditLimit']").value  = '';
	document.querySelector("input[name='dueDays']").value  = '';
}

document.addEventListener("DOMContentLoaded", function () {


	document.getElementById("btnSearch").addEventListener("click", getList);


	setCreditType();
	
	document.getElementById("btnSave").addEventListener("click", saveCredit);
	
	// 행 클릭시 상세정보 보기
	grid.on('click', (ev) => {
	  const rowKey = ev.rowKey;

	  if (rowKey == null) return;

	  const row = grid.getRow(rowKey);
	  setSelectedRow(row)
	});

	custCodeSearch.addEventListener('keydown', handleEnter);
	btnOpenCustModal.addEventListener('click', openCustModalOnly);
	btnCustClose.addEventListener('click', closeCustModal);
	window.handleSelectedCust = function(row) {
	    custCodeSearch.value = row.custCode;
		custNameSearch.value = row.custName;

	};

	window.afterCustSearch = function(result) {
	    const byEnter = custSearchByEnter === true;
	    custSearchByEnter = false;

	    if (byEnter) {
	        returnOnlyOne(result);
	    } else {
	        // 버튼으로 모달을 열어 내부에서 검색한 경우 등: 그냥 모달 보여주기만
	        openCustModal();
	    }
	};


	
	
	
 });