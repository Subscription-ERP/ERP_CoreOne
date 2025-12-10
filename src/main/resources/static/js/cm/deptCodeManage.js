/**
 * deptCodeManage.js
 */

/* ==========
 * 부서목록조회
 * ========== */
const deptGrid = new tui.Grid({
	el: document.getElementById("deptGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/deptList",
				method: "GET",
			},
		},
	},
	rowkey: "userId",
	bodyHeight: 740,
	columns: [
		{ header: "부서코드", name: "userId", align: "center", sortable: true, },
		{ header: "부서명", name: "userName", sortable: true, },
		{ header: "상위 부서코드", name: "userName", sortable: true, },
		{ header: "상위 부서명", name: "dept", sortable: true, align: "center" },
		{ header: "부서 레벨", name: "deptName", sortable: true, },
		{ header: "적용시작일", name: "deptName", sortable: true, },
		{ header: "적용종료일", name: "deptName", sortable: true, },
		{ header: "상태", name: "jobTitle", sortable: true, },
		{ header: "부서관리자", name: "position", sortable: true, },
	],
}); // end of deptGrid

document.addEventListener("DOMContentLoaded", () => {
	
});