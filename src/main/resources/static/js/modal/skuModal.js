/**
 * 품번 조회 모달
 * 작성: ChatGPT ERP 구조 최적화 버전
 */

let skuModalGrid;

// 요소
const skuModal      = document.getElementById("skuModal");
const btnSkuClose   = document.getElementById("btnSkuClose");
const btnSkuSearch  = document.getElementById("btnSkuSearch");

const schSkuCode    = document.getElementById("schSkuCode");
const schSkuName    = document.getElementById("schSkuName");
const skuBackdrop   = document.querySelector("#skuModal .modal-layer__backdrop");

document.addEventListener("DOMContentLoaded", () => {

    const Grid = tui.Grid;
    Grid.applyTheme('clean');

    // GRID 생성
    skuModalGrid = new Grid({
        el: document.getElementById("skuModalGrid"),
        rowHeaders: ['rowNum'],
        bodyHeight: 400,
        scrollY: true,
        scrollX: false,
        columns: [
            { header: '품번', name: 'sku', minWidth: 120 },
            { header: '품명', name: 'skuName', minWidth: 150 },
            { header: '규격', name: 'spec', minWidth: 100 },
            { header: '단위', name: 'unit', minWidth: 70 },
            { header: '단가', name: 'unitPrice', minWidth: 100, align: 'right' }
        ],
        data: []
    });

    // 초기 데이터 로드
    getSkuList();

    // 검색 버튼
    btnSkuSearch.addEventListener("click", searchSku);

    // Enter 검색
    schSkuCode.addEventListener("keydown", handleEnterSku);
    schSkuName.addEventListener("keydown", handleEnterSku);

    // 닫기
    btnSkuClose.addEventListener("click", closeSkuModal);
    skuBackdrop.addEventListener("click", closeSkuModal);

    // row 클릭 → 값 전달
    skuModalGrid.on("click", (e) => {
        const row = skuModalGrid.getRow(e.rowKey);
        if (!row) return;

        if (typeof window.handleSelectedSku === "function") {
            window.handleSelectedSku(row);
        }

        closeSkuModal();
    });
});

/* ======================================================
   API 호출
====================================================== */

// 전체 품번 목록 조회
function getSkuList() {
    fetch("/api/cm/skuOne")
        .then(res => res.json())
        .then(data => {
            const list = Array.isArray(data) ? data : [];
            skuModalGrid.resetData(list);
        })
        .catch(console.error);
}

// 검색
function searchSku(custCode) {
        const sku= schSkuCode.value.trim();
        const skuName = schSkuName.value.trim();

     fetch("/api/cm/skuOne?sku="+sku+"&skuName="+skuName+"%custCode=" + custCode)
        .then(res => res.json())
        .then(data => {
            const list = Array.isArray(data) ? data : [];
            skuModalGrid.resetData(list);
            skuModalGrid.refreshLayout();
        })
        .catch(console.error);
}

// Enter 이벤트
function handleEnterSku(e) {
    if (e.key === "Enter") {
        searchSku();
    }
}

/* ======================================================
   모달 열기/닫기
====================================================== */

function openSkuModalWindow() {
    skuModal.hidden = false;
    skuModal.classList.remove("hidden");

    skuModalGrid.refreshLayout();
}

function closeSkuModal() {
    skuModal.hidden = true;
    skuModal.classList.add("hidden");
}
