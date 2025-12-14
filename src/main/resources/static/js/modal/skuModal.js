/**
 * 품번 조회 모달
 * 작성: ChatGPT ERP 구조 최적화 버전
 *
 * 이력
 * 2025-12-08 최초 생성
 * 2025-12-12 단가 유형을 지정하여 필요한 품목을 지정할 수 있도록 변경
 * 2025-12-14 단가를 지정한 품목을 불러오도록 변경
 * 
 * 최초 생성자: 고유한
 * 최종 수정자: 박봉근
 *
 * 비고
 * 1. 단가 유형 지정 (buy: 발주, sell: 수주)
 *     window.skuModalType = 'sell';
 *     openSkuModalWindow();
 *
 * 2. 거래처 지정
 *     1) 전역변수 선언
 *     let selectedCustCode = null;
 *
 *     2) 모달에서 거래처 값 넘겨받을 때 selectedCustCode 저장
 *     selectedCustCode = row.custCode;
 */

let skuModalGrid;
let unitPriceType = null;

// 요소
const skuModal = document.getElementById("skuModal");
const btnSkuClose = document.getElementById("btnSkuClose");
const btnSkuSearch = document.getElementById("btnSkuSearch");

const schSkuCode = document.getElementById("schSkuCode");
const schSkuName = document.getElementById("schSkuName");
const skuBackdrop = document.querySelector("#skuModal .modal-layer__backdrop");

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
            {header: '품번', name: 'sku', minWidth: 120},
            {header: '품명', name: 'skuName', minWidth: 150},
            {header: '규격', name: 'spec', minWidth: 100, align: 'right'},
            {header: '단위', name: 'unit', minWidth: 70},
            {header: '단가유형', name: 'unitPriceType', minWidth: 80},
            {header: '단가', name: 'unitPrice', minWidth: 100, align: 'right'}
        ],
        data: []
    });

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

// 전체 품번 목록 조회 (초기/재조회)
function getSkuList(type) {
    const params = new URLSearchParams();

    // custCode가 있을 때만 추가
    if (selectedCustCode) {
        params.append('custCode', selectedCustCode);
    }

    if (type) {
        params.append('unitPriceType', type);
    }

    const url = `/api/cm/skuList?${params.toString()}`;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            const list = Array.isArray(data) ? data : [];
            skuModalGrid.resetData(list);
        })
        .catch(console.error);
}

// 검색
function searchSku() {
    const sku     = schSkuCode.value.trim();
    const skuName = schSkuName.value.trim();

    const params = new URLSearchParams({
        custCode: selectedCustCode || '',
        unitPriceType: unitPriceType || '',
        sku: sku || '',
        skuName: skuName || ''
    });

    fetch(`/api/cm/skuList?${params.toString()}`)
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
    unitPriceType = window.skuModalType || '';

    skuModal.hidden = false;
    skuModal.classList.remove("hidden");

    if (unitPriceType) {
        getSkuList(unitPriceType);
    }

    skuModalGrid.refreshLayout();
}

function closeSkuModal() {
    skuModal.hidden = true;
    skuModal.classList.add("hidden");
}
