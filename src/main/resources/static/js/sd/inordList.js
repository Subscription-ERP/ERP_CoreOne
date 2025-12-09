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
                width: 200,
                minWidth: 200,
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
                align: 'right',
                formatter({ value }) {
                    if (value == null) return '';
                    return Number(value).toLocaleString(); // 10000 → "10,000"[web:158][web:165]
                }
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
                width: 120,
                minWidth: 150,
            },
        ]
    });

    loadInOrdHeader();

    grid.on('expand', e => {
        loadInOrdDetail(e.rowKey);
    });


})

function loadInOrdHeader() {
    fetch('/api/inOrd/info')
        .then(res => res.json())
        .then(headerList => {
            const treeData = headerList.map(h => ({
                // SelectInOrdList 결과 필드명에 맞게 매핑
                inordNo: h.inordNo,
                custCode: h.custCode,
                custName: h.custName,
                inordDate: h.inordDate,
                price: h.totalPrice,
                outputStatus: h.outputStatusName,   // 코드명 or 코드
                invoiceStatus: '',                                     // 헤더 쿼리에 없으면 일단 공백
                _children: []                                          // 자식은 나중에 채움
            }));

            grid.resetData(treeData);
        })
        .catch(console.error);
}

function loadInOrdDetail(rowKey) {
    const parentRow = grid.getRow(rowKey);
    if (!parentRow) return;

    const inordNo = parentRow.inordNo;

    // 이미 자식이 있으면 다시 불러오지 않음
    const descendants = grid.getDescendantRows(rowKey);
    if (descendants.length) {
        console.log('이미 자식 있음, 로딩 스킵:', inordNo);
        return;
    }

    fetch(`/api/inOrd/detail?inordNo=${encodeURIComponent(inordNo)}`)
        .then(res => res.json())
        .then(detailList => {
            console.log(detailList);
            const children = detailList.map(d => ({
                inordNo: d.inordNo,
                inordDetailNo: d.inordDetailNo,
                custCode: d.custCode,
                custName: d.custName,
                sku: d.sku,
                skuName: d.skuName,
                inordDate: d.inordDate,
                price: d.price,
                outputStatus: d.outputStatusName,
                invoiceStatus: d.invoiceStatusName
            }));

            if (!children.length) {
                console.log('자식 없음:', inordNo);
                return;
            }

            children.forEach(child => {
                grid.appendRow(child, {
                    parentRowKey: rowKey
                });
            });

        })
        .catch(err => {
            console.error('수주 상세 로딩 실패:', err);
        });
}
