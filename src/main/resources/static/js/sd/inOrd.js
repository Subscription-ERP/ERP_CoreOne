const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const btnOpenSkuModal = document.getElementById('btnOpenSkuModal');
const custCodeSearch = document.getElementById('custCodeSearch');
const custNameSearch = document.getElementById('custNameSearch');
const btnCustClose = document.getElementById('btnCustClose');
const backdrop = document.querySelector(".modal-layer__backdrop");
const btnAddRow = document.getElementById('btnAddRow');
const btnDeleteRow = document.getElementById('btnDeleteRow');

let inOrdGrid;
let unitPriceTypeItems = [];
let skuDataList = [];
let defaultType = '';
let selectedCustCode = null;
let custSearchByEnter = false;
let remain = 0; // 잔여여신

document.getElementById('btnSave').addEventListener('click', async (e) => {
    e.preventDefault();

    // 유효성 검사 실행
    if (!validateInordForm()) {
        return;
    }

    const saveInOrdData = getInOrdData();

    const res = await fetch('/api/inOrd/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(saveInOrdData)
    });

    if (res.ok) {
        showToast('등록 완료', 'success');
        resetData();
    } else {
        showToast('등록 실패', 'error');
    }
});

// 조회
document.addEventListener("DOMContentLoaded", function () {

    /* ======================================================
       기본 사항 입력
    ====================================================== */

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

        inOrdGrid.finishEditing();

        const custCode = document.getElementById('custCodeSearch').value.trim();
        const custName = document.getElementById('custNameSearch').value.trim();

        if (!custCode && !custName) {
            showToast('거래처를 먼저 선택하세요.', 'warning'); // 기존 showToast 패턴 재사용[web:50]
            return;
        }

        window.skuModalType = 'sell';
        openSkuModalWindow();
    });

    /* ======================================================
       품목 목록
    ====================================================== */

    // grid 정보
    inOrdGrid = new tui.Grid({
        el: document.getElementById('inOrdGrid'),
        rowHeaders: ['checkbox'],
        bodyHeight: 'fitToParent', // 385
        scrollX: false,
        scrollY: true,
        useOptions: {
            editable: true
        },
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
                formatter: 'listItemText',
                editor: {
                    type: 'select',
                    options: {
                        listItems: unitPriceTypeItems
                    }
                }
            },
            {
                header: '단가',
                name: 'unitPrice',
                align: 'right',
                editor: 'text'
            },
            {
                header: '공급가액',
                name: 'supplyPrice',
                align: 'right'
            },
            {
                header: '부가세',
                name: 'surTax',
                align: 'right'
            },
            {
                header: '비고  ',
                name: 'remark',
                width: 250,
                minWidth: 250,
                editor: 'text'
            },
            { name: 'taxYn', hidden: true }
        ],
        data: []
    });

    emptyRow();

    // 초기화 버튼
    document.getElementById("btnReset").addEventListener('click', resetData);

    // 행 추가
    btnAddRow.addEventListener('click', emptyRow);

    // 행 삭제
    btnDeleteRow.addEventListener('click', () => {
        const checkedRows = inOrdGrid.getCheckedRows();
        if (!checkedRows.length) {
            showToast('삭제할 행을 선택하세요.', 'warning');
            return;
        }

        inOrdGrid.removeCheckedRows(false);

        if (inOrdGrid.getRowCount() === 0) {
            emptyRow();
        }

        sumPrice();
    });


    // editor가 설정된 컬럼만 클릭시 편집 시작
    inOrdGrid.on('click', (e) => {
        const column = e.columnName && inOrdGrid.getColumn(e.columnName);
        if (column && column.editor) {
            inOrdGrid.startEditing(e.rowKey, e.columnName);
        }
    });

    inOrdGrid.on('afterChange', (e) => {
        e.changes.forEach(change => {
            let { rowKey, columnName, value } = change;

            // 거래처 미선택 시 막기
            if (columnName === 'sku' || columnName === 'skuName') {
                if (!validateInordForm()) {
                    inOrdGrid.setValue(rowKey, columnName, '');
                    return;
                }
            }

            // 품목코드 입력시 자동 완성
            if (columnName === 'sku') {
                const skuCode = String(value || '').trim();

                if (!skuCode || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.sku === skuCode);

                if (sku) {
                    inOrdGrid.setValue(rowKey, 'skuName', sku.skuName || '');
                    inOrdGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    inOrdGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    inOrdGrid.setValue(rowKey, 'taxYn', sku.taxYn);
                } else {
                    inOrdGrid.setValue(rowKey, 'skuName', '');
                    inOrdGrid.setValue(rowKey, 'spec',    '');
                    inOrdGrid.setValue(rowKey, 'unit',    '');
                    inOrdGrid.setValue(rowKey, 'taxYn', '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', 0);
                }
            }

            // 품목명 입력시 자동 완성
            else if (columnName === 'skuName') {
                const skuName = String(value || '').trim();

                if (!skuName || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.skuName === skuName);

                if (sku) {
                    inOrdGrid.setValue(rowKey, 'sku', sku.sku || '');
                    inOrdGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    inOrdGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    inOrdGrid.setValue(rowKey, 'taxYn', sku.taxYn);
                } else {
                    inOrdGrid.setValue(rowKey, 'sku', '');
                    inOrdGrid.setValue(rowKey, 'spec',    '');
                    inOrdGrid.setValue(rowKey, 'unit',    '');
                    inOrdGrid.setValue(rowKey, 'taxYn', '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', 0);
                }
            }

            // 단가유형 선택 시 단가 변경 (이미 있는 로직 유지)
            if (columnName === 'unitPriceType') {
                handleUnitPriceTypeChange(rowKey, value);
            }

            // 공급가액, 부가세 계산
            if (columnName === 'qty' || columnName === 'unitPrice') {
                const row = inOrdGrid.getRow(rowKey);
                const qty = Number(row.qty) || 0;
                const unitPrice = Number(row.unitPrice) || 0;

                const supplyPrice = qty * unitPrice;
                const taxYn = row.taxYn;

                const surTax = (taxYn === 'N') ? 0 : Math.floor(supplyPrice * 0.1);


                inOrdGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
                inOrdGrid.setValue(rowKey, 'surTax', surTax.toLocaleString());

                // 총공급가액, 총부가세, 총액 화면에 표시
                sumPrice();
            }

            // 데이터 입력 후 다음 행 추가
            if (columnName === 'sku' || columnName === 'skuName') {
                const data = inOrdGrid.getData();
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

    inOrdGrid.on('editingStart', e => {
        const { rowKey, columnName } = e;
        if (columnName !== 'unitPriceType') return;

        const editor = inOrdGrid.getColumn(columnName).editor;
        const el = editor && editor.el;
        if (!el) return;

        const selectEl = el.querySelector('select');
        if (!selectEl) return;

        selectEl.addEventListener('change', () => {
            inOrdGrid.finishEditing();

            const value = selectEl.value;
            handleUnitPriceTypeChange(rowKey, value);
        }, { once: true });
    });


    inOrdGrid.off && inOrdGrid.off('editingFinish');

    inOrdGrid.on('editingFinish', (e) => {
        const { rowKey, columnName, value } = e;
        if (columnName === 'unitPriceType') {
            handleUnitPriceTypeChange(rowKey, value);
        }
    });

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



});

// 거래처 검색
window.afterCustSearch = function(result) {
    const byEnter = custSearchByEnter === true;
    custSearchByEnter = false;

    if (byEnter) {
        if (result.length === 1) {
            // 1건이면 바로 선택
            returnOnlyOne(result);
        } else if (result.length > 1) {
            // 여러 건이면 모달 열어서 선택하게
            window.custModalType = 'sales';
            openCustModal();
            searchCust(false);
        } else {
            showToast('검색 결과가 없습니다.', 'warning');
        }
    } else {
        window.custModalType = 'sales';
        openCustModal();
    }
};

// 모달에서 거래처 값 불러오기
window.handleSelectedCust = function(row) {

    // 품목 모달에 거래처 코드를 넘겨주기 위한 코드
    // selectedCustCode = row.custCode;

    custCodeSearch.value = row.custCode;
    custNameSearch.value = row.custName;

    document.getElementById('creditMax').value = Number(row.creditMax).toLocaleString();
    sumPrice();
    skuList();
};

// 품목 모달에서 값 불러오기
window.handleSelectedSku = function (row) {
    const data = inOrdGrid.getData();
    if (!data.length) return;

    // 1) 중복 품목 여부 체크 (sku 기준, 필요하면 skuName도 같이 체크)
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

    // 2) 마지막 행 rowKey 구하기
    const lastRow = data[data.length - 1];

    const rowKey = lastRow.rowKey;

    // 3) 마지막 행에 값 세팅
    inOrdGrid.setValue(rowKey, 'sku',       row.sku || '');
    inOrdGrid.setValue(rowKey, 'skuName',   row.skuName || '');
    inOrdGrid.setValue(rowKey, 'spec',      row.spec || '');
    inOrdGrid.setValue(rowKey, 'unit',      row.unit || '');
    inOrdGrid.setValue(rowKey, 'unitPriceType',  row.unitPriceTypeCode || defaultType);
    inOrdGrid.setValue(rowKey, 'unitPrice', row.unitPrice || 0);
    inOrdGrid.setValue(rowKey, 'taxYn',     row.taxYn || '');

    // 4) 공급가/부가세 재계산
    const qty        = Number(inOrdGrid.getValue(rowKey, 'qty')) || 0;
    const unitPrice  = Number(row.unitPrice) || 0;
    const supplyPrice = qty * unitPrice;
    const surTax      = (row.taxYn === 'N') ? 0 : Math.floor(supplyPrice * 0.1);

    inOrdGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
    inOrdGrid.setValue(rowKey, 'surTax',      surTax.toLocaleString());
    sumPrice();

    // 필요하면 모달 닫기
    // closeSkuModal();
};




/* ======================================================
   함수
====================================================== */

// 모달 열기
function openCustModalOnly(e) {
    e.preventDefault();

    const schCustCode = document.getElementById('schCustCode');
    const schCustName = document.getElementById('schCustName');

    if (schCustCode) schCustCode.value = '';
    if (schCustName) schCustName.value = '';

    window.custModalType = 'sales';
    openCustModal();
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

// 빈행 추가
function emptyRow(){
    inOrdGrid.appendRow({
        sku: '',
        skuName: '',
        qty: 0,
        unitPriceType: defaultType,
        unitPrice: 0,
        supplyPrice: 0,
        surTax: 0,
        remark: '',
        taxYn: ''
    });

    // 추가하는 행의 클래스 추가
    const data = inOrdGrid.getData();
    const last = data[data.length - 1];
    let rowKey = last && last.rowKey;

    if (data.length === 1) {
        rowKey = data[0].rowKey;
    }

    if (rowKey !== undefined) {
        inOrdGrid.addCellClassName(rowKey, 'spec', 'block');
        inOrdGrid.addCellClassName(rowKey, 'unit', 'block');
        inOrdGrid.addCellClassName(rowKey, 'supplyPrice', 'block');
        inOrdGrid.addCellClassName(rowKey, 'surTax', 'block');
    }
}

// 초기화 함수
function resetData() {
    document.querySelector('#dueDate').value  = '';
    document.querySelector('#dept').value  = '';
    document.querySelector('#pic').value  = '';
    document.querySelector('#custCodeSearch').value  = '';
    document.querySelector('#custNameSearch').value  = '';
    document.querySelector('#creditMax').value  = '';
    document.querySelector('#creditRemain').value  = '';
    document.querySelector('#totalSupplyPrice').value  = '';
    document.querySelector('#totalSurtax').value  = '';
    document.querySelector('#totalPrice').value  = '';

    if (inOrdGrid) {
        inOrdGrid.resetData([]);
        emptyRow();
    }
}

// 총공급액/총부가세/총액/잔여여신 계산
// 여신/합계 계산
function sumPrice() {
    const rows = inOrdGrid.getData();

    let totalSupply = 0;
    let totalSurTax = 0;

    rows.forEach(row => {
        // sp: 공급가액, st: 부가세
        const sp = Number(String(row.supplyPrice || 0).replace(/,/g, '')) || 0;
        const st = Number(String(row.surTax || 0).replace(/,/g, '')) || 0;
        totalSupply += sp;
        totalSurTax += st;
    });

    // 총액
    const totalPrice = totalSupply + totalSurTax;

    document.getElementById('totalSupplyPrice').value = totalSupply.toLocaleString();
    document.getElementById('totalSurtax').value = totalSurTax.toLocaleString();
    document.getElementById('totalPrice').value = totalPrice.toLocaleString();

    const creditMaxEl = document.getElementById('creditMax');
    const creditRemainEl = document.getElementById('creditRemain');

    if (creditMaxEl && creditRemainEl) {
        const max = Number(String(creditMaxEl.value).replace(/,/g, '')) || 0;
        remain = max - totalPrice;
        creditRemainEl.value = remain.toLocaleString();

        // 실시간 여신 경고 (빨강색)
        if (remain < 0) {
            creditRemainEl.style.color = 'red';
            creditRemainEl.title = `여신 ${Math.abs(remain).toLocaleString()}원 초과`;
        } else {
            creditRemainEl.style.color = '';
            creditRemainEl.title = '';
        }
    }
}

// 등록 전 데이터 불러오기
function getInOrdData() {
    // 기본정보
    const inordInfo = {
        inordNo: document.getElementById('inordNo').value || null,
        inordDate: document.getElementById('inordDate').value,   // "yyyy-MM-dd"
        dueDate: document.getElementById('dueDate').value,       // "yyyy-MM-dd"
        dept: document.getElementById('dept')?.value || '',
        pic: document.getElementById('pic')?.value || '',
        custCode: document.getElementById('custCodeSearch').value,
        custName: document.getElementById('custNameSearch').value,
        totalSupplyPrice: toNumber(document.getElementById('totalSupplyPrice')?.value),
        totalSurtax: toNumber(document.getElementById('totalSurtax')?.value),
        totalPrice: toNumber(document.getElementById('totalPrice')?.value)
    };

    // 품목
    const rows = inOrdGrid.getData();
    const inordDetail = rows
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

    return { inordInfo, inordDetail };
}

// 콤마 → 숫자
function toNumber(v) {
    if (v == null) return 0;
    const raw = String(v).replace(/,/g, '').trim();
    if (raw === '') return 0;
    const n = Number(raw);
    return Number.isNaN(n) ? 0 : n;
}

// 단가유형 클릭시 단가 변환
function handleUnitPriceTypeChange(rowKey, value) {
    const row = inOrdGrid.getRow(rowKey);
    const skuCode = row.sku;
    const typeCode = value;

    if (skuCode && typeCode) {
        const skuInfo = skuDataList.find(item =>
            item.sku === skuCode && item.unitPriceTypeCode === typeCode
        );

        if (skuInfo && skuInfo.unitPrice != null) {
            inOrdGrid.setValue(rowKey, 'unitPrice', skuInfo.unitPrice);
        } else {
            inOrdGrid.setValue(rowKey, 'unitPrice', 0);
        }

        const qty = Number(row.qty) || 0;
        const supplyPrice = qty * (Number(inOrdGrid.getValue(rowKey, 'unitPrice')) || 0);
        const surTax = Math.floor(supplyPrice * 0.1);

        inOrdGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
        inOrdGrid.setValue(rowKey, 'surTax', surTax.toLocaleString());
        sumPrice();
    }
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
                if (
                    item.unitPriceTypeCode &&
                    item.unitPriceType &&
                    (item.unitPriceTypeCode === 'SELL_UNIT' || item.unitPriceTypeCode === 'SELL_DISC_UNIT')
                ) {
                    map.set(item.unitPriceTypeCode, item.unitPriceType);
                }
            });

            unitPriceTypeItems = Array.from(map.entries()).map(([value, text]) => ({
                text,
                value
            }));

            inOrdGrid.setColumns(inOrdGrid.getColumns().map(col => {
                if (col.name === 'unitPriceType') {
                    col.editor.options.listItems = unitPriceTypeItems;
                }
                return col;
            }));

            // 단가 유형 세팅
            defaultType =
                unitPriceTypeItems.find(it => it.value === 'SELL_UNIT')?.value
                || (unitPriceTypeItems[0] && unitPriceTypeItems[0].value)
                || '';

            // 기존 행에 기본값 세팅
            inOrdGrid.getData().forEach(row => {
                if (!row.unitPriceType) {
                    inOrdGrid.setValue(row.rowKey, 'unitPriceType', defaultType);
                }
            });

        })
        .catch(err => console.error(err));
}

// 유효성 검사
function validateInordForm() {
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

    // 3. 여신 초과
    if (remain < 0) {
        showToast(`여신을 ${Math.abs(remain).toLocaleString()}원 초과하였습니다.`, 'warning');
        return false;
    }

    return true;
}
