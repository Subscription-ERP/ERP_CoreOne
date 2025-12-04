const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const custCodeSearch = document.getElementById('custCodeSearch');
const custNameSearch = document.getElementById('custNameSearch');
const btnCustClose     = document.getElementById('btnCustClose');
const backdrop = document.querySelector(".modal-layer__backdrop");
const btnAddRow = document.getElementById('btnAddRow');
let inOrdGrid;
let unitPriceTypeItems = [];
let skuDataList = [];

/* Enter 입력 방지 (grid에서 post 방지) */
document.addEventListener('keydown', function (e) {
    const target = e.target;
    if (e.key === 'Enter'
        && target.tagName !== 'TEXTAREA'
        && target.type !== 'submit') {
        e.preventDefault();
    }
});

document.addEventListener("DOMContentLoaded", function () {

    /* 기본 입력 사항 */

    // 거래처 검색
    custCodeSearch.addEventListener('keydown', handleEnter);
    custNameSearch.addEventListener('keydown', handleEnter);
    btnOpenCustModal.addEventListener('click', openCustModalOnly);

    // 거래처 모달 닫기
    btnCustClose.addEventListener('click', closeCustModal);
    backdrop.addEventListener('click', closeCustModal)

    /* 수주 품목 목록 */

    // grid 정보
    inOrdGrid = new tui.Grid({
        el: document.getElementById('inOrdGrid'),
        rowHeaders: ['checkbox'],
        bodyHeight: 385,
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
                align: 'right',
            },
            {
                header: '단위',
                name: 'unit',
                width: 100,
            },
            {
                header: '수량',
                name: 'qty',
                width: 100,
                align: 'right',
                editor: 'text'
            },
            {
                header: '단가유형',
                name: 'unitPriceType',
                width: 120,
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
                editor: 'text'
            },
        ],
        data: []
    });

    emptyRow();
    skuList();

    // 추가버튼 클릭시 행 추가
    btnAddRow.addEventListener('click', emptyRow);
    document.getElementById("btnReset").addEventListener('click', resetData);


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

            // 입력시 자동 완성 ===========================================

            // 품목코드 입력시 자동 완성
            if (columnName === 'sku') {
                const skuCode = value;

                if (!skuCode || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.sku === skuCode);

                if (sku) {
                    inOrdGrid.setValue(rowKey, 'skuName', sku.skuName || '');
                    inOrdGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    inOrdGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', sku.unitPrice || 0);

                } else {
                    inOrdGrid.setValue(rowKey, 'skuName', '');
                    inOrdGrid.setValue(rowKey, 'spec',    '');
                    inOrdGrid.setValue(rowKey, 'unit',    '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', 0);
                }

                inOrdGrid.focus(rowKey, 'qty');
                inOrdGrid.startEditing(rowKey, 'qty');
            }

            // 품목명 입력시 자동 완성
            else if (columnName === 'skuName') {
                const skuName = value;

                if (!skuName || !skuDataList || skuDataList.length === 0) return;

                const sku = skuDataList.find(item => item.skuName === skuName);

                if (sku) {
                    inOrdGrid.setValue(rowKey, 'sku', sku.sku || '');
                    inOrdGrid.setValue(rowKey, 'spec',    sku.spec || '');
                    inOrdGrid.setValue(rowKey, 'unit',    sku.unit || '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', sku.unitPrice || 0);
                } else {
                    inOrdGrid.setValue(rowKey, 'sku', '');
                    inOrdGrid.setValue(rowKey, 'spec',    '');
                    inOrdGrid.setValue(rowKey, 'unit',    '');
                    inOrdGrid.setValue(rowKey, 'unitPrice', 0);
                }

                inOrdGrid.focus(rowKey, 'qty');
                inOrdGrid.startEditing(rowKey, 'qty');
            }

            // 공급가액, 부가세 계산
            if (columnName === 'qty' || columnName === 'unitPrice') {
                const row = inOrdGrid.getRow(rowKey);
                const qty = Number(row.qty) || 0;
                const unitPrice = Number(row.unitPrice) || 0;

                const supplyPrice = qty * unitPrice;
                const surTax = Math.floor(supplyPrice * 0.1);

                inOrdGrid.setValue(rowKey, 'supplyPrice', supplyPrice.toLocaleString());
                inOrdGrid.setValue(rowKey, 'surTax', surTax.toLocaleString());

                // 총공급가액, 총부가세, 총액 화면에 표시
                sumPrice();
            }

            // 자동 행 추가 ===========================================
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

});

