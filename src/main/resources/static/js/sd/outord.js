let outordGrid;

// 요소
const btnReset = document.getElementById('btnReset');
const btnSave = document.getElementById('btnSave');
const btnDeleteRow = document.getElementById('btnDeleteRow');
const btnAddRow = document.getElementById('btnAddRow');

const custCodeSearch = document.getElementById('custCodeSearch');
const custNameSearch = document.getElementById('custNameSearch');

document.addEventListener('DOMContentLoaded', function () {

    /* ======================================================
       기본 사항 입력
    ====================================================== */



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
                header: '총액',
                name: 'price',
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
        summary: {
            height: 40,
            position: 'bottom',
            columnContent: {
                qty: function (valueMap) {
                    return valueMap.sum;
                },
                supplyPrice: function (valueMap) {
                    return valueMap.sum;
                },
                surTax: function (valueMap) {
                    return valueMap.sum;
                },
                price: function (valueMap) {
                    return valueMap.sum;
                },
            }
        }
    });
})

/* ======================================================
   함수
====================================================== */

