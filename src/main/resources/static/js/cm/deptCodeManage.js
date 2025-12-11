/**
 * deptCodeManage.js
 */

/* =====================
 * 검색 상태값 버튼 기능 구현
 * ===================== */
function getCmCodeOptionsPayRoll2(divId) {
	const keys = Object.keys(divId);
	const param = keys.map((k) => `code=${k}`).join("&");

	fetch(`/api/com/commonCodes?${param}`)
		.then((res) => res.json())
		.then((list) => {
			for (item in divId) {
				const select = document.querySelector(`#${divId[item]}`);

				if (select) {
					/* select박스에 전체 선택option 넣기 */
					const optDefault = document.createElement("option"); // 태그 생성
					optDefault.textContent = "전체"; // 화면에 보이는 글자
					optDefault.value = ""; // 실제 값
					select.appendChild(optDefault); // select박스에 추가

					// 요소가 존재할 때만 실행
					list[item].forEach((d) => {
						const opt = document.createElement("option");
						opt.value = d.code;
						opt.textContent = d.codeName;
						select.appendChild(opt);
					});
				}
			}
		})
		.catch((err) => console.error(err));
}

/* ====================
 * 검색 조회 버튼 기능 구현
 * ==================== */
function deptCodeManageBtnSearch() {
	// 각각 value값 가져오기
	const deptName = document.querySelector('#deptName').value; // 부서명
	const upperDeptName = document.querySelector('#upperDeptName').value; // 상위부서명
	const status = document.querySelector('#status').value; // 상태값
	const deptCodeManageStartLevel = document.querySelector('#deptCodeManageStartLevel').value; // 부서레벨시작값
	const deptCodeManageEndLevel = document.querySelector('#deptCodeManageEndLevel').value; // 부서레벨종료값
	
	// 조건 param값 data로 모으기
	const data = {
		deptName: deptName,
		upperDeptName: upperDeptName,
		status: status,
		deptCodeManageStartLevel: deptCodeManageStartLevel,
		deptCodeManageEndLevel: deptCodeManageEndLevel
	}
	
	// 조건 param값 data값으로 조회하고 그리드 새로고침
	deptCodeManageGrid.readData(1, data, true);
}


/* ==========
 * 부서목록조회
 * ========== */
const deptCodeManageGrid = new tui.Grid({
	el: document.getElementById("deptCodeManageGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/cm/selectDeptCodeManage",
				method: "GET",
			},
		},
	},
	rowkey: "deptCode",
	bodyHeight: 590,
	columns: [
		{ header: "부서코드", name: "deptCode", align: "center", sortable: true },
		{ header: "부서명", name: "deptName", sortable: true, },
		{ header: "상위 부서코드", name: "upperDeptNo", align: "center", sortable: true, },
		{ header: "상위 부서명", name: "upperDeptName", sortable: true, },
		{ header: "부서 레벨", name: "deptLevel", align: "right", sortable: true, },
		{ header: "적용시작일", name: "startDate", align: "center", sortable: true, },
		{ header: "적용종료일", name: "endDate", align: "center", sortable: true, },
		{ header: "상태", name: "status", sortable: true, },
		{ header: "부서관리자", name: "deptMng", sortable: true, },
	],
}); // end of deptGrid

document.addEventListener("DOMContentLoaded", () => {
	/* =================================
	 * 검색조건 부서명 공통코드 SELECT박스 반영
	 * ================================= */
	getDeptOptions2(["#deptName"]);

	/* =====================================
	 * 검색조건 상위 부서명 공통코드 SELECT박스 반영
	 * ===================================== */
	getDeptOptions2(["#upperDeptName"]);

	/* ==================================
	 * 검색조건 공통코드 상태값 SELECT박스 반영
	 * ================================== */
	getCmCodeOptionsPayRoll2({ "0R": "status" });

	/* ===================
	 * 검색 조회버튼 기능 구현
	 * =================== */
	document.querySelector('#deptCodeManageBtnSearch').addEventListener('click', () => {
		deptCodeManageBtnSearch();
	})
});