document.addEventListener('DOMContentLoaded', () => {
  const Grid = tui.Grid;

  const custModalGrid = new Grid({
    el: document.getElementById('custModalGrid'),
    bodyHeight: 220,
    rowHeaders: ['radio'],
    columns: [
      { header: '거래처코드', name: 'custCode', width: 110 },
      { header: '거래처명',   name: 'custName', minWidth: 180 },
      { header: '대표자명',   name: 'ceoName',  width: 90 },
      { header: '거래처 유형', name: 'custType', width: 90 }
    ]
  });

  const sample = [
    { custCode: '2020-SD-001', custName: '(주)예담직업전문학교', ceoName: '서강준', custType: '매입업체' },
    { custCode: 'CODE SAMPLE', custName: 'COMPANY SAMPLE', ceoName: 'NAME', custType: '매출업체' }
  ];
  custModalGrid.resetData(sample);

  document.getElementById('btnCustSearch').addEventListener('click', () => {
    // TODO: Ajax로 조회 후 custModalGrid.resetData(result);
  });

  document.getElementById('btnCustSelect').addEventListener('click', () => {
    const rowKey = custModalGrid.getCheckedRowKeys()[0];
    if (rowKey == null) return;
    const row = custModalGrid.getRow(rowKey);
    // 부모 화면에 값 세팅 후 모달 닫기
  });
});
