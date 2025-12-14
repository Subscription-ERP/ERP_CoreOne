// ================================
// 조회년월 기본값: 오늘 년월 자동 설정
// ================================
(function setDefaultMonth() {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, "0");
    document.getElementById("searchMonth").value = `${yyyy}-${mm}`;
})();

const grid = new tui.Grid({
    el: document.getElementById("journal-grid"),
    data: [],
    scrollX: false,
    scrollY: true,
    rowHeaders: ['rowNum'],
    bodyHeight: "fitToParent",
    columns: [
        { header: '일자', name: 'slipDate', width: 120 },
        { header: '회계계정', name: 'slipAccount', hidden:true },
		{ header: '회계계정', name: 'accountName', width: 150 },
        { header: '차변금액', name: 'debitAmount', width: 120, align:'right', formatter: ({value}) => value ? Number(value).toLocaleString() : ""},
        { header: '대변금액', name: 'creditAmount', width: 120, align:'right', formatter: ({value}) => value ? Number(value).toLocaleString() : "" },
        { header: '거래처', name: 'custName', width: 120 },
        { header: '적요', name: 'remark', minWidth: 200 }
    ]
});
function searchJournal() {

    const month = document.getElementById("searchMonth").value;
    const accountCode = document.getElementById("searchAccount").value;

    // --- 유효성 검사 ---
    if (!month) {
        alert("조회년월을 선택해주세요.");
        return;
    }

    // 조회 조건
    const params = new URLSearchParams({
        yearMonth: month.replace("-", ""),   // 2025-11 → 202511
        accountCode: accountCode
    });

    fetch("/api/fi/monthslip?" + params)
        .then(res => res.json())
        .then(result => {
			console.log(result)
            if (!result) {
                grid.resetData([]);
                return;
            }

            // 그리드 데이터 세팅
            grid.resetData(result);
        })
        .catch(err => {
            console.error("조회 오류:", err);
            alert("조회 중 오류가 발생했습니다.");
        });
}



document.addEventListener("DOMContentLoaded", function () {
	document.getElementById("searchMonth").value = new Date().toISOString().slice(0, 7);
	document.getElementById("btnSearch").addEventListener("click", searchJournal);
	document.getElementById("btnExcel").addEventListener("click", function () {
	    grid.export("xlsx", {
	        fileName: "월별분개장"
	    });
	});
});