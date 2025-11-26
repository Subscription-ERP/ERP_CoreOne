document.addEventListener("DOMContentLoaded", function () {

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
  
  function getList() {

		const selectParam ={
			sku : document.getElementById('selectsku').value,
			skuName : document.getElementById('selectskuname').value,
			unitPriceType : document.getElementById('selectunitpricetype').value,
			custCode : document.getElementById('selectcustcode').value,
			custName : document.getElementById('selectcustname').value
		}
		console.log(document.getElementById('selectunitpricetype').value);
	  fetch("/api/fi/unitprice?"+new URLSearchParams(selectParam))
	    .then(res => res.json())
	    .then(result => {
	      console.log(result); // JSON 데이터 확인

	      grid.resetData(result);
		  grid.refreshLayout();
	    })
	    .catch(err => console.error("조회 중 오류:", err));
	}

    document.getElementById("btnSearch").addEventListener("click", getList);
});