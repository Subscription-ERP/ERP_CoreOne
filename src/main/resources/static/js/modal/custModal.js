document.addEventListener('DOMContentLoaded', () => {
  const custModalGrid = new tui.Grid({
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

  function getCustList() {
    fetch('/api/sd/cust')
      .then(res => res.json())
      .then(result => {
        console.log(result);

        custModalGrid.resetData(result);
        custModalGrid.refreshLayout();

      })
      .catch(err => console.error(err));
  }
});




