const grid = new tui.Grid({
  el: document.getElementById('grid'),
  bodyHeight: 300,
  columns: [
    { header: '거래처코드', name: 'custCode' },
    { header: '거래처명', name: 'custName' },
    { header: '대표자명', name: 'ceoName' },
    { header: '연락처', name: 'phone' },
    { header: '이메일', name: 'custEmail' },
    { header: '주소  ', name: 'address' },
    { header: '거래처유형  ', name: 'custType' },
    { header: '사용구분  ', name: 'useStatus' },
    { header: '담당자  ', name: 'userName' },
  ],
  data: []   // 처음엔 빈 배열
});

async function loadData() {
  const res = await fetch('/api/sd/cust'); // or axios.get(...)
  const list = await res.json();             // [{id:..., name:...}, ...] 형태라고 가정
  grid.resetData(list);                      // 또는 grid.setData(list);
}

loadData();