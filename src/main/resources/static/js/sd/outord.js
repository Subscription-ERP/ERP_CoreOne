let outordGrid;

// 요소
const backdrop = document.querySelector(".modal-layer__backdrop");
const btnReset = document.getElementById('btnReset');
const btnSave = document.getElementById('btnSave');
const btnDeleteRow = document.getElementById('btnDeleteRow');
const btnAddRow = document.getElementById('btnAddRow');
const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const btnOpenSkuModal = document.getElementById('btnOpenSkuModal');

const custCodeSearch = document.getElementById('custCodeSearch');
const custNameSearch = document.getElementById('custNameSearch');

let skuDataList = [];
let totalQty = 0;
let totalSupplyPrice = 0;
let totalSurtax = 0;
let totalPrice = 0;
let selectedCustCode = null;

document.addEventListener('DOMContentLoaded', function () {

    /* ======================================================
       품목 목록
    ====================================================== */

    outordGrid = new tui.Grid({
        el: document.getElementById('outordGrid'),
        rowHeaders: ['checkbox'],
        scrollX: false,
        scrollY: true,
        bodyHeight: 'fitToParent',
        useOptions: {
            editable: true
        },
        data: [],
        columns: [
            {
                sortingType: 'asc',
                sortable: true,
                header: '품목코드',
                name: 'sku',
                align: 'center',
                editor: 'text'
            },
            {
                header: '품목명',
                name: 'skuName',
                editor: 'text'
            },
            {
                header: '규격',
                name: 'spec',
                width: 100,
                minWidth: 100,
                align: 'right',
            },
            {
                header: '단위',
                name: 'unit',
                width: 100,
                minWidth: 100,
            },
            {
                header: '수량',
                name: 'qty',
                width: 100,
                minWidth: 100,
                align: 'right',
                editor: 'text'
            },
            {
                header: '단가유형',
                name: 'unitPriceType',
                width: 120,
                minWidth: 120,
                hidden: true
            },
            {
                header: '단가',
                name: 'unitPrice',
                align: 'right',
                editor: 'text',
                formatter: ({ value }) => value ? Number(value).toLocaleString() : ''
            },
            {
                header: '공급가액',
                name: 'supplyPrice',
                align: 'right',
                formatter: ({ value }) => value ? Number(value).toLocaleString() : ''
            },
            {
                header: '부가세',
                name: 'surTax',
                align: 'right',
                formatter: ({ value }) => value ? Number(value).toLocaleString() : ''
            },
            {
                header: '총액',
                name: 'price',
                align: 'right',
                formatter: ({ value }) => value ? Number(value).toLocaleString() : ''
            },
            {
                header: '비고  ',
                name: 'remark',
                width: 250,
                minWidth: 250,
                editor: 'text'
            },
        ],
        summary: {
            height: 40,
            position: 'bottom',
            columnContent: {
                qty: {
                    template(summary) {
                        totalQty = summary.sum;
                        return summary.sum ? summary.sum.toLocaleString() : 0;
                    }
                },
                supplyPrice: {
                    template(summary) {
                        totalSupplyPrice = summary.sum;
                        return summary.sum ? summary.sum.toLocaleString() : 0;
                    }
                },
                surTax: {
                    template(summary) {
                        totalSurtax = summary.sum;
                        return summary.sum ? summary.sum.toLocaleString() : 0;
                    }
                },
                price: {
                    template(summary) {
                        totalPrice = summary.sum;
                        return summary.sum ? summary.sum.toLocaleString() : 0;
                    }
                }
            }
        }

    });


    /* ======================================================
       기본 사항 입력
    ====================================================== */

    // 초기화 버튼
    btnReset.addEventListener('click', resetData);

    // 저장 버튼
    btnSave.addEventListener('click', async (e) => {
        e.preventDefault();

        const saveOutOrdData = getOutordData();

        const res = await fetch('/api/outord/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(saveOutOrdData)
        });

        if (res.ok) {
            showToast('등록 완료', 'success');
        } else {
            showToast('등록 실패', 'error');
        }
    });

    // 행추가 버튼
    btnAddRow.addEventListener('click', emptyRow);

    // 행삭제 버튼
    btnDeleteRow.addEventListener('click', deleteRow);

    // 거래처 검색
    custCodeSearch.addEventListener('keydown', handleEnter);
    custNameSearch.addEventListener('keydown', handleEnter);
    btnOpenCustModal.addEventListener('click', openCustModalOnly);

    // 거래처 모달 닫기
    btnCustClose.addEventListener('click', closeCustModal);
    backdrop.addEventListener('click', closeCustModal);

    // 품목 모달 열기
    btnOpenSkuModal.addEventListener('click', (e) => {
        e.preventDefault();

        outordGrid.finishEditing();

        const custCode = document.getElementById('custCodeSearch').value.trim();
        const custName = document.getElementById('custNameSearch').value.trim();

        if (!custCode && !custName) {
            showToast('거래처를 먼저 선택하세요.', 'warning'); // 기존 showToast 패턴 재사용[web:50]
            return;
        }

        window.skuModalType = 'buy';
        openSkuModalWindow();
    });

    emptyRow();             // 기본 빈 행
    rightEditorMode();      // 바로 편집모드 진입
    skuListAfterEnter();    // 품목 입력 후 데이터 불러오기

    /* ------------------------------------------------------------------
	 * 사원 모달
	 * ------------------------------------------------------------------ */

    const btnOpenHrModal = document.getElementById('btnOpenHrModal');
    const UserName = document.getElementById('pic');
    const UserDept = document.getElementById('dept');

    // 사원명 input 클릭 시 모달 열기

    btnOpenHrModal.addEventListener("click", (e) => {
        if (typeof openUserSearchModal === 'function') {
            openUserSearchModal(e);
        } else {
            console.error("에러가 발생했습니다.");
        }
    });


    // 모달에서 row 선택 시 호출되는 콜백 (전역)
    window.handleSelectedEmp = function(row) {
        if (UserDept) {
            UserDept.value = row.deptName;
        }
        if (UserName) {
            UserName.value = row.userName;
        }
    };


})

