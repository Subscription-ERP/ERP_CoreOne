// ==========================
// 공통 DOM 요소
// ==========================
const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const custCodeSearch   = document.getElementById('custCode');
const custNameSearch   = document.getElementById('custName');
const btnCustClose     = document.getElementById('btnCustClose');
const backdrop         = document.querySelector(".modal-layer__backdrop");
const btnHistorySearch        = document.getElementById('btnHistorySearch');

let searchCustCode    = '';
let custSearchByEnter = false;

// ==========================
// GRID 생성
// ==========================
const grid = new tui.Grid({
  el: document.getElementById('taxInvoiceGrid'),
  data: [],
  scrollX: false,
  scrollY: true,
  bodyHeight: 'fitToParent',
  rowHeaders: ['checkbox'],

  columns: [
    { header:'발행일자',   name:'issueDate',       width:120 },
    { header:'세금일자',   name:'documentDate',    width:120 },
    { header:'일련번호',   name:'invoiceNo',       width:150 },
    { header:'거래처코드', name:'custCode',        hidden: true },
    { header:'거래처',     name:'custName',        width:120 },
    { header:'공급가액',   name:'totalSupplyPrice', align:'right', width:130, formatter:numberFormatter },
    { header:'세액',       name:'totalTaxPrice',    align:'right', width:130, formatter:numberFormatter },
    { header:'합계금액',   name:'totalAmount',      align:'right', width:130, formatter:numberFormatter }
  ]
});

// 숫자 포맷터(세 자리 콤마)
function numberFormatter({ value }) {
  if (value === null || value === undefined || value === '') return '';
  const num = Number(value);
  if (isNaN(num)) return '';
  return num.toLocaleString();
}

// ==========================
// 거래처 모달 관련
// ==========================
function openCustModalOnly(e) {
  e.preventDefault();
  openCustModal();   // 기존 공통 함수
  getCustList();     // 전체 목록 or 필요한 대로
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
  if (e.key === 'Enter') {
    e.preventDefault();
    searchCustModal();
  }
}

// ==========================
// 세금계산서 발행현황 조회
// ==========================
function loadTaxInvoiceHistory() {

  const companyCode = document.getElementById("loginCompanyCode").value;
  const fromDate    = document.getElementById("fromDate").value;
  const toDate      = document.getElementById("toDate").value;
  const custCode    = (custCodeSearch.value || '').trim();

  if (!fromDate || !toDate) {
    alert("발행일자 From/To 를 입력해 주세요.");
    return;
  }

  const params = new URLSearchParams({
    companyCode: companyCode,
    fromDate: fromDate,
    toDate: toDate
  });

  if (custCode) {
    params.append("custCode", custCode);
  }

  fetch("/api/fi/taxinvoicehistory?" + params.toString())
    .then(res => res.json())
    .then(list => {
      grid.resetData(list || []);
    })
    .catch(err => {
      console.error("세금계산서 발행현황 조회 오류:", err);
      alert("세금계산서 발행현황 조회 중 오류가 발생했습니다.");
    });
}
function handlePrint() {

  const checkedRows = grid.getCheckedRows();

  if (!checkedRows || checkedRows.length === 0) {
    alert("출력할 세금계산서를 선택해 주세요.");
    return;
  }

  // 여러 건 선택 가능 → invoiceNo 기반으로 출력
  const invoiceNos = checkedRows.map(r => r.invoiceNo);

  // 새로운 창을 여는 방식 (기본적인 세금계산서 출력 방식)
  const url = "/fi/taxinvoice/print?invoiceNos=" + encodeURIComponent(invoiceNos.join(","));

  window.open(url, "_blank");
}
// ==========================
// DOM 로드 후 초기화
// ==========================
document.addEventListener("DOMContentLoaded", function () {
  const companyCodeEl = document.getElementById("loginCompanyCode");

  // 기본 발행일자 = 오늘
  const today = new Date().toISOString().split("T")[0];
  document.getElementById("fromDate").value = today;
  document.getElementById("toDate").value   = today;

  // 거래처 선택 콜백 (공통 모달에서 호출)
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

  // 이벤트 바인딩
  custCodeSearch.addEventListener('keydown', handleEnter);
  btnOpenCustModal.addEventListener('click', openCustModalOnly);
  btnCustClose.addEventListener('click', closeCustModal);
  backdrop.addEventListener('click', closeCustModal);
  document.getElementById("btnPrint").addEventListener("click", handlePrint);
  if (btnHistorySearch) {
    btnHistorySearch.addEventListener('click', loadTaxInvoiceHistory);
  }
});