// 모달에서 거래처 값 불러오기
handleSelectedCust = function(row) {
    custCodeSearch.value = row.custCode;
    custNameSearch.value = row.custName;

    document.getElementById('creditMax').value = Number(row.creditMax).toLocaleString();
};

window.afterCustSearch = function(result) {
    const byEnter = window.custSearchByEnter === true;
    window.custSearchByEnter = false;

    if (byEnter) {
        if (result.length === 1) {
            const row = result[0];

            if (typeof handleSelectedCust === 'function') {
                handleSelectedCust(row);
            }
            closeCustModal();
        } else {
            // 여러 건이면 모달 띄워서 사용자 선택
            openCustModal();
        }
    } else {
        // 버튼으로 모달을 열어 내부에서 검색한 경우 등: 그냥 모달 보여주기만
        openCustModal();
    }
};


/* 함수 */

// 모달 열기
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
        window.custSearchByEnter = true;
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

// 빈행 추가
function emptyRow(){
    inOrdGrid.appendRow({
        sku: '',
        skuName: '',
        spec: '',
        qty: 0,
        unitPriceType: '',
        unitPrice: 0,
        supplyPrice: 0,
        surTax: 0,
        remark: ''
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

        // 품목코드 셀에 포커스
        inOrdGrid.focus(rowKey, 'sku');
        inOrdGrid.startEditing(rowKey, 'sku');
    }
}

// 초기화 함수
function resetData() {
    document.querySelector('#dueDate').value  = '';
    document.querySelector('#custCodeSearch').value  = '';
    document.querySelector('#custNameSearch').value  = '';
    document.querySelector('#creditMax').value  = '';
    document.querySelector('#creditRemain').value  = '';
    document.querySelector('#totalSupplyPrice').value  = '';
    document.querySelector('#totalSurtax').value  = '';
    document.querySelector('#totalPrice').value  = '';
}

// 총공급액/총부가세/총액/잔여여신 계산
function sumPrice() {
    const rows = inOrdGrid.getData();

    let totalSupply = 0;  // 총공급가액
    let totalSurTax = 0;  // 총부가세

    rows.forEach(row => {
        // 공급가액 합계
        if (row.supplyPrice) {
            const sp = typeof row.supplyPrice === 'number'
                ? row.supplyPrice
                : Number(String(row.supplyPrice).replace(/,/g, ''));
            if (!isNaN(sp)) totalSupply += sp;
        }

        // 부가세 합계
        if (row.surTax) {
            const st = typeof row.surTax === 'number'
                ? row.surTax
                : Number(String(row.surTax).replace(/,/g, ''));
            if (!isNaN(st)) totalSurTax += st;
        }
    });

    const totalPrice = totalSupply + totalSurTax; // 총액

    const supplyInput   = document.getElementById('totalSupplyPrice');
    const surtaxInput   = document.getElementById('totalSurtax');
    const totalInput    = document.getElementById('totalPrice');
    const creditMaxEl   = document.getElementById('creditMax');
    const creditRemainEl= document.getElementById('creditRemain');

    // 합계 표시
    if (supplyInput)   supplyInput.value    = totalSupply.toLocaleString();
    if (surtaxInput)   surtaxInput.value    = totalSurTax.toLocaleString();
    if (totalInput)    totalInput.value     = totalPrice.toLocaleString();

    // 잔여여신 = 여신한도 - 총액
    if (creditMaxEl && creditRemainEl) {
        const max = Number(String(creditMaxEl.value).replace(/,/g, '')) || 0;
        const remain = max - totalPrice;
        creditRemainEl.value = remain.toLocaleString();
    }
}


// 나중에 삭제할 것!! ==============================================

// 품목 불러오기
function skuList() {
    fetch("/api/cm/skuList")
        .then(res => res.json())
        .then(result => {
            skuDataList = result;

            // result 에서 단가유형 목록 추출 (중복 제거)
            const map = new Map();
            skuDataList.forEach(item => {
                if (item.unitPriceType && item.typeName) {
                    map.set(item.unitPriceType, item.typeName);
                }
            });

            unitPriceTypeItems = Array.from(map.entries()).map(([value, text]) => ({
                text,   // 화면에 보이는 값 (한글)
                value   // 실제 저장되는 값 (코드)
            }));

            // Grid 에 listItems 주입
            inOrdGrid.setColumns(inOrdGrid.getColumns().map(col => {
                if (col.name === 'unitPriceType') {
                    col.editor.options.listItems = unitPriceTypeItems;
                }
                return col;
            }));


        })
        .catch(err => console.error(err));
}

