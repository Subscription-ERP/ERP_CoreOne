/**
 * deptManage.js
 */

/* ========================
 * 급여대장-조회, 급여 대장 목록 
 * ======================== */
const payrollGrid = new tui.Grid({
	el: document.getElementById("payrollGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/payrollList",
				method: "GET",
			},
		},
	},
	rowkey: "payrollPeriodCode",
	bodyHeight: 740,
	columns: [
		{ header: "사번", name: "payrollPeriod", align: "center", sortable: true, },
		{ header: "성명", name: "payrollType", sortable: true, },
		{ header: "부서명", name: "payrollName", sortable: true, },
		{ header: "직위/직급", name: "payrollDate", sortable: true, },
		{ header: "직책", name: "peopleNumber", sortable: true, },
		{ header: "전화번호", name: "salaryCalculation" },
		{ header: "email", name: "totalPayment", sortable: true },
	],
}); // end of payrollGrid

document.addEventListener("DOMContentLoaded", () => {

	/* ==================
	 * TOAST UI TREE 생성
	 * ================== */
	// tui.Tree객체를 Tree라는 이름에 할당
	const Tree = tui.Tree;

	fetch('/api/hr/deptStructure')
		.then(response => response.json())
		.then(data => {
			console.log(data);

			// ⭐ 1. Flat List 데이터를 Tree 구조로 변환
			const treeData = listToTree(data);

			console.log("변환된 트리 데이터:", treeData);

			const tree = new Tree('#tree', {
				data: treeData,
				nodeDefaultState: 'opened',
			});
		})
		.catch(error => {
			console.error("조직도 데이터를 가져오는 중 오류 발새: ", error);
		})

	// =======================================================
	// 2. 계층적 데이터 변환 함수 (CEO 노드 최상위 처리)
	// =======================================================
	/**
	 * @description 플랫 리스트 형태의 부서 데이터를 TUI Tree가 요구하는 계층 구조로 변환합니다.
	 * 'D000' 코드를 가진 CEO 노드를 최상위 단일 루트로 설정합니다.
	 * @param {Array} list - 서버에서 가져온 flat list (DeptMasterVO 형태의 JSON)
	 * @returns {Array} 계층 구조의 TUI Tree 데이터
	 */
	function listToTree(list) {
		const map = {};
		let rootNode = null; // D000 (CEO) 노드를 저장할 변수

		// 모든 노드를 TUI Tree 형식으로 초기화하고 Map에 저장 ('deptCode'를 키로)
		list.forEach(item => {
			// TUI Tree 형식으로 다시 수정 
			const node = {
				id: item.deptCode,
				text: item.deptName,
				children: [],
				deptLevel: item.deptLevel
			};
			
			// deptCode를 키로 해서 mapping하는거
			// 나중에 부모 ID(upperDeptNo)를 알 때, 자식 노드를 빠르게 찾아서 연결할 수 있도록, deptCode를 주소(키)로 해서 node 객체를 저장(매핑)해 두세요
			map[item.deptCode] = node;

			// D000 코드를 가진 노드를 명시적으로 최상위 루트로 저장
			if (item.deptCode === 'D000') {
				rootNode = node;
			}
		});

		// 2. Map을 순회하며 부모-자식 관계를 연결
		list.forEach(item => {
			const node = map[item.deptCode];
			const parentId = item.upperDeptNo;

			// 현재 노드가 CEO 노드인 경우 (D000), 이는 이미 rootNode로 설정되었으므로 건너뜁니다.
			if (node.id === 'D000') {
				return;
			}

			// 부모 노드를 찾기 (CEO 노드(D000)도 포함하여 찾습니다)
			const parent = map[parentId];

			if (parent) {
				// 부모가 map에 있으면, 부모의 children 배열에 현재 노드를 추가
				parent.children.push(node);
			}
			// 주의: D000이 아닌데 부모가 없으면(parent가 null이면) 트리에 포함되지 않습니다.
		});

		// TUI Tree는 최상위 배열을 기대하므로, CEO 노드 하나만 담아 반환합니다.
		// 이 결과는 TUI Tree에서 CEO 노드 하나가 최상위에 표시되는 구조가 됩니다.
		return rootNode ? [rootNode] : [];
	}
});