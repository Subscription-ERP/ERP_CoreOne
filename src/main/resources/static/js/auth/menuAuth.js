/*********************************
 * 사용자별 메뉴권한 관리 JS
 *********************************/

// 전역 변수
let userGrid;
let selectedUserId = null;

// =========================
// 1. 페이지 로드시 초기화
// =========================
window.onload = function () {
    initUserGrid();
    bindEvents();
};


// =========================
// 2. 사용자 Grid 초기화
// =========================
function initUserGrid() {
    userGrid = new tui.Grid({
        el: document.getElementById('userGrid'),
        scrollX: false,
        scrollY: true,
        bodyHeight: 480,
        rowHeaders: ['rowNum'],
        columns: [
            { header: '사원ID', name: 'userId', width: 120 },
            { header: '이름', name: 'userName', width: 100 },
            { header: '부서', name: 'deptName', width: 100 },
            { header: '직급', name: 'positionName', width: 100 },
            { header: '상태', name: 'status', width: 80 }
        ]
    });

    // 행 클릭 시 메뉴트리 조회
    userGrid.on('click', ev => {
        const row = userGrid.getRow(ev.rowKey);
        if (!row) return;

        selectedUserId = row.userId;
        loadMenuTree('HR');
        loadMenuTree('SALES');
        loadMenuTree('FI');
    });
}


// =========================
// 3. 이벤트 바인딩
// =========================
function bindEvents() {

    // 조회
    document.getElementById('btnSearch').addEventListener('click', loadUserList);

    // 초기화
    document.getElementById('btnReset').addEventListener('click', () => {
        document.getElementById('searchUserName').value = '';
        document.getElementById('searchDept').value = '';
        document.getElementById('searchPosition').value = '';
    });

    // 탭 클릭
    document.querySelectorAll('.menu-tab-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            document.querySelectorAll('.menu-tab-btn').forEach(el => el.classList.remove('active'));
            this.classList.add('active');

            document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
            document.getElementById(this.dataset.tab).classList.add('active');
        });
    });

    // 전체 선택
    document.getElementById('btnAllCheck').addEventListener('click', () => {
        document.querySelectorAll('.auth-check input[type=checkbox]')
            .forEach(chk => chk.checked = true);
    });

    // 전체 해제
    document.getElementById('btnAllClear').addEventListener('click', () => {
        document.querySelectorAll('.auth-check input[type=checkbox]')
            .forEach(chk => chk.checked = false);
    });

    // 저장
    document.getElementById('btnSaveAuth').addEventListener('click', saveMenuAuth);
}



// =========================
// 4. 사원 목록 조회
// =========================
function loadUserList() {

    const params = {
        userName: document.getElementById('searchUserName').value,
        dept: document.getElementById('searchDept').value,
        position: document.getElementById('searchPosition').value
    };

    fetch('/auth/menu-permission/users?' + new URLSearchParams(params))
        .then(res => res.json())
        .then(data => {
            userGrid.resetData(data);
        });
}



// =========================
// 5. 메뉴 트리 조회
// =========================
function loadMenuTree(group) {

    if (!selectedUserId) {
        alert("먼저 사원을 선택해주세요.");
        return;
    }

    const container = {
        'HR': 'menuTreeHr',
        'SALES': 'menuTreeSales',
        'FI': 'menuTreeFi'
    }[group];

    fetch(`/auth/menu-permission/tree?userId=${selectedUserId}&menuGroup=${group}`)
        .then(res => res.json())
        .then(list => {
            renderMenuTree(list, container);
        });
}


// =========================
// 6. 메뉴트리 HTML 렌더링
// =========================
function renderMenuTree(list, containerId) {

    const container = document.getElementById(containerId);
    container.innerHTML = '';

    const map = {};

    list.forEach(item => {
        if (!map[item.parentMenuCode]) map[item.parentMenuCode] = [];
        map[item.parentMenuCode].push(item);
    });

    function drawTree(parentCode, parentEl) {

        if (!map[parentCode]) return;

        map[parentCode].forEach(menu => {
            const div = document.createElement('div');
            div.className = 'menu-item';

            div.innerHTML = `
                <span class="title">${menu.menuName}</span>
                <span class="auth-checks">
                    <label class="auth-check">조회 <input type="checkbox" data-menu="${menu.menuCode}" data-type="R" ${menu.readAuth === 'Y' ? 'checked':''}></label>
                    <label class="auth-check">등록 <input type="checkbox" data-menu="${menu.menuCode}" data-type="C" ${menu.createAuth === 'Y' ? 'checked':''}></label>
                    <label class="auth-check">수정 <input type="checkbox" data-menu="${menu.menuCode}" data-type="U" ${menu.updateAuth === 'Y' ? 'checked':''}></label>
                    <label class="auth-check">삭제 <input type="checkbox" data-menu="${menu.menuCode}" data-type="D" ${menu.deleteAuth === 'Y' ? 'checked':''}></label>
                </span>
            `;

            parentEl.appendChild(div);

            const child = document.createElement('div');
            child.className = 'menu-children';
            div.appendChild(child);

            drawTree(menu.menuCode, child);
        });
    }

    drawTree(null, container);
}



// =========================
// 7. 저장
// =========================
function saveMenuAuth() {

    if (!selectedUserId) {
        alert("먼저 사원을 선택해주세요.");
        return;
    }

    const authList = [];

    document.querySelectorAll('input[data-menu]').forEach(chk => {
        const menu = chk.dataset.menu;
        const type = chk.dataset.type;
        const checked = chk.checked;

        let target = authList.find(a => a.menuCode === menu);
        if (!target) {
            target = {
                companyCode: "ROOT",
                userId: selectedUserId,
                menuCode: menu,
                readAuth: "N",
                createAuth: "N",
                updateAuth: "N",
                deleteAuth: "N"
            };
            authList.push(target);
        }

        if (type === 'R') target.readAuth = checked ? 'Y' : 'N';
        if (type === 'C') target.createAuth = checked ? 'Y' : 'N';
        if (type === 'U') target.updateAuth = checked ? 'Y' : 'N';
        if (type === 'D') target.deleteAuth = checked ? 'Y' : 'N';
    });

    fetch(`/auth/menu-permission/save?userId=${selectedUserId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(authList)
    })
        .then(res => res.text())
        .then(() => alert("저장되었습니다."));
}
