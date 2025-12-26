// ==========================
// 공통 DOM 요소
// ==========================
const custCodeSearch  = document.getElementById("custCode");
const custNameSearch  = document.getElementById("custName");

const sumSupplyInput = document.getElementById("sum-supply");
const sumTaxInput    = document.getElementById("sum-tax");
const sumTotalInput  = document.getElementById("sum-total");

const btnOpenCustModal = document.getElementById("btnOpenCustModal"); // 거래처검색 버튼
const btnSave          = document.getElementById("btnSave");      // 수금 저장 버튼 (harp.html에서 id 맞춰 주세요)

// ==========================
// 거래처 모달 관련 요소 (기존 모달 재사용)
// ==========================
const btnCustClose = document.getElementById("btnCustClose");          // 모달 닫기 버튼
const backdrop   = document.querySelector(".modal-layer__backdrop");   // 공통 백드롭
const btnCustSelect = document.getElementById("btnCustSelect");        // 모달 내 '선택' 버튼(있다면)
const companyCode = document.getElementById("loginCompanyCode").value;
// 기존 거래처 모달에서 사용하던 Grid (이미 다른 js에서 생성되어 있다고 가정)
// taxInvoice 화면 등에서 쓰던 전역 custGrid 를 그대로 재사용
const custGrid = window.custGrid;  // 존재하지 않으면 undefined
let custSearchByEnter = false;
// ==========================
// 세금계산서 GRID 생성 (수금 대상 리스트)
// ==========================
const invoiceGrid = new tui.Grid({
  el: document.getElementById("invoice-grid"),
  data: [],
  scrollX: false,
  scrollY: true,
  bodyHeight: "fitToParent",
  rowHeaders: ["checkbox"], // 수금 대상 선택
  columns: [
    { header: "세금계산서번호", name: "invoiceNo", minWidth: 150 },
    { header: "발행일자",       name: "issueDate", align: "center" },
    { header: "공급가액",       name: "totalSupplyPrice", align: "right", formatter: numberFormatter },
    { header: "세액",           name: "totalTaxPrice",    align: "right", formatter: numberFormatter },
    { header: "합계금액",       name: "totalAmount",       align: "right", formatter: numberFormatter },
    { header: "기수금액",       name: "harpAmount",        align: "right", formatter: numberFormatter },
    { header: "미수금액",       name: "remainAmount",      align: "right", formatter: numberFormatter }
  ]
});

