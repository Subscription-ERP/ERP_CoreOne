
// 거래처 모달
const custModal = document.getElementById('custModal');
const btnOpenCustModal = document.getElementById('btnOpenCustModal');
const btnCustClose = document.getElementById('btnCustClose');
const btnCustCancel = document.getElementById('btnCustCancel');
const backdrop = custModal.querySelector('.modal-layer__backdrop');

const grid = new tui.Grid({
    el: document.getElementById('invoice-grid'),
    scrollX: true,
    scrollY: true,
    bodyHeight: 'fitToParent',
    rowHeaders: ['rowNum'],

    columns: [
        { header:'품번', name:'품번', minWidth:150 },
        { header:'품명', name:'품명', minWidth:150 },
        { header:'출고일자', name:'출고일자', minWidth:140 },
        { header:'출고수량', name:'출고수량', minWidth:120, align:'right' },
        { header:'단가', name:'단가', minWidth:120, align:'right' },
        { header:'공급가액', name:'공급가액', minWidth:140, align:'right' },
        { header:'세액', name:'세액', minWidth:120, align:'right' }
    ]
});

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

// 열기 버튼
btnOpenCustModal.addEventListener('click', openCustModal);

// 닫기 버튼들
btnCustClose.addEventListener('click', closeCustModal);
btnCustCancel.addEventListener('click', closeCustModal);

// 배경 클릭 시 닫기 (옵션)
backdrop.addEventListener('click', closeCustModal);