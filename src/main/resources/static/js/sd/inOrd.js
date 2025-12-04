const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const custCodeSearch = document.getElementById('custCodeSearch');
const custNameSearch = document.getElementById('custNameSearch');
const btnCustClose     = document.getElementById('btnCustClose');
const backdrop = document.querySelector(".modal-layer__backdrop");
const btnAddRow = document.getElementById('btnAddRow');
let inOrdGrid;
let unitPriceTypeItems = [];

document.addEventListener("DOMContentLoaded", function () {

    /* 기본 입력 사항 */

    // 거래처 검색
    custCodeSearch.addEventListener('keydown', handleEnter);
    custNameSearch.addEventListener('keydown', handleEnter);
    btnOpenCustModal.addEventListener('click', searchCustModal);

    // 거래처 모달 닫기
    btnCustClose.addEventListener('click', closeCustModal);
    backdrop.addEventListener('click', closeCustModal)

    /* 수주 품목 목록 */

    // grid 정보
    inOrdGrid = new tui.Grid({
        el: document.getElementById('inOrdGrid'),
        rowHeaders: ['checkbox'],
        bodyHeight: 300,
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
                align: 'right',
            },
            {
                header: '단위',
                name: 'unit',
                align: 'right',
            },
            {
                header: '수량',
                name: 'qty',
                align: 'right',
                editor: 'text'
            },
            {
                header: '단가유형',
                name: 'unitPriceType',
                formatter: 'listItemText',        // 셀에는 text를 보여주기 위함[web:113][web:151]
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
                editor: 'text'
            },
        ],
        data: []
    });

    AddSkuRow();

    // editor가 설정된 컬럼만 클릭시 편집 시작
    inOrdGrid.on('click', (ev) => {
        // editor가 설정된 컬럼만 클릭시 편집 시작
        const column = inOrdGrid.getColumn(ev.columnName);
        if (column.editor) {
            inOrdGrid.startEditing(ev.rowKey, ev.columnName);
        }
    });

});

// 모달에서 거래처 값 불러오기
handleSelectedCust = function(row) {
    custCodeSearch.value = row.custCode;
    custNameSearch.value = row.custName;

    document.getElementById('creditMax').value = Number(row.creditMax).toLocaleString();
};

/* 함수 */

// 단가유형
/*function unitPriceTypeListData() {
    return fetch('/api/com/type?groupCode=UNIT_PRICE_TYPE')
        .then(res => res.json())
        .then(list => {
            console.log(list);
            unitPriceTypeItems = list.map(unit => ({
                text: unit.name,
                value: unit.code
            }))
        })
        .catch(err => console.error(err));
}*/

// 거래처 모달 검색 결과 전달 받음
function searchCustModal() {
    const custCodeKeyword = custCodeSearch.value.trim();
    const custNameKeyword = custNameSearch.value.trim();

    if (!custCodeKeyword && !custNameKeyword) {
        openCustModal();
        getCustList(); // 전체 목록 불러오기
        return;
    }

    // 1) 모달 쪽 검색 키워드 입력
    const schCustCode = document.getElementById('schCustCode');
    const schCustName = document.getElementById('schCustName');

    if (schCustCode) schCustCode.value = custCodeKeyword;
    if (schCustName) schCustName.value = custNameKeyword;

    // 2) 모달 JS의 검색 함수 호출
    if (typeof searchCust === 'function') {
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

// 추가 버튼 클릭시 행 추가
function AddSkuRow() {
    // 수주 품목 추가 ==========================================
    btnAddRow.addEventListener('click', function () {
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

        // 2) 현재 데이터에서 마지막 행의 rowKey 가져오기
        const data = inOrdGrid.getData();
        const last = data[data.length - 1];
        const rowKey = last && last.rowKey;

        console.log('calculated rowKey:', rowKey);
        console.log('columns:', inOrdGrid.getColumns());

        if (rowKey !== undefined) {
            inOrdGrid.addCellClassName(rowKey, 'spec', 'block');
            inOrdGrid.addCellClassName(rowKey, 'unit', 'block');
            inOrdGrid.addCellClassName(rowKey, 'supplyPrice', 'block');
            inOrdGrid.addCellClassName(rowKey, 'surTax', 'block');

            // 품목코드 셀에 포커스
            inOrdGrid.focusIn(rowKey, 'sku');
        }
    });
}

