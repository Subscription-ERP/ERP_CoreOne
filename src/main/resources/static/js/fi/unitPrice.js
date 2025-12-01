document.addEventListener("DOMContentLoaded", function () {

	// 거래처 모달
	const custModal = document.getElementById('custModal');
	const btnOpenCustModal = document.getElementById('btnOpenCustModal');
	const btnCustClose = document.getElementById('btnCustClose');
	const btnCustCancel = document.getElementById('btnCustCancel');
	const backdrop = custModal.querySelector('.modal-layer__backdrop');
	
	let alreadyCheck = false;
	
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
			companyCode : 'SAMPLE_COMPANY',
			sku : document.getElementById('selectsku').value,
			skuName : document.getElementById('selectskuname').value,
			unitPriceType : document.getElementById('selectunitpricetype').value,
			custCode : document.getElementById('selectcustcode').value,
			custName : document.getElementById('selectcustname').value
		}
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
	
	function saveUnitPrice(){
		
		const raw = Object.fromEntries(new FormData(document.getElementById("unitPriceForm")));
	
		const selectParam = {
			companyCode : 'SAMPLE_COMPANY',
		    sku: raw.insertsku,
		    unitPriceType: raw.insertunitpricetype,
		    startDate: raw.insertstartdate,
		    custCode: raw.insertcustcode,
		    unitPrice: raw.insertunitprice
		};
		
		
		fetch("/api/fi/unitprice/check?" + new URLSearchParams(selectParam))
		.then(res => res.json())
		.then(result => {		
		console.log("response raw:", result);
		console.log("cnt value:", result.cnt, typeof result.cnt);
			if(result.cnt > 0){
				updateUnitPrice(selectParam);	
			}
			else {
				insertUnitPrice(selectParam);
			}
  		})
		.catch(err => console.error("조회 중 오류:", err));
	}

	document.getElementById("btnSave").addEventListener("click", saveUnitPrice);
	function insertUnitPrice(data){
		console.log(data);
		fetch("/api/fi/unitprice", {
		    method: "POST",
		    headers: { "Content-Type": "application/json" },
		    body: JSON.stringify(data)
		})
		.then(res => res.json())
		.then(result => {
		    alert("등록되었습니다.");
		    getList();
		});
	}
	function updateUnitPrice(data){
		fetch("/api/fi/unitprice", {
		    method: "PUT",
		    headers: { "Content-Type": "application/json" },
		    body: JSON.stringify(data)
		})
		.then(res => res.json())
		.then(result => {
		    alert("수정되었습니다.");
		    getList();
		});
	}
	setUnitPriceType();
	function setUnitPriceType(){
		fetch("/api/com/type?groupCode=UNIT_PRICE_TYPE")
		.then(res => res.json())
		.then(result => {
			result.forEach(item => {
				const select = document.querySelector("select[name='insertunitpricetype']");
				const select2 = document.querySelector("select[name='selectunitpricetype']");
				const option = document.createElement("option");
				option.value = item.code;
				option.textContent = item.codeName; 
				select.appendChild(option);
				select2.appendChild(option);
			});
		})
	}

	// 행 클릭시 상세정보 보기
	grid.on('click', (ev) => {
	  const rowKey = ev.rowKey;

	  if (rowKey == null) return;

	  const row = grid.getRow(rowKey);
	  setSelectedRow(row)
	});

	function setSelectedRow(row){
		if (!row) {
		  // 선택 없을 때 초기화
		  resetData();
		  return;
		}
		document.querySelector("input[name='insertsku']").value  = row.sku;
		document.querySelector("input[name='insertskuname']").value  = row.skuName;
		document.querySelector("select[name='insertunitpricetype']").value  = row.unitPriceType;
		document.querySelector("input[name='insertstartdate']").value  = row.startDate;
		document.querySelector("input[name='insertcustcode']").value  = row.custCode;
		document.querySelector("input[name='insertcustname']").value  = row.custName;
		document.querySelector("input[name='insertunitprice']").value  = row.unitPrice;
	}
	function resetData(){
		document.querySelector("input[name='insertsku']").value  = '';
		document.querySelector("input[name='insertskuname']").value  = '';
		document.querySelector("select[name='insertunitpricetype']").value  = ';'
		document.querySelector("input[name='insertstartdate']").value  = '';
		document.querySelector("input[name='insertcustcode']").value  = ''
		document.querySelector("input[name='insertcustname']").value  = '';
		document.querySelector("input[name='insertunitprice']").value  = '';
	}

	// 거래처 모달
	function openCustModal(e) {
		if (e) {
			e.stopPropagation();
			e.preventDefault();
		}
		custModal.hidden = false;
		custModal.classList.remove('hidden');

		if (window.custModalGrid) {
			window.custModalGrid.refreshLayout();
		}
	}

	function closeCustModal() {
		custModal.hidden = true;
		custModal.classList.add('hidden');
	}

	// 열기 버튼
	btnOpenCustModal.addEventListener('click', openCustModal);

	// 닫기 버튼들
	btnCustClose.addEventListener('click', closeCustModal);
	btnCustCancel.addEventListener('click', closeCustModal);

	// 배경 클릭 시 닫기 (옵션)
	backdrop.addEventListener('click', closeCustModal);
});