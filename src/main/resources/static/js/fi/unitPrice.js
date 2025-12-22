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
let custSearchByEnter = false;


const grid = new tui.Grid({
	  el : document.getElementById('unitprice-grid'),
	data: [],
	rowHeaders: ['rowNum'],
	scrollX: false,
	scrollY: true,
	columns: [
	  { header:'품번', name:'sku' },
	  { header:'품명', name:'skuName' },
	  { header:'단가유형', name:'unitPriceTypeName' },
	  { header:'단가적용일', name:'startDate' },
	  { header:'거래처', name:'custName' },
	  { header:'기준단가', name:'unitPrice' }
	]
});

function getList() {

	const selectParam ={
		companyCode : '0000',
		sku : document.getElementById('selectsku').value,
		skuName : document.getElementById('selectskuname').value,
		unitPriceType : document.getElementById('selectunitpricetype').value,
		custCode : document.getElementById('selectcustcode').value,
		custName : document.getElementById('selectcustname').value
	}
	fetch("/api/fi/unitprice?"+new URLSearchParams(selectParam))
	.then(res => res.json())
	.then(result => {
	  console.log(result); // JSON 데이터 확인

	  grid.resetData(result);
	  grid.refreshLayout();
	})
	.catch(err => console.error("조회 중 오류:", err));
}

function saveUnitPrice(){
	
	const raw = Object.fromEntries(new FormData(document.getElementById("unitPriceForm")));

	const selectParam = {
		companyCode : '0000',
	    sku: raw.insertsku,
	    unitPriceType: raw.insertunitpricetype,
	    startDate: raw.insertstartdate,
	    custCode: raw.insertcustcode,
	    unitPrice: raw.insertunitprice
	};
	
	
	fetch("/api/fi/unitprice/check?" + new URLSearchParams(selectParam))
	.then(res => res.json())
	.then(result => {		
	console.log("response raw:", result);
	console.log("cnt value:", result.cnt, typeof result.cnt);
		if(result.cnt > 0){
			updateUnitPrice(selectParam);	
		}
		else {
			insertUnitPrice(selectParam);
		}
	})
	.catch(err => console.error("조회 중 오류:", err));
}

function insertUnitPrice(data){
	console.log(data);
	fetch("/api/fi/unitprice", {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(data)
	})
	.then(res => res.json())
	.then(result => {
	    alert("등록되었습니다.");
	    getList();
	});
}
function updateUnitPrice(data){
	fetch("/api/fi/unitprice", {
	    method: "PUT",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(data)
	})
	.then(res => res.json())
	.then(result => {
	    alert("수정되었습니다.");
	    getList();
	});
}

function setUnitPriceType(){
	fetch("/api/com/type?groupCode=UNIT_PRICE_TYPE")
	.then(res => res.json())
	.then(result => {
		result.forEach(item => {
			const select = document.querySelector("select[name='insertunitpricetype']");
			const option = document.createElement("option");
			option.value = item.code;
			option.textContent = item.codeName; 
			select.appendChild(option);
		});
		result.forEach(item => {
			const select2 = document.querySelector("select[name='selectunitpricetype']");
			const option = document.createElement("option");
			option.value = item.code;
			option.textContent = item.codeName; 
			select2.appendChild(option);
		});
	})
}

function setSelectedRow(row){
	if (!row) {
	  // 선택 없을 때 초기화
	  resetData();
	  return;
	}
	document.querySelector("input[name='insertsku']").value  = row.sku;
	document.querySelector("input[name='insertskuname']").value  = row.skuName;
	document.querySelector("select[name='insertunitpricetype']").value  = row.unitPriceType;
	document.querySelector("input[name='insertstartdate']").value  = row.startDate;
	document.querySelector("input[name='insertcustcode']").value  = row.custCode;
	document.querySelector("input[name='insertcustname']").value  = row.custName;
	document.querySelector("input[name='insertunitprice']").value  = row.unitPrice;
}
function resetData(){
	document.querySelector("input[name='insertsku']").value  = '';
	document.querySelector("input[name='insertskuname']").value  = '';
	document.querySelector("select[name='insertunitpricetype']").value  = ';'
	document.querySelector("input[name='insertstartdate']").value  = '';
	document.querySelector("input[name='insertcustcode']").value  = ''
	document.querySelector("input[name='insertcustname']").value  = '';
	document.querySelector("input[name='insertunitprice']").value  = '';
}



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
        searchCust(true);
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

document.addEventListener("DOMContentLoaded", function () {


    document.getElementById("btnSearch").addEventListener("click", getList);
	

	document.getElementById("btnSave").addEventListener("click", saveUnitPrice);
	setUnitPriceType();

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
			if (result.length === 1) {
				// 1건이면 바로 선택
				returnOnlyOne(result);  // 모달 JS의 전역 함수 호출
			} else if (result.length > 1) {
				// 여러 건이면 모달 열어서 선택하게
				openCustModal();
			} else {
				showToast('검색 결과가 없습니다.', 'warning');
			}
		} else {
			// 버튼으로 모달 연 경우: 그냥 목록만 보여주면 됨
			openCustModal();
		}
	};

});