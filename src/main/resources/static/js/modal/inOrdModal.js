/**
 * 수주 조회 모달
 * 다중 선택 가능 (체크박스)
 */

let inOrdModalGrid;

// 요소들
const inOrdModal      = document.getElementById("inOrdModal");
const btnInOrdClose   = document.getElementById("btnInOrdClose");
const btnInOrdSearch  = document.getElementById("btnInOrdSearch");
const btnInOrdSelect  = document.getElementById("btnInOrdSelect");

const schInOrdCustCode     = document.getElementById("schInOrdCustCode");
const schInOrdCustName     = document.getElementById("schInOrdCustName");
const inOrdBackdrop   = document.querySelector("#inOrdModal .modal-layer__backdrop");

document.addEventListener("DOMContentLoaded", () => {

    const Grid = tui.Grid;
    Grid.applyTheme('clean');

    inOrdModalGrid = new Grid({
        el: document.getElementById("inOrdModalGrid"),
        rowHeaders: ['checkbox'],
        bodyHeight: 400,
        scrollX: false,
        scrollY: true,
        columns: [
            { header: '수주번호', name: 'inordNo', minWidth: 120 },
            { header: '수주일자', name: 'inordDate', minWidth: 120 },
            { header: '거래처코드', name: 'custCode', minWidth: 100 },
            { header: '거래처명', name: 'custName', minWidth: 150 },
			{ header: '품번', name: 'sku', minWidth: 150 },
			{ header: '품명', name: 'skuName', minWidth: 150 },
            { header: '수량', name: 'qty', minWidth: 80, align:'right' },
            { header: '금액', name: 'price', minWidth: 120, align:'right' }
        ],
        data: []
    });

    // 초기 전체 조회
    getInOrdList();

    // 검색
    btnInOrdSearch.addEventListener("click", () => searchInOrd());

    // 엔터로 검색
    schInOrdCustCode.addEventListener("keydown", handleEnterSearch);
    schInOrdCustName.addEventListener("keydown", handleEnterSearch);

    // 닫기
    btnInOrdClose.addEventListener("click", closeInOrdModal);
    inOrdBackdrop.addEventListener("click", closeInOrdModal);

    // 선택 버튼
    btnInOrdSelect.addEventListener("click", sendSelectedOrders);
});

/* ============================
   1. 전체 목록 조회
============================ */
function getInOrdList(custCode = "") {
	const url = custCode
	    ? `/api/inOrd/detail?custCode=${encodeURIComponent(custCode)}&searchDiv=Modal`
	    : `/api/inOrd/detail`;
		
    fetch(url)
        .then(res => res.json())
        .then(data => {
            inOrdModalGrid.resetData(data);
        })
        .catch(err => console.error(err));
}

/* ============================
   2. 검색
============================ */
function searchInOrd() {
        const custCode = schInOrdCustCode.value.trim();
        const custName = schInOrdCustName.value.trim();

    fetch("/api/inOrd/detail?custCode="+custCode+"&custName="+custName+"&searchdiv=Modal")
        .then(res => res.json())
        .then(data => {
            inOrdModalGrid.resetData(data);
            inOrdModalGrid.refreshLayout();
        })
        .catch(err => console.error(err));
}

function handleEnterSearch(e) {
    if (e.key === "Enter") {
        e.preventDefault();
        searchInOrd();
    }
}

/* ============================
   3. 선택 버튼 → 부모에게 전달
============================ */
function sendSelectedOrders() {
    const rows = inOrdModalGrid.getCheckedRows();

    if (rows.length === 0) {
        alert("선택된 수주가 없습니다.");
        return;
    }

    // 1) 거래처 동일성 체크
    const custSet = new Set(rows.map(r => r.custCode));
    if (custSet.size > 1) {
        alert("같은 거래처의 수주만 선택할 수 있습니다.");
        return;
    }

    // 2) 메인 그리드 중복 체크
    if (typeof grid !== "undefined") {
        const mainData = grid.getData();
        const mainDetailOrdNos = mainData.map(r => r.inordDetailNo);

        const duplicate = rows.some(r => mainDetailOrdNos.includes(r.inordDetailNo));

        if (duplicate) {
            alert("이미 추가된 수주가 포함되어 있습니다.");
            return;
        }
    }

    // 3) 정상 전달
    if (typeof window.handleSelectedInOrd === "function") {
        window.handleSelectedInOrd(rows);
    }

    closeInOrdModal();
}


/* ============================
   4. 모달 열기/닫기
============================ */
function openInOrdModal() {
    inOrdModal.hidden = false;
    inOrdModal.classList.remove("hidden");

    if (inOrdModalGrid) inOrdModalGrid.refreshLayout();
}

function closeInOrdModal() {
    inOrdModal.hidden = true;
    inOrdModal.classList.add("hidden");
}
