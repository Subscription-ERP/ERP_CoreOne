
  const grid = new tui.Grid({
	  el : document.getElementById('unitprice-grid'),
    data: [],
    rowHeaders: ['rowNum'],
    scrollX: false,
    scrollY: true,
    columns: [
      { header:'품번', name:'sku' },
      { header:'품명', name:'skuName' },
      { header:'단가유형', name:'unitPriceTypeName' },
      { header:'단가적용일', name:'startDate' },
      { header:'거래처', name:'custName' },
      { header:'기준단가', name:'unitPrice' }
    ]
  });
  
  getList();
  function getList() {
	  fetch("/api/fi/unitprice")
	    .then(res => res.json())
	    .then(result => {
	      console.log(result); // JSON 데이터 확인

	      grid.resetData(result);  // ⭐ 여기가 핵심
		  grid.refreshLayout(); // ⭐ 중요
	    })
	    .catch(err => console.error("조회 중 오류:", err));
	}