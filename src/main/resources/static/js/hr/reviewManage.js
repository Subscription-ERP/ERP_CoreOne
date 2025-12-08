/**
 * reviewManage.js (인사평가관리)
 */

document.addEventListener("DOMContentLoaded", async () => {
	
		
	/* ------------------------------------------------------------------
	 * Toast UI Grid 생성
	 * ------------------------------------------------------------------ */
	let grid;

	function initReviewMasterGrid() {
		grid = new tui.Grid({
			el: document.getElementById('reviewList'),
			bodyHeight: 'fitToParent',
			scrollX: false,
			scrollY: true,
			rowHeaders: ['rowNum'],
			columns: [
				{ header: "인사평가명", name: "reviewMasterName" },
				{ header: "시작일", name: "reviewStartDate" },
				{ header: "종료일", name: "reviewEndDate" },
				{ header: "상태", name: "reviewStatus" },
				//{ header: "평가하기", name: "" },
			]
		});
	}

	
	/* ------------------------------------------------------------------
	 * 인사평가 기준관리 다건조회
	 * ------------------------------------------------------------------ */
	async function loadReviewMasterList(){
		globalLoader.style.display = "flex";
		const response = await fetch('/api/review/manage');
		const data = await response.json();
	    grid.resetData(data);
		grid.refreshLayout();
		globalLoader.style.display = "none";
	}
		
	initReviewMasterGrid();
	loadReviewMasterList();
	
	
	
	
	
	
});