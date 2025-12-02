document.addEventListener("DOMContentLoaded", function () {

    const btnOpenCustModal = document.getElementById('btnOpenCustModal');
    const custCodeSearch = document.getElementById('custCodeSearch');
    const custNameSearch = document.getElementById('custNameSearch');

    // grid 정보
    const grid = new tui.Grid({
        el: document.getElementById('inOrdGrid'),
        rowHeaders: ['checkbox'],
        bodyHeight: 300,
        scrollX: false,
        scrollY: true,
        columns: [
            {
                sortingType: 'asc',
                sortable: true,
                header: '품목코드',
                name: 'sku',
                align: 'center',
                editor: 'text'
            },
            { header: '품목명', name: 'skuName' },
            {
                header: '규격',
                name: 'spec',
                align: 'right'
            },
            {
                header: '수량',
                name: 'qty',
                align: 'right'
            },
            { header: '단가유형', name: 'unitPriceType' }, // selectBox
            {
                header: '단가',
                name: 'unitPrice',
                align: 'right'
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
            { header: '비고  ', name: 'remark' },
        ],
        data: []
    });

    // 배경 클릭 시 닫기 (옵션)
    // backdrop.addEventListener('click', closeCustModal);

    // 버튼 눌렀을 때: 입력값으로 먼저 검색
    btnOpenCustModal.addEventListener('click', () => {
        const custCodeKeyword = custCodeSearch.value.trim();
        const custNameKeyword = custNameSearch.value.trim();

        if (!custCodeKeyword && !custNameKeyword) {
            openCustModal();
            getCustList(); // 전체 목록 불러오기
            return;
        }

        // 1) 모달 열기
        openCustModal();

        // 2) 모달 쪽 검색 인풋에 값 주입
        const schCustCode = document.getElementById('schCustCode');
        const schCustName = document.getElementById('schCustName');

        if (schCustCode) schCustCode.value = custCodeKeyword;
        if (schCustName) schCustName.value = custNameKeyword;

        // 3) 모달 JS의 검색 함수 호출
        if (typeof searchCust === 'function') {
            searchCust();
        } else {
            console.error('searchCust 함수가 로드되지 않았습니다.');
        }
    });

    // 닫기 버튼들
    btnCustClose.addEventListener('click', closeCustModal);
    btnCustCancel.addEventListener('click', closeCustModal);

    // 모달에서 거래처 값 불러오기
    window.handleSelectedCust = function(row) {
        custCodeSearch.value = row.custCode;
        custNameSearch.value = row.custName;

        // document.getElementById('creditMax').value = row.creditMax;
    };

    // 바로 실행
    custListData();

    // 함수

    // 거래처 정보 불러오기 (거래처코드, 상호, 여신한도)
    function custListData() {
        fetch('/api/sd/cust')
            .then(res => res.json())
            .then(result => {
                console.log(result);

            })
            .catch(err => console.error(err));
    }
});
