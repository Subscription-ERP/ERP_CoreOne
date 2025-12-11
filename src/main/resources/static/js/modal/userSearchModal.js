/**
 * userSearchModal.js
 *
 * 설명: 사원 조회 모달
 * 사용법:
 *  1. 부모 화면에서 사원명 input 클릭 시: openHrEmpModal(e) 호출
 *  2. 모달에서 행 선택 시: window.handleSelectedEmp(row) 콜백 호출 (부모에서 정의)
 */

// 모달 및 요소들
const userSearchModal       = document.getElementById('userSearchModal'); // 모달 레이어
const searchUserIdInput     = document.getElementById('userId');          // 사원번호 검색
const searchUserNameInput   = document.getElementById('userName');        // 성명 검색
const btnSearch             = document.getElementById('btnSearch');       // 조회 버튼
const btnResetSearch        = document.getElementById('btnResetSearch');  // 초기화 버튼

let userGrid; // Toast UI Grid 인스턴스

document.addEventListener('DOMContentLoaded', () => {
  const gridEl = document.getElementById('userGrid');
  if (!gridEl) return;

  // === Grid 생성 ===
  userGrid = new tui.Grid({
    el: gridEl,
    rowHeaders: ['rowNum'],
    bodyHeight: 430,
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '사원번호',   name: 'userId',       align: 'center' },
      { header: '이름',       name: 'userName' },
      { header: '부서',       name: 'deptName' },
      { header: '직위/직급',  name: 'jobTitleName' },
      { header: '직책',       name: 'positionName' },
    ],
    data: [],
  });

  // 모달 열리기 전에 미리 전체 목록 한 번 로딩
  loadUserList();

  // 조회 버튼
  if (btnSearch) {
    btnSearch.addEventListener('click', (e) => {
      e.preventDefault();
      searchUsers();
    });
  }

  // 초기화 버튼
  if (btnResetSearch) {
    btnResetSearch.addEventListener('click', (e) => {
      e.preventDefault();
      if (searchUserIdInput)   searchUserIdInput.value = '';
      if (searchUserNameInput) searchUserNameInput.value = '';
      loadUserList(); // 초기화 후 전체조회
    });
  }

  // 엔터키로 조회
  if (searchUserIdInput) {
    searchUserIdInput.addEventListener('keydown', handleEnterForSearch);
  }
  if (searchUserNameInput) {
    searchUserNameInput.addEventListener('keydown', handleEnterForSearch);
  }

  // 그리드 행 클릭 시 선택 처리
  userGrid.on('click', (e) => {
    const rowKey = e.rowKey;
    if (rowKey == null) return;

    const row = userGrid.getRow(rowKey);

    // 부모 페이지에서 정의한 콜백으로 선택된 사원정보 전달
    if (typeof window.handleSelectedEmp === 'function') {
      window.handleSelectedEmp(row);
    }

    closeHrEmpModal();
  });

  // 닫기 버튼
  const btnClose = document.getElementById('btnClose');
  if (btnClose) {
    btnClose.addEventListener('click', closeHrEmpModal);
  }
});

/**
 * 전체 사원 목록 조회 (검색조건 없이)
 */
function loadUserList() {
  if (!userGrid) return;

  // 검색조건 없이 전체 조회
  fetch('/api/hr/user/search')
    .then((res) => res.json())
    .then((result) => {
      userGrid.resetData(result);
      userGrid.refreshLayout();
    })
    .catch((err) => console.error(err));
}

/**
 * 검색 조건으로 사원 조회
 */
function searchUsers() {
  if (!userGrid) return;

  const userId   = (searchUserIdInput?.value || '').trim();
  const userName = (searchUserNameInput?.value || '').trim();

  const params = new URLSearchParams();
  if (userId)   params.append('userId', userId);
  if (userName) params.append('userName', userName);

  const url =
    '/api/hr/user/search' + (params.toString() ? `?${params.toString()}` : '');

  fetch(url)
    .then((res) => res.json())
    .then((result) => {
      userGrid.resetData(result);
      userGrid.refreshLayout();

      // 검색 결과 후 추가 동작이 필요하면 부모에서 afterEmpSearch 정의해서 사용
      if (typeof window.afterEmpSearch === 'function') {
        window.afterEmpSearch(result);
      }
    })
    .catch((err) => console.error(err));
}

/**
 * 검색 input에서 엔터 누르면 조회
 */
function handleEnterForSearch(e) {
  if (e.key === 'Enter') {
    e.preventDefault();
    searchUsers();
  }
}

/**
 * 모달 열기 (부모 js에서 사용: openHrEmpModal(e))
 */
function openHrEmpModal(e) {
  if (e) {
    e.stopPropagation();
    e.preventDefault();
  }

  if (!userSearchModal) return;

  userSearchModal.hidden = false;
  userSearchModal.classList.remove('hidden');

  if (userGrid) {
    userGrid.refreshLayout();
  }
}

/**
 * 모달 닫기
 */
function closeHrEmpModal() {
  if (!userSearchModal) return;

  userSearchModal.hidden = true;
  userSearchModal.classList.add('hidden');
}