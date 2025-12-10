let grid;
let currentStatus = 'NOT_DONE';

document.addEventListener('DOMContentLoaded', () => {
    /* ------------------------------------------------------------------
     * 탭 전환
     * ------------------------------------------------------------------ */
    document.querySelectorAll(".tab-btn").forEach((btn) => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
            document.querySelectorAll(".tab-panel").forEach(p => p.classList.remove("active"));

            btn.classList.add("active");
            const targetId = "tab-" + btn.dataset.tab;
            const panel = document.getElementById(targetId);
            if (panel) panel.classList.add("active");

            const status = btn.dataset.status;
            currentStatus = status;

            if (status === 'DONE') {
                btnOutPut.disabled = true;
            } else {
                btnOutPut.disabled = false;
            }

            loadInOrdHeader(status);
        });
    });
    

    /* ------------------------------------------------------------------
     * 출고버튼
     * ------------------------------------------------------------------ */
    const btnOutPut = document.getElementById('btnOutPut');

    btnOutPut.addEventListener('click', (e) => {
        e.preventDefault();
        processOutput()
            .then(r => r.json())
            .then(result => {
                console.log(result);
            })
            .catch(err => console.error(err));

    });


    /* ------------------------------------------------------------------
     * 목록
     * ------------------------------------------------------------------ */
    const Grid = tui.Grid;

    Grid.applyTheme('clean');

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
            useCascadingCheckbox: false
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

    grid.on('expand', e => {
        loadInOrdDetail(e.rowKey);
    });

    // 부모 체크/해제 시 자식도 같이 체크/해제
    grid.on('check', e => {
        const { rowKey } = e;
        const children = grid.getDescendantRows(rowKey);   // 모든 자식(손자 포함)
        children.forEach(row => {
            grid.check(row.rowKey);
        });
    });

    grid.on('uncheck', ev => {
        const { rowKey } = ev;
        const children = grid.getDescendantRows(rowKey);
        children.forEach(row => {
            grid.uncheck(row.rowKey);
        });
    });

    loadInOrdHeader('NOT_DONE');



})

// 수주 헤더 정보
function loadInOrdHeader(outputStatusFilter) {
    const url = '/api/inOrd/info?status=' + encodeURIComponent(outputStatusFilter);

    fetch(url)
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

// 수주 세부 정보
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

async function processOutput() {
    if (currentStatus === 'DONE') {
        showToast('출고완료 건은 다시 출고할 수 없습니다.', 'error');
        return;
    }

    const checked = getSelectedRowsFromGrid(grid);
    if (!checked.length) {
        showToast('출고할 수주를 선택하세요.', 'warning');
        return;
    }

    const inordNo = checked[0].inordNo;

    const details = checked.map(row => ({
        inordNo: row.inordNo,
        inordDetailNo: row.inordDetailNo
    }));

    const payload = { inordNo, details };

    const res = await fetch('/api/inOrd/output', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (!res.ok) {
        showToast('출고 처리에 실패했습니다.', 'error');
        return;
    }

    loadInOrdHeader('NOT_DONE');
}


function getSelectedRowsFromGrid(grid) {
    const rowKeys = grid.getCheckedRowKeys();
    return rowKeys.map(key => grid.getRow(key));
}
