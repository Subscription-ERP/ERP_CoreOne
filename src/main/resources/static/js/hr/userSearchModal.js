/**
 * userSearchModal.js
 */

/*
 * 설명: 사원 모달
 * 사용법
 * 1. 모달 열기: openHrEmpModal()
 * 2. 부모에서 선택값 받기: window.handleSelectedEmp = function(row) { ... }
 */

const hrEmpModal    = document.getElementById('hrEmpModal');
const schEmpId      = document.getElementById('schEmpId');
const schEmpName    = document.getElementById('schEmpName');
const btnHrEmpSearch = document.getElementById('btnHrEmpSearch');
let hrEmpModalGrid;

document.addEventListener('DOMContentLoaded', () => {
  const gridEl = document.getElementById('hrEmpModalGrid');
  if (!gridEl) return;

  const Grid = tui.Grid;
  Grid.applyTheme('clean');

  hrEmpModalGrid = new Grid({
    el: gridEl,
    rowHeaders: ['rowNum'],
    bodyHeight: 430,
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '사번',   name: 'userId', align: 'center' },
      { header: '이름',   name: 'userName' },
      { header: '부서',   name: 'deptName' },
      { header: '직급',   name: 'jobTitleName' },
    ],
    data: [],
  });

  getEmpList();

  btnHrEmpSearch.addEventListener("click", function (e) {
    e.preventDefault();
    searchEmp();
  });

  schEmpId.addEventListener('keydown', handleEnter);
  schEmpName.addEventListener('keydown', handleEnter);

  hrEmpModalGrid.on('click', (e) => {
    const rowKey = e.rowKey;
    if (rowKey == null) return;

    const row = hrEmpModalGrid.getRow(rowKey);

    // 부모에서 정의한 전역함수에게 선택 값 전달
    if (typeof window.handleSelectedEmp === 'function') {
      window.handleSelectedEmp(row);
    }

    closeHrEmpModal();
  });

  // 닫기 버튼
  const btnClose = document.getElementById('btnHrEmpClose');
  if (btnClose) {
    btnClose.addEventListener('click', closeHrEmpModal);
  }
});

// 사원 목록 로딩
function getEmpList() {
  if (!hrEmpModalGrid) return;

  fetch('/api/hr/empList')   // <- 실제 URL에 맞게 수정
    .then(res => res.json())
    .then(result => {
      hrEmpModalGrid.resetData(result);
    })
    .catch(err => console.error(err));
}

// 사원 검색
function searchEmp() {
  const userId   = schEmpId.value.trim();
  const userName = schEmpName.value.trim();

  const params = { userId, userName };

  if (!hrEmpModalGrid) return;

  fetch('/api/hr/searchEmp', {   // <- 실제 URL에 맞게 수정
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(params)
  })
    .then(res => res.json())
    .then(result => {
      hrEmpModalGrid.resetData(result);
      hrEmpModalGrid.refreshLayout();

      if (typeof window.afterEmpSearch === 'function') {
        window.afterEmpSearch(result);
      }
    })
    .catch(err => console.error(err));
}

function handleEnter(e) {
  if (e.key === 'Enter') {
    e.preventDefault();
    searchEmp();
  }
}

// 모달 열기
function openHrEmpModal(e) {
  if (e) {
    e.stopPropagation();
    e.preventDefault();
  }
  hrEmpModal.hidden = false;
  hrEmpModal.classList.remove('hidden');

  if (hrEmpModalGrid) {
    hrEmpModalGrid.refreshLayout();
  }
}

// 모달 닫기
function closeHrEmpModal() {
  hrEmpModal.hidden = true;
  hrEmpModal.classList.add('hidden');
}