/* ======================================================
   모달
====================================================== */

// 모달에서 거래처 값 불러오기
window.handleSelectedCust = function(row) {

    // 품목 모달에 거래처 코드를 넘겨주기 위한 코드
    // selectedCustCode = row.custCode;

    custCodeSearch.value = row.custCode;
    custNameSearch.value = row.custName;

    skuList();
};

window.afterCustSearch = function(result) {
    const byEnter = custSearchByEnter === true;
    custSearchByEnter = false;

    if (byEnter) {
        if (result.length === 1) {
            // 1건이면 바로 선택
            returnOnlyOne(result);  // 모달 JS의 전역 함수 호출
        } else if (result.length > 1) {
            // 여러 건이면 모달 열어서 선택하게
            window.custModalType = 'purchase';
            openCustModal();
        } else {
            showToast('검색 결과가 없습니다.', 'warning');
        }
    } else {
        // 버튼으로 모달 연 경우: 그냥 목록만 보여주면 됨
        window.custModalType = 'purchase';
        openCustModal();
    }
};

// 품목 모달에서 값 불러오기
window.handleSelectedSku = function (row) {
    outordGrid.finishEditing();

    const data = outordGrid.getData();
    if (!data.length) return;

    // sku + unitPriceType 중복 체크
    const isDup = data.some(r => {
        const sku1 = String(r.sku || '').trim();
        const sku2 = String(row.sku || '').trim();

        const type1 = String(r.unitPriceType || '').trim();          // 그리드: 코드
        const type2 = String(row.unitPriceTypeCode || '').trim();    // 모달: 코드

        return sku1 !== '' && sku1 === sku2 && type1 !== '' && type1 === type2;
    });

    if (isDup) {
        showToast('이미 선택된 품목입니다.', 'warning');
        return;
    }

    // 마지막 행에 값 세팅
    const lastRow = data[data.length - 1];
    const rowKey = lastRow.rowKey;

    outordGrid.setValue(rowKey, 'sku',           row.sku || '');
    outordGrid.setValue(rowKey, 'skuName',       row.skuName || '');
    outordGrid.setValue(rowKey, 'spec',          row.spec || '');
    outordGrid.setValue(rowKey, 'unit',          row.unit || '');
    outordGrid.setValue(rowKey, 'unitPrice',     row.unitPrice || 0);
    outordGrid.setValue(rowKey, 'unitPriceType', row.unitPriceType || '');

    // 공급가/부가세 재계산
    const qty        = Number(outordGrid.getValue(rowKey, 'qty')) || 0;
    const unitPrice  = Number(row.unitPrice) || 0;
    const supplyPrice = qty * unitPrice;
    const surTax      = Math.floor(supplyPrice * 0.1);

    outordGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
    outordGrid.setValue(rowKey, 'surTax',      surTax.toLocaleString());

};




/* ======================================================
   함수
====================================================== */

