let grid;

document.addEventListener('DOMContentLoaded', () => {
    const Grid = tui.Grid;

    Grid.applyTheme('clean');

    /* 수주 품목 목록 */

    // grid 정보
    grid = new Grid({
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
            {
                header: '거래처코드',
                name: 'custCode',
                align: 'center'
            },
            { header: '거래처명', name: 'custName' },
            {
                header: '품목코드',
                name: 'sku',
                align: 'center'
            },
            { header: '품목명', name: 'skuName' },
            {
                header: '수주일자',
                name: 'inordDate',
                align: 'center'
            },
            {
                header: '수주금액',
                name: 'price',
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

    inordListGridData();


})

// flatList: /api/inOrd/detail 에서 넘어오는 조인 결과 배열
function inordDetailData(flatList) {
    const map = {};
    const roots = [];

    flatList.forEach(row => {
        const inordNo = row.inordNo;

        // 부모 생성 (수주번호 기준)
        if (!map[inordNo]) {
            map[inordNo] = {
                // 부모에 필요한 필드들
                inordNo: row.inordNo,          // 수주번호
                custCode: row.custCode,        // 거래처코드
                custName: row.custName,        // 거래처명
                inordDate: row.inordDate,      // 수주일자
                price: row.price,              // 수주금액 (합계 금액 필드명에 맞춰 변경)
                outputStatus: row.outputStatus,
                invoiceStatus: row.invoiceStatus,
                _children: []
            };
            roots.push(map[inordNo]);
        }

        // 상세가 있는 경우만 자식 추가
        if (row.inordDetailNo) {
            map[inordNo]._children.push({
                // 자식(수주 상세)에 필요한 필드들
                inordNo: row.inordNo,              // 필요하면 같이 보여줄 수 있음
                inordDetailNo: row.inordDetailNo,  // 수주상세번호
                custCode: row.custCode,
                custName: row.custName,
                sku: row.sku,                      // 품목코드
                skuName: row.skuName,              // 품목명
                inordDate: row.inordDate,
                price: row.price,                  // 상세 금액 또는 단가/금액 중 선택
                outputStatus: row.outputStatus,
                invoiceStatus: row.invoiceStatus
            });
        }
    });

    return roots;
}


function inordListGridData() {
    fetch('/api/inOrd/detail')
        .then(res => res.json())
        .then(flatList => {
            const treeData = inordDetailData(flatList);
            console.log('treeData', treeData);
            grid.resetData(treeData);
        })
        .catch(console.error);
}