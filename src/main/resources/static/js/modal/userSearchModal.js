/*
 * userSearchModal.js (사원조회)
 */

document.addEventListener('DOMContentLoaded', () => {

  /* ------------------------------------------------------------------
   * 요소/변수
   * ------------------------------------------------------------------ */
  const userSearchModal = document.querySelector('#userSearchModal');     // 모달 레이어
  const userIdSearch    = document.querySelector('#userId-search');       // 사원번호 검색
  const userNameSearch  = document.querySelector('#userName-search');     // 성명 검색
  const btnSearch       = document.querySelector('#btnSearch');           // 조회 버튼
  const btnResetSearch  = document.querySelector('#btnResetSearch');      // 초기화 버튼
  const btnClose        = document.querySelector('#btnClose');            // 닫기 버튼

  let userGrid; // Toast UI Grid 인스턴스

  /* ------------------------------------------------------------------
   * 그리드 생성
   * ------------------------------------------------------------------ */
  const gridEl = document.querySelector('#userGrid');
  if (!gridEl) return;

  userGrid = new tui.Grid({
    el: gridEl,
    rowHeaders: ['rowNum'],
    bodyHeight: 430,
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '사원번호',  name: 'userId',       align: 'center' },
      { header: '이름',      name: 'userName' },
      { header: '부서',      name: 'deptName' },
      { header: '직위/직급', name: 'jobTitleName' },
      { header: '직책',      name: 'positionName' },
    ],
    data: [],
  });

  // 최초 전체조회
  loadUserList();

  /* ------------------------------------------------------------------
   * 이벤트 바인딩
   * ------------------------------------------------------------------ */

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
      if (userIdSearch)   userIdSearch.value   = '';
      if (userNameSearch) userNameSearch.value = '';
      loadUserList();
    });
  }

  // 닫기 버튼
  if (btnClose) {
    btnClose.addEventListener('click', closeUserSearchModal);
  }

  // 그리드 행 클릭 시
  userGrid.on('click', (e) => {
    const rowKey = e.rowKey;
    if (rowKey == null) return;

    const row = userGrid.getRow(rowKey);

    if (typeof window.handleSelectedEmp === 'function') {
      window.handleSelectedEmp(row);
    }

    closeUserSearchModal();
  });

  /* ------------------------------------------------------------------
   * 조회 관련 함수
   * ------------------------------------------------------------------ */

  // 전체 조회
  function loadUserList() {
    if (!userGrid) return;

    fetch('/api/hr/user/search')
      .then((res) => res.json())
      .then((result) => {
        userGrid.resetData(result);
        userGrid.refreshLayout();
      })
      .catch((err) => console.error(err));
  }

  // 조건 조회
  function searchUsers() {
    if (!userGrid) return;

    const userId   = (userIdSearch?.value   || '').trim();
    const userName = (userNameSearch?.value || '').trim();

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
      })
      .catch((err) => console.error(err));
  }


  /* ------------------------------------------------------------------
   * 모달 열기/닫기
   * ------------------------------------------------------------------ */

  function openUserSearchModal(e) {
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

  function closeUserSearchModal() {
    if (!userSearchModal) return;

    userSearchModal.hidden = true;
    userSearchModal.classList.add('hidden');
  }

  /* ------------------------------------------------------------------
   * 전역 export (부모 화면에서 사용)
   * ------------------------------------------------------------------ */
  window.openUserSearchModal  = openUserSearchModal;
  window.closeUserSearchModal = closeUserSearchModal;
  
  
});
