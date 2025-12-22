const btnReset = document.getElementById('btnReset');
const btnSearch = document.getElementById('btnSearch');
const inordDateFrom = document.getElementById('inordDateFrom');
const inordDateTo = document.getElementById('inordDateTo');
const searchCustCode = document.getElementById('searchCustCode');
const searchCustName = document.getElementById('searchCustName');

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

            const custCode = searchCustCode.value.trim();
            const custName = searchCustName.value.trim();
            const inordDateFromValue = inordDateFrom.value;
            const inordDateToValue = inordDateTo.value;

            if (status === 'DONE') {
                btnOutPut.disabled = true;
            } else {
                btnOutPut.disabled = false;
            }

            loadInOrdHeader(status, custCode, custName, inordDateFromValue, inordDateToValue);
        });
    });
    

    /* ------------------------------------------------------------------
     * 출고버튼
     * ------------------------------------------------------------------ */
    const btnOutPut = document.getElementById('btnOutPut');

    btnOutPut.addEventListener('click', async (e) => {
        e.preventDefault();

        try {
            await processOutput();
        } catch (err) {
            console.error(err);
            showToast('처리 중 오류가 발생했습니다.', 'error');
        }

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
            { header: '거래처명', name: 'custName' },
            {
                header: '품목코드',
                name: 'sku',
                align: 'center'
            },
            { header: '품목명', name: 'skuName' },
            {
                header: '수량',
                name: 'qty',
                width: 100,
                minWidth: 100,
                align: 'right',
                editor: 'text'
            },
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

    /* ------------------------------------------------------------------
     * 조회
     * ------------------------------------------------------------------ */

    // 조회 버튼 클릭
    btnSearch.addEventListener('click', () => {
        const custCode = searchCustCode.value.trim();
        const custName = searchCustName.value.trim();
        const from = inordDateFrom.value;
        const to = inordDateTo.value;

        if (from && to && from > to) {
            showToast('시작일은 종료일 이전이어야 합니다.', 'warning');
            return;
        }

        loadInOrdHeader(currentStatus, custCode, custName, from, to);
    });

    /* ------------------------------------------------------------------
     * 초기화
     * ------------------------------------------------------------------ */
    btnReset.addEventListener('click', resetSearch);

    // 초기 로드
    loadInOrdHeader('NOT_DONE', inordDateFrom.value, inordDateTo.value);



})

// 수주 헤더 정보
function loadInOrdHeader(outputStatusFilter, custCode, custName, inordDateFrom, inordDateTo) {
    const status = outputStatusFilter || 'NOT_DONE';
    let url = '/api/inOrd/info?status=' + encodeURIComponent(status);

    // 거래처 관련 검색
    if (custCode) {
        url += '&custCode=' + encodeURIComponent(custCode);
    }
    if (custName) {
        url += '&custName=' + encodeURIComponent(custName);
    }

    // 수주일자 범위 검색
    if (inordDateFrom) {
        url += '&inordDateFrom=' + encodeURIComponent(inordDateFrom);
    }
    if (inordDateTo) {
        url += '&inordDateTo=' + encodeURIComponent(inordDateTo);
    }


    fetch(url)
        .then(res => res.json())
        .then(headerList => {
            const treeData = headerList.map(h => ({
                // SelectInOrdList 결과 필드명에 맞게 매핑
                inordNo: h.inordNo,
                custName: h.custName,
                inordDate: h.inordDate,
                price: h.totalPrice,
                outputStatus: h.outputStatusName,   // 코드명 or 코드
                invoiceStatus: '',                                     // 헤더 쿼리에 없으면 일단 공백
                _children: []                                          // 자식은 나중에 채움
            }));

            grid.resetData(treeData);

            grid.getData().forEach(row => {
                grid.addRowClassName(row.rowKey, 'row-header');
            });
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
                custName: d.custName,
                sku: d.sku,
                skuName: d.skuName,
                qty: d.qty,
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

    // 출고처리 이후에도 검색 조건 유지
    const custCode = searchCustCode.value.trim();
    const custName = searchCustName.value.trim();
    const inordDateFromValue = inordDateFrom.value;
    const inordDateToValue = inordDateTo.value;
    
    loadInOrdHeader(currentStatus, custCode, custName, inordDateFromValue, inordDateToValue);

    return res;
}

function getSelectedRowsFromGrid(grid) {
    const rowKeys = grid.getCheckedRowKeys();
    return rowKeys.map(key => grid.getRow(key));
}

function resetSearch() {
    inordDateFrom.value = '';
    inordDateTo.value = '';
    searchCustCode.value = '';
    searchCustName.value = '';

    loadInOrdHeader(
        currentStatus,
        inordDateFrom.value,
        inordDateTo.value,
        searchCustCode.value,
        searchCustName.value
    );
}
