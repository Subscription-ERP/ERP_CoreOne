/* sbManage.js */

let companyGrid;
let subscribeGrid;

// 검색 버튼 클릭 시 호출할 함수
function searchCompany() {
  const companyName = document.getElementById('searchCompanyName').value;
  const ceoName = document.getElementById('searchCeoName').value;
  loadCompanyList(companyName, ceoName);
}

// 회사 목록(전체 + 검색) 조회
function loadCompanyList(companyName, ceoName) {
  const params = new URLSearchParams();

  if (companyName && companyName.trim() !== '') {
    params.append("companyName", companyName.trim());
  }
  if (ceoName && ceoName.trim() !== '') {
    params.append("ceoName", ceoName.trim());
  }

  const queryString = params.toString() ? ("?" + params.toString()) : "";

  fetch('/api/manage/list' + queryString)
    .then(res => res.json())
    .then(data => {
      companyGrid.resetData(data);
    })
    .catch(err => {
      console.error(err);
      alert('회사 목록 조회 중 오류가 발생했습니다.');
    });
}

// 페이지가 모두 로딩된 뒤 실행
document.addEventListener('DOMContentLoaded', function() {
  initCompanyGrid();
  initSubscribeGrid();

  // 페이지 진입 시 전체 조회
  loadCompanyList('', '');

  // 회사명 / 대표자명 인풋에서 Enter 눌렀을 때도 검색되게
  const companyNameInput = document.getElementById('searchCompanyName');
  const ceoNameInput = document.getElementById('searchCeoName');

  [companyNameInput, ceoNameInput].forEach(function(input) {
    if (input) {
      input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
          searchCompany();
        }
      });
    }
  });
});

// 회사 목록 Grid 초기화
function initCompanyGrid() {
  companyGrid = new tui.Grid({
    el: document.getElementById('companyGrid'),
    rowHeaders: ['rowNum'],
    bodyHeight: 400,
    columns: [
      { name: 'companyName',    header: '회사명',       width: 120 },
      { name: 'ceoName',        header: '대표자',       minWidth: 200 },
      { name: 'ceoPhone',       header: '대표자번호' },
      { name: 'companyEmail',   header: '회사이메일' },
      { name: 'managerName',    header: '담당자' },
      { name: 'managerPhone',   header: '담당자 번호' },
      { name: 'subsStatusName', header: '구독상태' },
      {
        name: 'actions',
        header: '구독이력',
        width: 120,
        align: 'center',
        formatter: function() {
          return '<button type="button" class="btn-subscribe">조회</button>';
        }
      }
    ],
    data: []
  });

  // actions 컬럼 클릭 시 모달 열기
  companyGrid.on('click', function(ev) {
    const columnName = ev.columnName;

    if (columnName === 'actions') {
      const rowData = companyGrid.getRow(ev.rowKey);
      if (rowData) {
        openSubscribeModal(rowData);
      }
    }
  });
}

// 구독 이력 Grid 초기화
function initSubscribeGrid() {
  subscribeGrid = new tui.Grid({
    el: document.getElementById('subscribeGrid'),
    rowHeaders: ['rowNum'],
    bodyHeight: 300,
    columns: [
      { name: 'planCode',          header: '플랜명',     minWidth: 150 },
      { name: 'subsStart',         header: '구독시작일', width: 120 },
      { name: 'subsEnd',           header: '구독종료일', width: 120 },
      { name: 'currentPrice',      header: '가격',       width: 100 },
      { name: 'currentUserCount',  header: '사용자수',   width: 100 },
      { name: 'subsStatus',        header: '구독상태',   width: 100 },
      { name: 'inactiveReasonName',header: '비고',       width: 100 }
    ],
    data: []
  });
}

// 구독 이력 모달 열기
function openSubscribeModal(company) {
  document.getElementById('modalComCode').innerText = company.companyCode || '';
  document.getElementById('modalComName').innerText = company.companyName || '';

  const modal = document.getElementById('subscribeModal');
  modal.style.display = 'flex';

  loadSubscribeData(company.companyCode);
}

// 특정 회사의 구독 이력 조회
function loadSubscribeData(companyCode) {
  const url = '/api/manage/subscribes?companyCode=' + encodeURIComponent(companyCode);

  fetch(url)
    .then(function(response) {
      if (!response.ok) {
        throw new Error('구독 이력 조회 실패 (status: ' + response.status + ')');
      }
      return response.json();
    })
    .then(function(data) {
      subscribeGrid.resetData(data);
      subscribeGrid.refreshLayout();
    })
    .catch(function(err) {
      console.error(err);
      alert('구독 이력을 불러오는 중 오류가 발생했습니다.');
    });
}

// 구독 이력 모달 닫기
function closeSubscribeModal() {
  const modal = document.getElementById('subscribeModal');
  modal.style.display = 'none';
  subscribeGrid.resetData([]);
}
