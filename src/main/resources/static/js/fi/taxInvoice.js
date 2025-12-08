const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const tobno = document.getElementById('tobno');
const custCodeSearch = document.getElementById('tocustname');
const toceoname = document.getElementById('toceoname');
const toaddress = document.getElementById('toaddress');
const tobitem = document.getElementById('tobitem');
const tobtype = document.getElementById('tobtype');
const btnCustClose     = document.getElementById('btnCustClose');
const backdrop = document.querySelector(".modal-layer__backdrop");
const btnAddRow = document.getElementById('btnAddRow');
const btnLoadOrder = document.getElementById("btnLoadOrder");
let searchCustCode = '';
let currentSkuRowKey = null;

class IconRenderer {
    constructor(props) {
        const el = document.createElement("div");
        el.style.display = "flex";
        el.style.alignItems = "center";
        el.style.gap = "6px";

		el.style.justifyContent = "space-between";
        // 값 표시
        const text = document.createElement("span");
        text.textContent = props.value ?? "";

        // 버튼 생성
        const btn = document.createElement("button");
        btn.className = "btn btn-icon";
        btn.innerHTML = `<i class="bi bi-search"></i>`;
        btn.style.padding = "2px 5px";

        btn.addEventListener("click", () => {
            if (typeof openSkuModal === "function") {
                openSkuModal(props.rowKey);
            }
        });

        el.appendChild(text);
        el.appendChild(btn);

        this.el = el;
    }

    getElement() {
        return this.el;
    }
}

const grid = new tui.Grid({
    el: document.getElementById('invoice-grid'),
    scrollX: true,
    scrollY: true,
    bodyHeight: 'fitToParent',
    rowHeaders: ['rowNum'],

    columns: [
		{
		    header: '품번',
		    name: 'sku',
		    minWidth: 150,
			renderer: { type: IconRenderer },
			
		},
        { header:'품명', name:'skuName', minWidth:150 },
        { header:'출고일자', name:'inordDate', minWidth:140 , 
			editor: {
			  type: 'datePicker',
			  options: {
			    format: 'yyyy-MM-dd'
			  }
			}
		},
		{ name: 'inordNo', header: '수주번호', hidden: true },
        { header:'출고수량', name:'qty', minWidth:120, align:'right' },
        { header:'단가', name:'unitPrice', minWidth:120, align:'right' },
        { header:'공급가액', name:'supplyPrice', minWidth:140, align:'right' },
        { header:'세액', name:'surtax', minWidth:120, align:'right' }
    ]
});
// 모달 열기
function openCustModalOnly(e) {
	
    e.preventDefault();
	openCustModal();
    getCustList();    // 전체 목록 or 필요한 대로
}


// 거래처 모달 검색 결과 전달 받음
function searchCustModal() {
    const custCodeKeyword = custCodeSearch.value.trim();

    // 1) 모달 쪽 검색 키워드 입력
    const schCustCode = document.getElementById('schCustCode');

    if (schCustCode) schCustCode.value = custCodeKeyword;

    // 2) 모달 JS의 검색 함수 호출
    if (typeof searchCust === 'function') {
        custSearchByEnter = true;
        searchCust();
    }
}

function closeCustModal() {
	custModal.hidden = true;
	custModal.classList.add('hidden');
}


// Enter 입력 시 검색
function handleEnter(e) {
    if(e.key === 'Enter') {
        e.preventDefault();
        searchCustModal();
    }
}
function updateTotalAmount() {
    let totalSupply = 0;
    let totalTax = 0;

    grid.getData().forEach(row => {
        totalSupply += Number(row.supplyPrice || 0);
        totalTax += Number(row.surtax || 0);
    });

    document.getElementById("supplyAmount").value = totalSupply.toLocaleString();
    document.getElementById("taxAmount").value = totalTax.toLocaleString();
}

function addEmptyRow() {
    grid.appendRow({
        sku: '',
        skuName: '',
        inordDate: '',
        qty: '',
        unitPrice: '',
        supplyPrice: '',
        surtax: '',
		inordNo:''
    });

    // 마지막 행의 rowKey 가져오기
    const data = grid.getData();
    const lastRow = data[data.length - 1];

    // 포커스를 sku(품번) 셀로 이동
    if (lastRow) {
        grid.focus(lastRow.rowKey, 'sku');
    }
}

function openSkuModal(rowKey) {
    currentSkuRowKey = rowKey;  // 어떤 행에서 품번 검색했는지 저장
	if(!searchCustCode){
		showToast('거래처를 먼저 입력해주세요', 'warning');
		return;
	}
    // 실제 모달 열기
    // (custModal처럼 동일한 방식)
    openSkuModalWindow(searchCustCode);
}
function loadSkuInfo(rowKey, sku) {

    fetch(`/api/cm/skuOne?sku=${encodeURIComponent(sku)}`)
        .then(res => {
            if (!res.ok) throw new Error("SKU 조회 실패");
            return res.json();
        })
        .then(row => {
            if (!row || !row.sku) {
                alert("존재하지 않는 품번입니다.");
                grid.setValue(rowKey, 'sku', '');
                grid.setValue(rowKey, 'skuName', '');
                grid.setValue(rowKey, 'unitPrice', '');
                grid.setValue(rowKey, 'supplyPrice', '');
                grid.setValue(rowKey, 'taxPrice', '');
                return;
            }

            // 🔥 조회된 SKU 정보를 그리드에 입력
            grid.setValue(rowKey, 'skuName', row.skuName || '');
            grid.setValue(rowKey, 'unitPrice', row.unitPrice || 0);
            grid.setValue(rowKey, 'inOrdQty', 1);

            const supply = Number(row.unitPrice || 0);
            const tax = Math.floor(supply * 0.1);

            grid.setValue(rowKey, 'supplyPrice', supply.toLocaleString());
            grid.setValue(rowKey, 'taxPrice', tax.toLocaleString());

            updateInvoiceAmount();  // 공급가액/세액 총합 업데이트
        })
        .catch(err => {
            console.error(err);
            alert("품번 조회 중 오류가 발생했습니다.");
        });
}