// 바로 편집모드 진입
function rightEditorMode() {
    outordGrid.on('click', (e) => {
        const column = e.columnName && outordGrid.getColumn(e.columnName);
        if (column && column.editor) {
            outordGrid.startEditing(e.rowKey, e.columnName);
        }
    });
}

// 초기화 함수
function resetData() {
    document.querySelector('#dueDate').value  = '';
    document.querySelector('#dept').value  = '';
    document.querySelector('#pic').value  = '';
    document.querySelector('#custCodeSearch').value  = '';
    document.querySelector('#custNameSearch').value  = '';

    if (outordGrid) {
        outordGrid.resetData([]);
        emptyRow();
    }
}

// 빈행 추가
function emptyRow(){
    outordGrid.appendRow({
        sku: '',
        skuName: '',
        qty: 0,
        unitPrice: 0,
        supplyPrice: 0,
        surTax: 0,
        price: 0,
        remark: '',
    });

    // 추가하는 행의 클래스 추가
    const data = outordGrid.getData();
    const last = data[data.length - 1];
    let rowKey = last && last.rowKey;

    if (data.length === 1) {
        rowKey = data[0].rowKey;
    }

    if (rowKey !== undefined) {
        outordGrid.addCellClassName(rowKey, 'spec', 'block');
        outordGrid.addCellClassName(rowKey, 'unit', 'block');
        outordGrid.addCellClassName(rowKey, 'supplyPrice', 'block');
        outordGrid.addCellClassName(rowKey, 'surTax', 'block');
        outordGrid.addCellClassName(rowKey, 'price', 'block');
    }
}

// 행 삭제
function deleteRow() {
    const checkedRows = outordGrid.getCheckedRows();
    if (!checkedRows.length) {
        showToast('삭제할 행을 선택하세요.', 'warning');
        return;
    }

    outordGrid.removeCheckedRows(false);

    if (outordGrid.getRowCount() === 0) {
        emptyRow();
    }
}


// 콤마 → 숫자
function toNumber(v) {
    if (v == null) return 0;
    const raw = String(v).replace(/,/g, '').trim();
    if (raw === '') return 0;
    const n = Number(raw);
    return Number.isNaN(n) ? 0 : n;
}


// 품목 불러오기 (직접 입력)
function skuList() {
    const custCode = document.getElementById('custCodeSearch').value.trim();

    if (!custCode) {
        showToast('거래처 코드가 없습니다. 모달에서 거래처를 먼저 선택하세요.', 'warning');
        return;
    }

    const url = `/api/cm/skuList`;

    fetch(url)
        .then(res => res.json())
        .then(result => {
            skuDataList = result;

            const map = new Map();
            skuDataList.forEach(item => {
                if (item.unitPriceType) {
                    map.set(item.unitPriceType);
                }
            });

            unitPriceTypeItems = Array.from(map.entries()).map(([value, text]) => ({
                text,
                value
            }));

        })
        .catch(err => console.error(err));
}

// 품목 직접 입력으로 값 불러오기
function skuListAfterEnter() {
    outordGrid.on('afterChange', (e) => {
        console.log('afterChange:', e.changes);

        e.changes.forEach(change => {
            let { rowKey, columnName, value } = change;

            if (columnName === 'sku' || columnName === 'skuName') {
                if (!validateOutordForm()) {
                    outordGrid.setValue(rowKey, columnName, '');
                    return;
                }
            }

            // 수량 변경 시 공급가/부가세/총액 재계산
            if (columnName === 'qty' || columnName === 'unitPrice') {
                const row = outordGrid.getRow(rowKey);
                const qty = toNumber(row.qty) || 0;
                const unitPrice = toNumber(row.unitPrice) || 0;

                const supplyPrice = qty * unitPrice;
                const surTax = Math.floor(supplyPrice * 0.1);
                const price = supplyPrice + surTax;

                outordGrid.setValue(rowKey, 'supplyPrice', supplyPrice);
                outordGrid.setValue(rowKey, 'surTax', surTax);
                outordGrid.setValue(rowKey, 'price', price);
            }

            // 데이터 입력 후 다음 행 추가
            if (columnName === 'sku' || columnName === 'skuName') {
                const data = outordGrid.getData();
                if (!data.length) return;

                const lastRow = data[data.length - 1];

                if (lastRow.rowKey === rowKey) {
                    const hasSkuOrName = (lastRow.sku && String(lastRow.sku).trim() !== '') ||
                        (lastRow.skuName && String(lastRow.skuName).trim() !== '');
                    if (hasSkuOrName) {
                        emptyRow();
                    }
                }
            }


        });
    });
}

