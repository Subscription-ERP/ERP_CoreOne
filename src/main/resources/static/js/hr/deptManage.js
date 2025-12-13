/** 
 * 조직도 관리 페이지
 * @file deptManage.js
 * @description 회사내의 조직도를 볼 수 있으며 각 부서별 해당하는 사원들을 간단하게 조회 해 볼 수 있습니다.
 * @author 장준현
 * @version 1.0.1 // 버전을 명시하여 추후 변경 이력을 관리할 수 있습니다.
 */

/** ===========================================================================================================================
 * 계층적 데이터 변환 함수 (CEO 노드 최상위 처리)
 * @description 플랫 리스트 형태의 부서 데이터를 TUI Tree가 요구하는 계층 구조로 변환합니다. 부서깊이코드가 1인 CEO 노드를 최상위 단일 루트로 설정합니다.
 * @param {Array} list - 서버에서 가져온 flat list (DeptMasterVO 형태의 JSON)
 * @returns {Array} 계층 구조의 TUI Tree 데이터
 * =========================================================================== */
function listToTree(list) { // list는 fetch로 부터 받은 따끈따끈한 데이터임(json형태)
	const map = {}; // 여기가 이제 우리가 담을 맵객체를 선언함
	let rootNode = null; // 최상위 노드인 CEO 노드를 저장할 변수

	// 모든 노드를 TUI Tree 형식으로 초기화하고 Map에 저장 ('deptCode'를 키로)
	// 여기가 이제 fetch로 받은 데이터들을 반복문 돌리면서 하나하나 TUI TREE형식으로 바꾸는거임
	list.forEach(item => {
		// TUI Tree 형식으로 다시 수정 
		const node = {
			id: item.deptCode,
			text: item.deptName,
			children: [],
			deptLevel: item.deptLevel
		}; // 혹시나 다른 속성중에 필요한게 있으면 여기에 추가해도 됨

		// deptCode를 키로 해서 mapping하는거
		// 나중에 부모 ID(upperDeptNo)를 알 때, 자식 노드를 빠르게 찾아서 연결할 수 있도록, deptCode를 주소(키)로 해서 node 객체를 저장(매핑)해 두세요
		map[item.deptCode] = node; // 그니까 KEY:VALUE 형식인데 여기서 KEY를 DEPTCODE로 하는거임. MAP은 위에우리가 선언한 MAP객체에다가 저장하는겨

		// 부서레벨이 1인경우 명시적으로 최상위 루트로 저장
		if (item.deptLevel === '1') {
			rootNode = node;
		}
	});

	/* =================================================================================================================================
	 * 자그럼 지금 바뀐게 MAP객체에 KEY:VALUE형식으로해서 KEY는 DEPTCODE VALUE는 NODE객체가 저장된거임 다음으로 할거는 NODE객체안에있는 CHILDREN배열을 채울꺼임
	 * 부서가 상위계층 하위계층이 있으니까 하위는 CHILDREN배열에 넣어주는거임
	 * Map을 순회하며 부모-자식 관계를 연결 
	 * ================================================================================================================================= */
	list.forEach(item => {
		const node = map[item.deptCode]; // 위에서 만든 MAP객체중에서 LIST에서 해당하는 DEPTCODE를 NODE에 담는거임
		const parentId = item.upperDeptNo; // FETCH로 받은 데이터중 상위부서를 담는거임

		// 부서레벨이 1인경우, 이는 이미 rootNode로 설정되었으므로 건너뜁니다.
		if (node.deptLevel === '1') {
			return;
		}

		// 부모 노드를 찾기 (CEO 노드도 포함하여 찾습니다)
		const parent = map[parentId]; // 그럼 MAP객체 키값중에서 PARENTID에 해당하는 KEY를 찾아서 PARENT에 담는거임  

		if (parent) {
			// 부모가 map에 있으면, 부모의 children 배열에 현재 노드를 추가
			parent.children.push(node);
		}
	});

	// TUI Tree는 최상위 배열을 기대하므로, CEO 노드 하나만 담아 반환합니다.
	// 이 결과는 TUI Tree에서 CEO 노드 하나가 최상위에 표시되는 구조가 됩니다.
	return [rootNode];
}

/** ===================
 * 조직도 관리 우측 사원조회
 * @type {tui.Grid}
 * ==================== */
const deptUserGrid = new tui.Grid({
	el: document.getElementById("deptUserGrid"),
	scrollX: true,
	scrollY: true,
	data: {
		api: {
			readData: {
				url: "/api/hr/deptUserList",
				method: "GET",
			},
		},
	},
	bodyHeight: 740,
	columns: [
		{ header: "사번", name: "userId", align: "center", sortable: true, },
		{ header: "성명", name: "userName", sortable: true, },
		{ header: "부서코드", name: "dept", sortable: true, align: "center" },
		{ header: "부서명", name: "deptName", sortable: true, },
		{ header: "직위/직급", name: "jobTitle", sortable: true, },
		{ header: "직책", name: "position", sortable: true, },
	],
}); // end of deptUserGrid

document.addEventListener("DOMContentLoaded", () => {

	/* ==================
	 * TOAST UI TREE 생성
	 * ================== */
	// tui.Tree객체를 Tree라는 이름에 할당
	const Tree = tui.Tree;

	// 부서 데이터 불러오기
	fetch('/api/hr/deptStructure')
		.then(response => response.json())
		.then(data => {
			console.log(data);

			/* =====================================================================================
			 * TOAST UI TREE 설정
			 * 이걸 하는 이유는, TUI Tree가 요구하는 데이터 구조가 우리가 받아오는 구조와 다르기 때문에 변환을 해야한다.
			 * 기본적으로 json은 그냥 단순 {deptCode: 'DPT00001', companyCode: 'ROOT', deptName: 'CEO', upperDeptNo: null, deptLevel: '1', …} 요런형태임
			 * 하지만 TUI Tree는 {id: 'DPT00001', text: 'CEO', children: [...]} 요런식으로 계층구조를 가져야함
			 * childeren은 자식부서(하위부서)가 들어가야함
			 * 여기서 flat list는 단순배열형태 tree구조는 자식부모 즉 계층 형태
			 * Flat List 데이터를 Tree 구조로 변환
			 * 받은 data를 listToTree함수에 넣어서 트리구조로 변환해서 treeData에 저장함
			 * ===================================================================================== */
			const treeData = listToTree(data);

			console.log("변환된 트리 데이터:", treeData);

			// TUI Tree 인스턴스 생성
			const tree = new Tree('#tree', {
				data: treeData, // 여기가 트리 데이터를 넣는 부분
				nodeDefaultState: 'opened', // 노드 기본 상태 설정인데 어떤거냐면 처음에 열려있는상태로 할지 닫혀있는상태로 할지를 정하는거임. opened는 열림 closed는 닫힘임
			}).enableFeature('Selectable', { // 요게 그거임 노드 선택가능하게 만들어주는거 요거랑 밑에 tree.on 이거랑 연계해서 어떤 노드를 선택했는지 출력 가능
				selectedClassName: 'tui-tree-selected',
			});

			/* ================================
			 * 부서클릭했을때 우측에 해당하는 사원 조회
			 * ================================ */
			tree.on('select', function(eventData) {
				const nodeData = tree.getNodeData(eventData.nodeId);
				console.log(nodeData);
				deptUserGrid.readData(1, nodeData, true);
			});
		})
		.catch(error => {
			console.error("조직도 데이터를 가져오는 중 오류 발새: ", error);
		})

});