document.addEventListener("DOMContentLoaded", function () {
	//console.log("로그인 사용자:", LOGIN_USER_ID);
	//console.log("회사코드:", LOGIN_COMPANY_CODE);
	const today = new Date().toISOString().split("T")[0];
	document.getElementById("documentDate").value = today;
	// 모달에서 거래처 값 불러오기
	window.handleSelectedCust = function(row) {
	    custCodeSearch.value = row.custName;
		tobno.value = row.bno;
		toceoname.value = row.ceoName;
		toaddress.value = row.address;
		tobitem.value = row.bitem;
		tobtype.value = row.btype;	    
		searchCustCode = row.custCode;
	
	    //document.getElementById('creditMax').value = Number(row.creditMax).toLocaleString();
	    //sumPrice();
	};
	
	window.afterCustSearch = function(result) {
	    const byEnter = custSearchByEnter === true;
	    custSearchByEnter = false;
	
	    if (byEnter) {
	        returnOnlyOne(result);
	    } else {
	        // 버튼으로 모달을 열어 내부에서 검색한 경우 등: 그냥 모달 보여주기만
	        openCustModal();
	    }
	};

	btnLoadOrder.addEventListener("click", function () {
		if (!searchCustCode || searchCustCode.trim() === "") {
		    showToast("거래처를 먼저 선택하세요.", "warning");
		    return;
		}
		openInOrdModal();
		getInOrdList(searchCustCode);   // 거래처코드 기반 조회
	});
	window.handleSelectedInOrd = function(rows) {

		const current = grid.getData();     // 기존 데이터
		const appendList = [];              // 그리드에 넣을 최종 목록.

		rows.forEach(r => {
		    appendList.push({
		        sku: r.sku,
		        skuName: r.skuName,
		        inordDate: r.inordDate,
		        qty: r.qty,
		        unitPrice: r.unitPrice,
		        supplyPrice: r.supplyPrice,
		        surtax: r.surtax,
				inordNo: r.inordNo
		    });
		});
		
		custCodeSearch.value = rows[0].custCode;
		searchCustModal();
		// 그리드 추가
		grid.resetData([...current, ...appendList]);
		updateTotalAmount();
	};
	// 거래처 검색
	custCodeSearch.addEventListener('keydown', handleEnter);
	btnOpenCustModal.addEventListener('click', openCustModalOnly);
	
	// 거래처 모달 닫기
	btnCustClose.addEventListener('click', closeCustModal);
	backdrop.addEventListener('click', closeCustModal);
	grid.on("afterChange", ev => {
	    ev.changes.forEach(change => {
	        const { rowKey, columnName, value } = change;

	        // 단가 또는 수량 입력 시 계산 수행
	        if (columnName === "unitPrice" || columnName === "inOrdQty") {
	            const row = grid.getRow(rowKey);

	            const qty = Number(row.inOrdQty || 0);
	            const unit = Number(row.unitPrice || 0);

	            const supply = qty * unit;
	            const tax = Math.floor(supply * 0.1);

	            grid.setValue(rowKey, "supplyPrice", supply);
	            grid.setValue(rowKey, "taxPrice", tax);

	            updateTotalAmount();
	        }
	    });
	});
	grid.on('gridUpdated', () => {
	    updateTotalAmount();
	});
	btnAddRow.addEventListener("click", function () {
	    addEmptyRow();
	});

	// skuModal에서 선택된 row 받음
	window.handleSelectedSku = function(row) {
	    if (currentSkuRowKey == null) return;
	    // Grid에 값 채우기
	    grid.setValue(currentSkuRowKey, "sku", row.sku);
	    grid.setValue(currentSkuRowKey, "skuName", row.skuName);
	    grid.setValue(currentSkuRowKey, "unitPrice", row.unitPrice);

	    // 기본 계산값
	    const qty = Number(grid.getValue(currentSkuRowKey, 'inOrdQty')) || 0;
	    const supplyPrice = qty * row.unitPrice;
	    const tax = Math.floor(supplyPrice * 0.1);

	    grid.setValue(currentSkuRowKey, "supplyPrice", supplyPrice.toLocaleString());
	    grid.setValue(currentSkuRowKey, "surtax", tax.toLocaleString());

	    updateInvoiceAmount();  // 합산 함수
	    currentSkuRowKey = null;
	};
})
// 배경 클릭 시 닫기 (옵션)