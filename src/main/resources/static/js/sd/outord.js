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
        bodyHeight: 400,
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
            /*{
                header: '단가유형',
                name: 'unitPriceType',
                width: 120,
                minWidth: 120,
                formatter: 'listItemText',
                editor: {
                    type: 'select',
                    options: {
                        listItems: unitPriceTypeItems
                    }
                }
            },*/
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
        returnOnlyOne(result);
    } else {
        openCustModal();
    }
};

// 품목 모달에서 값 불러오기
window.handleSelectedSku = function (row) {

    console.log('selected row from modal:', row);

    const data = outordGrid.getData();
    console.log('before setValue, grid data:', data);

    if (!data.length) {
        console.warn('no rows in grid, append empty row first');
        emptyRow();
        return;
    }

    // 1) 중복 품목 여부 체크 (sku 기준, 필요하면 skuName도 같이 체크)
    const isDup = data.some(r => String(r.sku).trim() === String(row.sku).trim());
    if (isDup) {
        showToast('이미 선택된 품목입니다.', 'warning');
        return;
    }

    // 2) 마지막 행 rowKey 구하기
    const lastRow = data[data.length - 1];
    const rowKey = lastRow.rowKey;
    console.log('target rowKey:', rowKey, 'lastRow:', lastRow);

    // 3) 마지막 행에 값 세팅
    outordGrid.setValue(rowKey, 'sku',       row.sku || '');
    outordGrid.setValue(rowKey, 'skuName',   row.skuName || '');
    outordGrid.setValue(rowKey, 'spec',      row.spec || '');
    outordGrid.setValue(rowKey, 'unit',      row.unit || '');
    outordGrid.setValue(rowKey, 'unitPrice', row.unitPrice || 0);


    const after = outordGrid.getRow(rowKey);
    console.log('after setValue row:', after);

    // 4) 공급가/부가세 재계산
    const qty        = Number(outordGrid.getValue(rowKey, 'qty')) || 0;
    const unitPrice  = Number(row.unitPrice) || 0;
    const supplyPrice = qty * unitPrice;
    const surTax = Math.floor(supplyPrice * 0.1);

    outordGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
    outordGrid.setValue(rowKey, 'surTax',      surTax.toLocaleString());

    // 필요하면 모달 닫기
    // closeSkuModal();
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
                if (item.unitPriceType && item.typeName) {
                    map.set(item.unitPriceType, item.typeName);
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

            // 품목코드 입력시 자동 완성
            if (columnName === 'sku') {
                const skuCode = value;

                if (!skuCode || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.sku === skuCode);

                console.log(sku);

                if (sku) {
                    outordGrid.setValue(rowKey, 'skuName', sku.skuName || '');
                    outordGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    outordGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    outordGrid.setValue(rowKey, 'unitPrice', sku.unitPrice || 0);
                } else {
                    outordGrid.setValue(rowKey, 'skuName', '');
                    outordGrid.setValue(rowKey, 'spec',    '');
                    outordGrid.setValue(rowKey, 'unit',    '');
                    outordGrid.setValue(rowKey, 'unitPrice', 0);
                }
            }

            // 품목명 입력시 자동 완성
            else if (columnName === 'skuName') {
                const skuName = value;

                if (!skuName || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.skuName === skuName);

                if (sku) {
                    outordGrid.setValue(rowKey, 'sku', sku.sku || '');
                    outordGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    outordGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    outordGrid.setValue(rowKey, 'unitPrice', sku.unitPrice || 0);
                } else {
                    outordGrid.setValue(rowKey, 'sku', '');
                    outordGrid.setValue(rowKey, 'spec',    '');
                    outordGrid.setValue(rowKey, 'unit',    '');
                    outordGrid.setValue(rowKey, 'unitPrice', 0);
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
    const info = {
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
    const detail = rows
        .filter(r => (r.sku && String(r.sku).trim() !== '')   // 빈 행 제거
            || (r.skuName && String(r.skuName).trim() !== ''))
        .map((row, idx) => ({
            lineNo: idx + 1,
            sku: row.sku,
            qty: toNumber(row.qty),
            unitPrice: toNumber(row.unitPrice),     // 단가
            supplyPrice: toNumber(row.supplyPrice), // 공급가액 (수량*단가)
            surtax: toNumber(row.surTax),           // 부가세
            price: toNumber(row.supplyPrice) + toNumber(row.surTax),
            remark: row.remark
        }));

    return { info, detail };
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

    openCustModal();  // 모달 열기
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
        searchCust();
    }
}

// Enter 입력 시 검색
function handleEnter(e) {
    if(e.key === 'Enter') {
        e.preventDefault();
        searchCustModal();
    }
}