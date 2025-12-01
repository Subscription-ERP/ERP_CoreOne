document.addEventListener("DOMContentLoaded", function () {

  const grid = new tui.Grid({
    el: document.getElementById('credit-grid'),
    data: [],
    rowHeaders: ['rowNum'],
    scrollX: false,
    scrollY: true,
    bodyHeight: 'fitToParent',
    columns: [
      { header:'거래처코드', name:'custCode' },
      { header:'거래처명', name:'custName' },
      { header:'여신구분', name:'creditTypeName' },
      { header:'여신한도', name:'creditMax', align:'right'},
      { header:'외상한도일', name:'creditDueDay', align:'right'}
    ]
  });
  function getList(){
	const selectParam ={
		companyCode : '0000',
		custCode : document.getElementById('selectcustcode').value,
		custName : document.getElementById('selectcustname').value
	}
	fetch("/api/fi/credit?"+new URLSearchParams(selectParam))
	.then(res => res.json())
	.then(result => {
	  console.log(result); // JSON 데이터 확인

	  grid.resetData(result);
	  grid.refreshLayout();
	})
	.catch(err => console.error("조회 중 오류:", err));
  }
	document.getElementById("btnSearch").addEventListener("click", getList);

	function setCreditType(){
		fetch("/api/com/type?groupCode=CREDIT_TYPE")
		.then(res => res.json())
		.then(result => {
			result.forEach(item => {
				const select = document.querySelector("select[name='creditType']");
				const option = document.createElement("option");
				option.value = item.code;
				option.textContent = item.codeName; 
				select.appendChild(option);
			});
		})
	}
	setCreditType();
	
	function saveCredit(){
		const raw = Object.fromEntries(new FormData(document.getElementById("creditForm")));
		const selectParam = {
			companyCode : '0000',
		    custCode: raw.custCode,
		    creditType: raw.creditType,
		    creditMax: raw.creditLimit,
		    creditDueDay: raw.dueDays,
		};

		fetch("/api/fi/credit", {
		    method: "PUT",
		    headers: { "Content-Type": "application/json" },
		    body: JSON.stringify(selectParam)
		})
		.then(res => res.json())
		.then(result => {
		    alert("등록되었습니다.");
		    getList();
		});

	}
	
	document.getElementById("btnSave").addEventListener("click", saveCredit);
	
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
		document.querySelector("input[name='custCode']").value  = row.custCode;
		document.querySelector("input[name='custName']").value  = row.custName;
		document.querySelector("select[name='creditType']").value  = row.creditType;
		document.querySelector("input[name='creditLimit']").value  = row.creditMax;
		document.querySelector("input[name='dueDays']").value  = row.creditDueDay;
	}
	function resetData(){
		document.querySelector("input[name='custCode']").value  = '';
		document.querySelector("input[name='custName']").value  = '';
		document.querySelector("select[name='creditType']").value  = '';
		document.querySelector("input[name='creditLimit']").value  = '';
		document.querySelector("input[name='dueDays']").value  = '';
	}
	
	
	
 });