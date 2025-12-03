// 전역 변수
const custModal = document.getElementById('custModal');
const schCustCode = document.getElementById('schCustCode');
const schCustName = document.getElementById('schCustName');
const btnSearch = document.getElementById("btnCustSearch");
let custModalGrid;

// 모달 실행
document.addEventListener('DOMContentLoaded', () => {
  const gridEl = document.getElementById('custModalGrid');
  if (!gridEl) return;

  const Grid = tui.Grid;
  Grid.applyTheme('clean');

  custModalGrid = new Grid({
    el: gridEl,
    rowHeaders: ['rowNum'],
    bodyHeight: 430,
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '거래처코드', name: 'custCode' },
      { header: '거래처명', name: 'custName' },
      { header: '대표자명', name: 'ceoName' },
      { header: '거래처유형  ', name: 'custType' },
    ],
    data: [],
  });

  getCustList();
  window.custModalGrid = custModalGrid;

  btnSearch.addEventListener("click", function (e) {
    e.preventDefault();
    searchCust();
  });

  schCustCode.addEventListener('keydown', handleEnter);
  schCustName.addEventListener('keydown', handleEnter);

  custModalGrid.on('click', (e) => {
    const rowKey = e.rowKey;
    if (rowKey == null) return;

    const row = custModalGrid.getRow(rowKey);

    // 전역 함수로 handleSelectedCust = function(row) {} 선언시 선택한 row 값을 내보냄
    if (typeof window.handleSelectedCust === 'function') {
      window.handleSelectedCust(row);
    }

    closeCustModal();

  })
});

// 함수 영역 (전역함수)

// 거래처 정보 불러오기
function getCustList() {
  if (!window.custModalGrid) return;

  fetch('/api/sd/cust')
    .then(res => res.json())
    .then(result => {
      window.custModalGrid.resetData(result);
    })
    .catch(err => console.error(err));
}

// 거래처 검색
function searchCust() {
  const custCode = document.getElementById('schCustCode').value.trim();
  const custName = document.getElementById('schCustName').value.trim();

  const params = { custCode, custName };

  if (!window.custModalGrid) return;

  fetch('/api/sd/searchCust', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(params)
  })
      .then(res => res.json())
      .then(result => {
        window.custModalGrid.resetData(result);

        if (result.length === 1) {
          const row = result[0];

          // 부모에 값 넘기는 함수
          if (typeof handleSelectedCust === 'function') {
            handleSelectedCust(row);
          }
          closeCustModal();
        } else {
          openCustModal();
        }

        window.custModalGrid.refreshLayout();
      })
      .catch(err => console.error(err));
}

// Enter 입력 시 검색
function handleEnter(e) {
  if(e.key === 'Enter') {
    e.preventDefault();
    if (btnSearch) btnSearch.click();
  }
}

// 거래처 모달
function openCustModal(e) {
  if (e) {
    e.stopPropagation();
    e.preventDefault();
  }
  custModal.hidden = false;
  custModal.classList.remove('hidden');

  if (window.custModalGrid) {
    window.custModalGrid.refreshLayout();
  }
}

// 모달 닫기
function closeCustModal() {
  if (!window.custModalGrid) return;

  custModal.hidden = true;
  custModal.classList.add('hidden');
}



