document.addEventListener('DOMContentLoaded', () => {
  const custModal = document.getElementById('custModal');

  let custModalGrid;
  const btnSearch = document.getElementById("btnCustSearch");
  console.log('btnSearch =', btnSearch);
  const schCustCode = document.getElementById('schCustCode');
  const schCustName = document.getElementById('schCustName');

  custModalGrid = new tui.Grid({
    el: document.getElementById('custModalGrid'),
    rowHeaders: ['rowNum'],
    bodyHeight: 210,
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
    console.log(row);
    closeCustModal();

  })

  // 함수 영역 ==================================================================

  // 거래처 정보 불러오기
  function getCustList() {
    fetch('/api/sd/cust')
      .then(res => res.json())
      .then(result => {
        console.log(result);

        custModalGrid.resetData(result);
      })
      .catch(err => console.error(err));
  }

  // 거래처 검색
  function searchCust() {
    const custCode = document.getElementById('schCustCode').value.trim();
    const custName = document.getElementById('schCustName').value.trim();

    const params = {
      custCode,
      custName
    };

    fetch('/api/sd/searchCust', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(params)
    })
        .then(res => res.json())
        .then(result => {
          custModalGrid.resetData(result);

          if (result.length === 1) {
            const row = result[0];

            // 부모에 값 넘기는 함수
            handleSelectedCust(row);

            closeCustModal();
          }

          custModalGrid.refreshLayout();
        })
        .catch(err => console.error(err));
  }

  // Enter 입력 시 검색
  function handleEnter(e) {
    if(e.key === 'Enter') {
      e.preventDefault();
      btnSearch.click();
    }
  }

  // 모달 닫기
  function closeCustModal() {
    custModal.hidden = true;
    custModal.classList.add('hidden');
  }
});



