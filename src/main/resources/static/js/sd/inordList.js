document.addEventListener('DOMContentLoaded', () => {
    const Grid = tui.Grid;

    Grid.applyTheme('clean');

    /* 수주 품목 목록 */

    // grid 정보
    const grid = new tui.Grid({
        el: document.getElementById('grid'),
        data: [],
        rowHeaders: ['checkbox'],
        bodyHeight: 'fitToParent',
        scrollX: false,
        scrollY: true,
        treeColumnOptions: {
            name: 'inordNo',
            useCascadingCheckbox: true
        },
        columns: [
            {
                header: '수주번호',
                name: 'inordNo',
                align: 'center',
                sortingType: 'asc',
                sortable: true
            },
            {
                header: '수주상세번호',
                name: 'inordDetailNo',
                align: 'center'
            },
            { header: '거래처명', name: 'custName' },
            { header: '담당자', name: 'pic' },
            { header: '품목명', name: 'skuName' },
            {
                header: '수주일자',
                name: 'inordDate',
                align: 'center'
            },
            {
                header: '납기일자',
                name: 'dueDate',
                align: 'center'
            },
            {
                header: '수주금액',
                name: 'dueDate',
                align: 'center'
            },
            {
                header: '출고여부',
                name: 'outputStatus',
                align: 'center',
                width: 100,
                minWidth: 100,
            },
            {
                header: '세금계산서발행여부',
                name: 'invoiceStatus',
                align: 'center',
                width: 150,
                minWidth: 150,
            },
        ]
    });



})
