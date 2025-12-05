/* ============================================================
   menuAuth.js  (COMPANY_CODE 자동보정 포함 최신안정본)
   - 검색 조건 AJAX
   - GRID 행 선택 강조
   - 메뉴트리 렌더링
   - 전체선택/해제, 되돌리기, 저장 기능
============================================================ */

let COMPANY_CODE = '';

let userGrid;
let selectedUser = null;
let selectedRowKey = null;
let originalAuthSnapshot = null;

// 탭별 메뉴 데이터
let menuTreeData = {
    SYS: [],
    HR: [],
    SALES: [],
    FI: [],
    SUB: []
};

// ============================================================
//  초기 로딩
// ============================================================
$(document).ready(function () {
    console.log("menuAuth.js loaded");

    // ---------------------------------------------
    // 🔥 1) 회사코드 읽기 + 자동 보정
    // ---------------------------------------------
    COMPANY_CODE = $("#sessionCompanyCode").val();

    if (!COMPANY_CODE || COMPANY_CODE.trim() === '') {
        COMPANY_CODE = '0000';     
    }

    console.log("최종 COMPANY_CODE:", COMPANY_CODE);

    // ---------------------------------------------
    // 🔥 2) 왼쪽 GRID 및 이벤트 초기화
    // ---------------------------------------------
    initUserGrid();
    bindEvents();
});


// ============================================================
// 1) LEFT 사용자 GRID 초기화
// ============================================================
function initUserGrid() {

    const Grid = tui.Grid;

    userGrid = new Grid({
        el: document.getElementById('userGrid'),
        bodyHeight: 'fitToParent',
        scrollX: true,
        scrollY: true,
        rowHeight: 34,
        minBodyHeight: 300,
        columns: [
            { header: '사원번호', name: 'userId', width: 110, align: 'center' },
            { header: '사원명', name: 'userName', width: 120, align: 'center' },
            { header: '부서', name: 'dept', width: 120, align: 'center' },
            { header: '직급', name: 'jobTitle', width: 120, align: 'center' },
            { header: '직책', name: 'position', width: 120, align: 'center' },
            {
                header: '상태', name: 'userStatus', width: 90, align: 'center',
                formatter: ({ value }) => {
                    if (value === '0')
                        return '<span style="color:#0a7a20; font-weight:600;">재직</span>';
                    if (value === '1')
                        return '<span style="color:#d93025; font-weight:600;">퇴사</span>';
                    return '-';
                }
            }
        ]
    });

    // -------------------------------------------------------
    // GRID Row 클릭 → 선택 강조 + 오른쪽 메뉴권한 조회
    // -------------------------------------------------------
    userGrid.on('click', function (ev) {
        const rowKey = ev.rowKey;
        const rowData = userGrid.getRow(rowKey);
        if (!rowData) return;

        // 이전 선택 제거
        if (selectedRowKey !== null)
            userGrid.removeRowClassName(selectedRowKey, 'row-selected');

        // 선택 강조
        userGrid.addRowClassName(rowKey, 'row-selected');
        selectedRowKey = rowKey;
        selectedUser = rowData;

        console.log("선택한 사용자:", rowData);

        loadUserMenuAuth(rowData.userId);
    });
}


// ============================================================
// 2) 이벤트 바인딩
// ============================================================
function bindEvents() {

    // 🔍 조회 버튼
    $('#btnSearch').on('click', function () {
        loadUserList();
    });

    // 🧹 초기화 버튼
    $('#btnReset').on('click', function () {
        $('#searchUserName').val('');
        $('#searchDept').val('');
        $('#searchPosition').val('');

        userGrid.resetData([]);
        selectedUser = null;
        selectedRowKey = null;
    });

    // 탭 클릭
    $('.menu-tab-btn').on('click', function () {
        const targetId = $(this).data('tab');

        $('.menu-tab-btn').removeClass('active');
        $(this).addClass('active');

        $('.tab-panel').removeClass('active').hide();
        $('#' + targetId).addClass('active').show();
    });

    // 전체선택
    $('#btnAllCheck').on('click', function () {
        $('.tab-panel.active .chk-auth').prop('checked', true);
    });

    // 전체해제
    $('#btnAllClear').on('click', function () {
        $('.tab-panel.active .chk-auth').prop('checked', false);
    });

    // 되돌리기
    $('#btnRevert').on('click', function () {
        if (!originalAuthSnapshot) {
            alert("되돌릴 내용이 없습니다.");
            return;
        }
        restoreAuthFromSnapshot(originalAuthSnapshot);
    });

    // 저장
    $('#btnSaveAuth').on('click', function () {
        saveMenuAuth();
    });
}


