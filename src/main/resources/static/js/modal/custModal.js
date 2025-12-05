/*
* 설명: 거래처 모달
* 작성자: 박봉근
* 작성일자: 2025-12-04
* 수정이력
*   2025-12-04: 불필요한 window처리 삭제
*
* 사용법
* 1. 모달 열기: openCustModal()
*
* 2. 거래처 정보 부르기: getCustList()
*
* 3. 검색: searchCust()
* 3-1. 결과 값 가지고 오기
*   a. 부모에서 지역 함수로 정의: window.handleSelectedCust = function(row) {}
*   b. 값 가지고 오기: (input id).value = row.custCode / (input id).value = row.custName
*   c. 부모에서 정의: afterCustSearch
* 3-2. 커스텀
*   a. 부모에서 지역함수로 정의: window.afterCustSearch = function(result) {}
*   b. 값이 1개일 때 바로 return: returnOnlyOne(result)
*
* 4. 모달 닫기: closeCustModal()
*
* */


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
  if (!custModalGrid) return;

  fetch('/api/sd/custList')
    .then(res => res.json())
    .then(result => {
      custModalGrid.resetData(result);
    })
    .catch(err => console.error(err));
}

// 거래처 검색
function searchCust() {
  const custCode = document.getElementById('schCustCode').value.trim();
  const custName = document.getElementById('schCustName').value.trim();

  const params = { custCode, custName };

  if (!custModalGrid) return;

  fetch('/api/sd/searchCust', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(params)
  })
      .then(res => res.json())
      .then(result => {
        custModalGrid.resetData(result);
        custModalGrid.refreshLayout();

        // 부모에 값 넘기는 함수
        if (typeof window.afterCustSearch === 'function') {
          window.afterCustSearch(result);
        }
      })
      .catch(err => console.error(err));
}

// Enter 입력 시 검색
function handleEnter(e) {
  if(e.key === 'Enter') {
    e.preventDefault();
    searchCust();
  }
}

function returnOnlyOne(result) {
  if (result.length === 1) {
    const row = result[0];

    if (typeof handleSelectedCust === 'function') {
      handleSelectedCust(row);
    }
    closeCustModal();
  } else {
    // 여러 건이면 모달 띄워서 사용자 선택
    openCustModal();
  }
}

// 거래처 모달 열기
function openCustModal(e) {
  if (e) {
    e.stopPropagation();
    e.preventDefault();
  }
  custModal.hidden = false;
  custModal.classList.remove('hidden');

  if (custModalGrid) {
    custModalGrid.refreshLayout();
  }
}

// 모달 닫기
function closeCustModal() {
  if (!custModalGrid) return;

  custModal.hidden = true;
  custModal.classList.add('hidden');
}