// 숫자 포맷터 (세 자리 콤마)
function numberFormatter({ value }) {
  if (value === null || value === undefined || value === "") return "";
  const num = Number(value);
  if (isNaN(num)) return "";
  return num.toLocaleString();
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

function loadInvoiceList() {
  // 1) 입력값 읽을 때 .value 사용 + trim
  const custCode = (custCodeSearch?.value || "").trim();
  if (!custCode) {
    showToast("거래처를 먼저 선택해 주세요.");
    return; // ← 없으면 빈 값으로도 fetch가 호출됩니다.
  }

  const params = new URLSearchParams({
    companyCode: companyCode,  // 로그인 회사코드 (loginCompanyCode hidden에서 가져온 값)
    custCode: custCode
  });

  fetch("/api/fi/harp/invoice?" + params.toString())
    .then(res => res.json())
    .then(data => {
      const list = data || [];
      invoiceGrid.resetData(list);
      invoiceGrid.refreshLayout();

      // 조회 후 합계는 선택된 행 기준으로 다시 계산
      updateSummary();
    })
    .catch(err => {
      console.error("세금계산서 조회 오류:", err);
      alert("세금계산서 조회 중 오류가 발생했습니다.");
    });
}
function updateSummary() {
  const checkedRows = invoiceGrid.getCheckedRows();

  let sumSupply = 0;
  let sumTax    = 0;
  let sumTotal  = 0;

  checkedRows.forEach(row => {
    sumSupply += Number(row.totalSupplyPrice) || 0;
    sumTax    += Number(row.totalTaxPrice)   || 0;
    sumTotal  += Number(row.totalAmount)     || 0;
  });

  if (sumSupplyInput) sumSupplyInput.value = sumSupply.toLocaleString();
  if (sumTaxInput)    sumTaxInput.value    = sumTax.toLocaleString();
  if (sumTotalInput)  sumTotalInput.value  = sumTotal.toLocaleString();
}
function saveHarp() {
  const custCode = (custCodeSearch?.value || "").trim();
  if (!custCode) {
    showToast("거래처를 먼저 선택해 주세요.");
    custCodeSearch.focus();
    return;
  }

  const checkedRows = invoiceGrid.getCheckedRows();
  if (!checkedRows || checkedRows.length === 0) {
    showToast("수금등록할 세금계산서를 선택해 주세요.");
    return;
  }

  // 선택된 행 기준으로 합계 계산 + 상세 리스트 구성
  let totalAmount = 0;
  const detailList = checkedRows.map(row => {
    const amount = Number(row.totalAmount) || 0;  // 이번 수금금액 = 합계금액 전체 (전액수금 기준)
    totalAmount += amount;

    return {
      // HarpDetailVO 기준 필드
      harpDetailNo: null,           // 서버에서 생성
      companyCode: companyCode,
      harpNo: null,                 // 서버에서 생성
      invoiceNo: row.invoiceNo,
      amount: amount,
      createdBy: null,
      createDate: null,
      updatedBy: null,
      updateDate: null
    };
  });

  // 수금일자 → 오늘 날짜 (yyyy-MM-dd)
  const today = new Date().toISOString().slice(0, 10);

  // HarpMasterVO 구조에 맞게 JSON 구성
  const param = {
    companyCode: companyCode,
    harpNo: null,         // 서버에서 채번
    custCode: custCode,
    harpDate: today,
    totalAmount: totalAmount,
    createdBy: null,
    createDate: null,
    updatedBy: null,
    updateDate: null,
    detailList: detailList
  };

  console.log("수금 저장 요청 파라미터:", param);

  fetch("/api/fi/harp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(param)
  })
    .then(res => res.json())
    .then(result => {
      console.log("수금 저장 결과:", result);

      if (result && result.success) {
        showToast("수금정보가 저장되었습니다.");
        // 저장 후 다시 목록 조회해서 미수금 반영
        loadInvoiceList();
      } else {
        const msg = (result && result.message) ? result.message : "수금 저장에 실패했습니다.";
        alert(msg);
      }
    })
    .catch(err => {
      console.error("수금 저장 오류:", err);
      alert("수금 저장 중 오류가 발생했습니다.");
    });
}

document.addEventListener("DOMContentLoaded", function () {
	custCodeSearch.addEventListener('keydown', handleEnter);
	btnOpenCustModal.addEventListener('click', openCustModalOnly);
	btnCustClose.addEventListener('click', closeCustModal);
	window.handleSelectedCust = function(row) {
	    custCodeSearch.value = row.custCode;
		custNameSearch.value = row.custName;

		loadInvoiceList()
	};

    window.afterCustSearch = function(result) {
        const byEnter = custSearchByEnter === true;
        custSearchByEnter = false;

        if (byEnter) {
          if (result.length === 1) {
            returnOnlyOne(result);
          } else if (result.length > 1) {
            openCustModal();
            searchCust(false);
          } else {
            showToast('검색 결과가 없습니다.', 'warning');
          }
        } else {
          openCustModal();
        }
    };

	invoiceGrid.on("check", updateSummary);
	invoiceGrid.on("uncheck", updateSummary);
	invoiceGrid.on("checkAll", updateSummary);
	invoiceGrid.on("uncheckAll", updateSummary);
	
	if (btnSave) {
	  btnSave.addEventListener('click', saveHarp);
	}
  });
  
  
  