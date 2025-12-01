document.addEventListener("DOMContentLoaded", function () {

    const custSearchInput = document.getElementById('custSearch');

    // 거래처 모달
    const custModal = document.getElementById('custModal');
    const btnOpenCustModal = document.getElementById('btnOpenCustModal');
    const btnCustClose = document.getElementById('btnCustClose');
    const btnCustCancel = document.getElementById('btnCustCancel');
    const custSearch= document.getElementById('custSearch');



    // 닫기 버튼들
    btnCustClose.addEventListener('click', closeCustModal);
    btnCustCancel.addEventListener('click', closeCustModal);

    // 배경 클릭 시 닫기 (옵션)
    backdrop.addEventListener('click', closeCustModal);

    // 버튼 눌렀을 때: 입력값으로 먼저 검색
    btnOpenCustModal.addEventListener('click', () => {
        const keyword = custSearchInput.value.trim();
        if (!keyword) {
            // 아무것도 없으면 그냥 모달 열어서 전체 조회
            openCustModal();
            loadCustModalList(); // 전체 목록 불러오기
            return;
        }
        findCust(keyword);
    });


    // 함수 영역 ==================================================================

    // 거래처 모달
    function openCustModal(e) {
        if (e) {
            e.stopPropagation();
            e.preventDefault();
        }
        custModal.hidden = false;
        custModal.classList.remove('hidden');

        if (window.custModalGrid) {
            window.custModalGrid.refreshLayout();
        }
    }

    function closeCustModal() {
        custModal.hidden = true;
        custModal.classList.add('hidden');

    }
});
