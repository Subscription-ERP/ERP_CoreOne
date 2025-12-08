/* ============================================================
 ✅ USER → ROLE → MENU 권한 관리 menuAuth.js (최종 완전 안정본)
============================================================ */

let COMPANY_CODE = '';
let userGrid;
let authGrid;
let selectedUser = null;
let originalSnapshot = null;

// ✅ 페이지 초기화
$(document).ready(function () {

    COMPANY_CODE = $("#sessionCompanyCode").val();
    if (!COMPANY_CODE) COMPANY_CODE = "ROOT";

    initUserGrid();
    initAuthGrid();
    bindEvents();

    // ❌ 자동 조회 절대 금지
    // loadUserList();
});

// ============================================================
// ✅ 1) USER GRID (왼쪽)
// ============================================================
function initUserGrid() {

    userGrid = new tui.Grid({
        el: document.getElementById('userGrid'),
        scrollX: true,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            { header: '사번', name: 'userId', width: 120, align: 'center' },
            { header: '사원명', name: 'userName', width: 120, align: 'center' },
            { header: '부서', name: 'dept', width: 100, align: 'center' },
            { header: '직급', name: 'position', width: 100, align: 'center' },
            { header: 'ROLE', name: 'roleName', width: 140, align: 'center' }
        ]
    });

    // ✅ 반드시 이렇게 rowKey 체크해야 클릭이 100% 정상 동작함
    userGrid.on('click', function (ev) {

        if (ev.rowKey === null || ev.rowKey === undefined) return;

        const rowKey = ev.rowKey;
        const rowData = userGrid.getRow(rowKey);
        if (!rowData) return;

        // ✅ 이전 선택 테두리 제거
        if (selectedUser) {
            userGrid.removeRowClassName(selectedUser._rowKey, 'row-selected');
        }

        // ✅ 현재 선택 강조
        userGrid.addRowClassName(rowKey, 'row-selected');

        selectedUser = rowData;
        selectedUser._rowKey = rowKey;

        // ✅ ROLE 없는 사원 클릭 시 오른쪽 비움
        if (!rowData.roleCode) {
            authGrid.resetData([]);
            originalSnapshot = null;
            return;
        }

        loadRoleMenuGrid(rowData.roleCode);
    });
}

// ============================================================
// ✅ 2) ROLE → MENU GRID (오른쪽)
// ============================================================
function initAuthGrid() {

    authGrid = new tui.Grid({
        el: document.getElementById('authGrid'),
        scrollX: false,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            { header: '메뉴코드', name: 'menuCode', width: 200 },
            { header: 'READ', name: 'readYn', width: 80, align: 'center', formatter: checkboxFormatter },
            { header: 'CREATE', name: 'createYn', width: 80, align: 'center', formatter: checkboxFormatter },
            { header: 'UPDATE', name: 'updateYn', width: 80, align: 'center', formatter: checkboxFormatter },
            { header: 'DELETE', name: 'deleteYn', width: 80, align: 'center', formatter: checkboxFormatter }
        ]
    });
}

function checkboxFormatter({ value }) {
    return `<input type="checkbox" class="grid-chk" ${value === 'Y' ? 'checked' : ''}>`;
}

// ============================================================
// ✅ 3) 버튼 이벤트
// ============================================================
function bindEvents() {

    // ✅ 조회
    $('#btnSearch').on('click', loadUserList);

    // ✅ 초기화 (진짜 초기화)
    $('#btnReset').on('click', function () {

        $('#searchUserName').val('');
        $('#searchDept').val('');
        $('#searchPosition').val('');

        // ✅ GRID 전부 비움
        userGrid.resetData([]);
        authGrid.resetData([]);

        selectedUser = null;
        originalSnapshot = null;
    });

    $('#btnAllCheck').on('click', () => $('.grid-chk').prop('checked', true));
    $('#btnAllClear').on('click', () => $('.grid-chk').prop('checked', false));

    $('#btnRevert').on('click', restoreSnapshot);
    $('#btnSaveAuth').on('click', saveRoleMenuAuth);
}

// ============================================================
// ✅ 4) USER 조회 (조회 버튼 전용)
// ============================================================
function loadUserList() {

    const param = {
        userName: $('#searchUserName').val(),
        dept: $('#searchDept').val(),
        position: $('#searchPosition').val()
    };

    $.get('/auth/menu_permission/api/user', param, function (data) {
        userGrid.resetData(data);

        // ✅ 다시 조회했으니 오른쪽은 항상 비움
        authGrid.resetData([]);
        selectedUser = null;
        originalSnapshot = null;
    });
}

// ============================================================
// ✅ 5) ROLE → MENU 조회
// ============================================================
function loadRoleMenuGrid(roleCode) {

    $.get('/auth/menu_permission/api/role-menu', { roleCode }, function (data) {
        authGrid.resetData(data);
        originalSnapshot = JSON.stringify(data);
    });
}

// ============================================================
// ✅ 6) ROLE → MENU 저장
// ============================================================
function saveRoleMenuAuth() {

    if (!selectedUser) {
        alert("사원을 먼저 선택하세요.");
        return;
    }

    const payload = [];

    authGrid.getData().forEach((row, idx) => {

        const el = authGrid.getRowElement(idx);
        const chks = el.querySelectorAll('.grid-chk');

        payload.push({
            companyCode: COMPANY_CODE,
            roleCode: selectedUser.roleCode,
            menuCode: row.menuCode,
            readYn: chks[0].checked ? 'Y' : 'N',
            createYn: chks[1].checked ? 'Y' : 'N',
            updateYn: chks[2].checked ? 'Y' : 'N',
            deleteYn: chks[3].checked ? 'Y' : 'N'
        });
    });

    $.ajax({
        url: '/auth/menu_permission/api/role-menu/save',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function () {
            alert('ROLE 권한 저장 완료');
            originalSnapshot = JSON.stringify(payload);
        }
    });
}

// ============================================================
// ✅ 7) 되돌리기
// ============================================================
function restoreSnapshot() {
    if (!originalSnapshot) return;
    authGrid.resetData(JSON.parse(originalSnapshot));
}