// 저장할 발주 데이터 불러오기
function getOutordData() {
    // 저장 직전에 항상 최신 합계 계산
    const totals = calcTotalsFromGrid();

    // 기본정보
    const outordInfo = {
        outordNo: document.getElementById('outordNo').value || null,
        outordDate: document.getElementById('outordDate').value,
        dueDate: document.getElementById('dueDate').value,
        dept: document.getElementById('dept')?.value || '',
        pic: document.getElementById('pic')?.value || '',
        custCode: document.getElementById('custCodeSearch').value,
        custName: document.getElementById('custNameSearch').value,

        totalQty: totals.totalQty,
        totalSupplyPrice: totals.totalSupplyPrice,
        totalSurtax: totals.totalSurtax,
        totalPrice: totals.totalPrice
    };

    // 품목
    const rows = outordGrid.getData();
    const outordDetail = rows
        .filter(r => (r.sku && String(r.sku).trim() !== '')   // 빈 행 제거
            || (r.skuName && String(r.skuName).trim() !== ''))
        .map((row, idx) => ({
            lineNo: idx + 1,
            sku: row.sku,
            skuName: row.skuName,
            qty: toNumber(row.qty),
            unitPrice: toNumber(row.unitPrice),     // 단가
            supplyPrice: toNumber(row.supplyPrice), // 공급가액 (수량*단가)
            surtax: toNumber(row.surTax),           // 부가세
            price: toNumber(row.supplyPrice) + toNumber(row.surTax),
            remark: row.remark
        }));

    return { outordInfo, outordDetail };
}


function calcTotalsFromGrid() {
    const rows = outordGrid.getData();
    const validRows = rows.filter(r =>
        (r.sku && String(r.sku).trim() !== '') ||
        (r.skuName && String(r.skuName).trim() !== '')
    );

    return validRows.reduce((acc, r) => {
        acc.totalQty         += toNumber(r.qty);
        acc.totalSupplyPrice += toNumber(r.supplyPrice);
        acc.totalSurtax      += toNumber(r.surTax);
        acc.totalPrice       += toNumber(r.price);
        return acc;
    }, { totalQty: 0, totalSupplyPrice: 0, totalSurtax: 0, totalPrice: 0 });
}



/* ======================================================
   함수 - 모달
====================================================== */

function openCustModalOnly(e) {
    e.preventDefault();

    // 모달 input에 부모 검색어만 세팅
    // const custCodeKeyword = custCodeSearch.value.trim();
    // const custNameKeyword = custNameSearch.value.trim();

    const schCustCode = document.getElementById('schCustCode');
    const schCustName = document.getElementById('schCustName');

    if (schCustCode) schCustCode.value = '';
    if (schCustName) schCustName.value = '';

    // if (schCustCode) schCustCode.value = custCodeKeyword;
    // if (schCustName) schCustName.value = custNameKeyword;

    // 모달 열기
    window.custModalType = 'purchase';
    openCustModal();

    getCustList();    // 전체 목록 or 필요한 대로
}

// 거래처 모달 검색 결과 전달 받음
function searchCustModal() {
    const custCodeKeyword = custCodeSearch.value.trim();
    const custNameKeyword = custNameSearch.value.trim();

    // 1) 모달 쪽 검색 키워드 입력
    const schCustCode = document.getElementById('schCustCode');
    const schCustName = document.getElementById('schCustName');

    if (schCustCode) schCustCode.value = custCodeKeyword;
    if (schCustName) schCustName.value = custNameKeyword;

    // 2) 모달 JS의 검색 함수 호출
    if (typeof searchCust === 'function') {
        custSearchByEnter = true;
        searchCust(true);
    }
}

// Enter 입력 시 검색
function handleEnter(e) {
    if(e.key === 'Enter') {
        e.preventDefault();
        searchCustModal();
    }
}

// 유효성 검사
function validateOutordForm() {
    // 1. 거래처 필수
    const custCode = document.getElementById('custCodeSearch').value.trim();
    if (!custCode) {
        showToast('거래처를 선택하세요.', 'warning');
        return false;
    }

    // 2. 품목 최소 1개
    const validRows = inOrdGrid.getData().filter(r =>
        r.sku?.trim() || r.skuName?.trim()
    );
    if (validRows.length === 0) {
        showToast('품목을 1개 이상 입력하세요.', 'warning');
        return false;
    }

    return true;
}