// ============================================================
// 3) 사용자 목록 AJAX 조회
// ============================================================
function loadUserList() {

    const params = {
        userName: $('#searchUserName').val(),
        dept: $('#searchDept').val(),
        position: $('#searchPosition').val()
    };

    console.log("검색 조건:", params);

    $.ajax({
        url: '/auth/menu_permission/api/user',
        type: 'GET',
        data: params,
        success: function (data) {
            console.log("사용자 목록:", data);

            userGrid.resetData(data);

            selectedUser = null;
            selectedRowKey = null;
        },
        error: function () {
            alert("사용자 목록 조회 중 오류가 발생했습니다.");
        }
    });
}


// ============================================================
// 4) 선택된 사용자 메뉴 권한 조회
// ============================================================
function loadUserMenuAuth(userId) {

    $.ajax({
        url: '/auth/menu_permission/api/menu',
        type: 'GET',
        data: {
            companyCode: COMPANY_CODE,
            userId: userId
        },
        success: function (data) {
            console.log("메뉴 권한 데이터:", data);

            splitMenuTreeBySystemType(data);
            renderAllMenuTrees();

            // 되돌리기용 스냅샷 저장
            originalAuthSnapshot = makeAuthSnapshot();
        },
        error: function () {
            alert("메뉴 권한 조회 실패");
        }
    });
}


// ============================================================
// 5) 메뉴트리 systemType 분류
// ============================================================
function splitMenuTreeBySystemType(list) {

    menuTreeData = { SYS: [], HR: [], SALES: [], FI: [], SUB: [] };

    list.forEach(node => {
        const type = node.systemType || 'SYS';
        if (menuTreeData[type]) {
            menuTreeData[type].push(node);
        }
    });
}


// ============================================================
// 6) 전체 메뉴트리 렌더링
// ============================================================
function renderAllMenuTrees() {
    renderMenuTree(menuTreeData.SYS, $('#menuTreeSys'));
    renderMenuTree(menuTreeData.HR, $('#menuTreeHr'));
    renderMenuTree(menuTreeData.SALES, $('#menuTreeSales'));
    renderMenuTree(menuTreeData.FI, $('#menuTreeFi'));
    renderMenuTree(menuTreeData.SUB, $('#menuTreeSub'));
}


// ============================================================
// 6-1) 개별 메뉴트리 렌더링
// ============================================================
function renderMenuTree(nodes, $container) {

    $container.empty();

    nodes.forEach(node => {
        $container.append(buildMenuItem(node));
    });
}


// ============================================================
// 6-2) 재귀 기반 메뉴 Item 구성
// ============================================================
function buildMenuItem(node) {

    const $item = $('<div class="menu-item"></div>');
    const $title = $('<span class="title"></span>').text(node.menuName);

    const $checks = $('<span class="auth-checks"></span>');
    const actions = node.actions || [];

    actions.forEach(action => {
        const $label = $(`
            <label>
                <input type="checkbox"
                       class="chk-auth"
                       data-menu="${node.menuCode}"
                       data-action="${action.actionCode}">
                ${action.actionName || action.actionCode}
            </label>
        `);

        if (action.authYn === 'Y') {
            $label.find('input').prop('checked', true);
        }

        $checks.append($label);
    });

    $item.append($title).append($checks);

    if (node.children && node.children.length > 0) {
        const $childWrap = $('<div class="menu-children"></div>');
        node.children.forEach(child => {
            $childWrap.append(buildMenuItem(child));
        });
        $item.append($childWrap);
    }

    return $item;
}


// ============================================================
// 7) 되돌리기 Snapshot 생성 / 복원
// ============================================================
function makeAuthSnapshot() {
    const snapshot = {};
    $('.chk-auth').each(function () {
        const menu = $(this).data('menu');
        const action = $(this).data('action');
        const checked = $(this).prop('checked');

        if (!snapshot[menu]) snapshot[menu] = {};
        snapshot[menu][action] = checked;
    });
    return snapshot;
}

function restoreAuthFromSnapshot(snapshot) {
    $('.chk-auth').each(function () {
        const menu = $(this).data('menu');
        const action = $(this).data('action');
        $(this).prop('checked', snapshot[menu]?.[action] ?? false);
    });
}


// ============================================================
// 8) 메뉴권한 저장
// ============================================================
function saveMenuAuth() {

    if (!selectedUser) {
        return alert("먼저 사원을 선택하세요.");
    }

    const payload = [];

    $('.chk-auth').each(function () {
        payload.push({
            companyCode: COMPANY_CODE,
            userId: selectedUser.userId,
            menuCode: $(this).data('menu'),
            actionCode: $(this).data('action'),
            authYn: $(this).prop('checked') ? 'Y' : 'N'
        });
    });

    $.ajax({
        url: '/auth/menu_permission/api/save',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function () {
            alert("메뉴 권한이 저장되었습니다.");
            originalAuthSnapshot = makeAuthSnapshot();
        },
        error: function () {
            alert("저장 실패");
        }
    });
}
