let COMPANY_CODE = '';
let userGrid;
let authGrid;

let selectedUser = null;

// ✅ 전체 원본 데이터 (TAB 기준 재필터링용)
let fullAuthData = [];

// ✅ 현재 활성 TAB
let currentSystemType = 'SYSTEM';

$(document).ready(function () {

    COMPANY_CODE = $("#sessionCompanyCode").val();
    if (!COMPANY_CODE) COMPANY_CODE = "ROOT";

    initUserGrid();
    initAuthGrid();
    bindEvents();
});

/* ================= USER GRID ================= */

function initUserGrid() {

    userGrid = new tui.Grid({
        el: document.getElementById('userGrid'),
        scrollX: true,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            { header: '사번',   name: 'userId',   width: 120, align: 'center' },
            { header: '사원명', name: 'userName', width: 120, align: 'center' },
            { header: '부서',   name: 'dept',     width: 120, align: 'center' },
            { header: '직급',   name: 'position', width: 120, align: 'center' },
            { header: 'ROLE',   name: 'roleName', width: 160, align: 'center' }
        ]
    });

    userGrid.on('click', function (ev) {

        const row = userGrid.getRow(ev.rowKey);
        if (!row || !row.roleCode) {
            authGrid.resetData([]);
            fullAuthData = [];
            return;
        }

        selectedUser = row;
        loadRoleMenuGrid(row.roleCode);
    });
}

/* ================= AUTH GRID ================= */

function initAuthGrid() {

    authGrid = new tui.Grid({
        el: document.getElementById('authGrid'),
        scrollX: false,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            // ✅ 가운데 정렬
            { header: '메뉴코드', name: 'menuCode', width: 170, align: 'center' },

            // ✅ 메뉴명 칸 더 넓게 + 가운데 정렬
            { header: '메뉴명',   name: 'menuName', width: 340, align: 'center' },

            // ✅ 네 칸 완전 동일 + 대비되는 오른쪽 정렬 느낌
            { header: '조회', name: 'readYn',   width: 80, align: 'center', formatter: checkboxFormatter },
            { header: '등록', name: 'createYn', width: 80, align: 'center', formatter: checkboxFormatter },
            { header: '수정', name: 'updateYn', width: 80, align: 'center', formatter: checkboxFormatter },
            { header: '삭제', name: 'deleteYn', width: 80, align: 'center', formatter: checkboxFormatter }
        ]
    });
}

function checkboxFormatter({ value }) {
    return `<input type="checkbox" class="grid-chk" ${value === 'Y' ? 'checked' : ''}>`;
}

/* ================= EVENT ================= */

function bindEvents() {

    $('#btnSearch').on('click', loadUserList);

    $('#btnReset').on('click', function () {
        $('#searchUserName').val('');
        $('#searchDept').val('');
        $('#searchPosition').val('');
        userGrid.resetData([]);
        authGrid.resetData([]);
        selectedUser = null;
        fullAuthData = [];
    });

    $('#btnAllCheck').on('click', () => $('.grid-chk').prop('checked', true));
    $('#btnAllClear').on('click', () => $('.grid-chk').prop('checked', false));
    $('#btnRevert').on('click', restoreSnapshot);

    // ✅ TAB 클릭
    $('.menu-tab-btn').on('click', function () {

        $('.menu-tab-btn').removeClass('active');
        $(this).addClass('active');

        currentSystemType = $(this).data('system');
        applyTabFilter();
    });
}

/* ================= USER SEARCH ================= */

function loadUserList() {

    const param = {
        userName: $('#searchUserName').val(),
        dept: $('#searchDept').val(),
        position: $('#searchPosition').val()
    };

    $.get('/auth/menu_permission/api/user', param, function (data) {
        userGrid.resetData(data);
        authGrid.resetData([]);
        fullAuthData = [];
        selectedUser = null;
    });
}

/* ================= ROLE → MENU ================= */

function loadRoleMenuGrid(roleCode) {

    $.get('/auth/menu_permission/api/role-menu', { roleCode }, function (data) {

        fullAuthData = data;
        applyTabFilter();
    });
}

/* ================= TAB FILTER ================= */

function applyTabFilter() {

    if (!fullAuthData.length) {
        authGrid.resetData([]);
        return;
    }

    const filtered = fullAuthData.filter(menu => {
        return menu.systemType === currentSystemType;
    });

    authGrid.resetData(filtered);
}

/* ================= 되돌리기 ================= */

function restoreSnapshot() {
    if (!fullAuthData.length) return;
    applyTabFilter();
}
