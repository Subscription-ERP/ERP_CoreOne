console.log("✅ menuAuth.js 로드됨");

let COMPANY_CODE = '';

let roleGrid;
let authGrid;

let selectedRole = null;
let originalAuthSnapshot = null;
let fullAuthData = [];
let currentSystemType = 'SYSTEM';

// ============================================================
// ✅ 1) 초기 로딩
// ============================================================
$(document).ready(function () {

    COMPANY_CODE = $("#sessionCompanyCode").val();
    if (!COMPANY_CODE) COMPANY_CODE = "0000";

    initRoleGrid();
    initAuthGrid();
    bindEvents();
    loadRoleSelectBox();
});

// ============================================================
// ✅ 2) ROLE GRID (🔥 클릭 문제 완전 해결 버전)
// ============================================================
function initRoleGrid() {

    roleGrid = new tui.Grid({
        el: document.getElementById('roleGrid'),
        scrollX: false,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            { header: 'ROLE CODE', name: 'roleCode', width: 150, align: 'center' },
            { header: 'ROLE NAME', name: 'roleName', width: 200, align: 'center' }
        ]
    });

    // ✅ ✅ ✅ 클릭 이벤트 대신 focusChange 사용 (핵심 수정)
    roleGrid.on('focusChange', ev => {

        if (ev.rowKey == null) return;

        selectedRole = roleGrid.getRow(ev.rowKey);
        if (!selectedRole) return;

        $(".tui-grid-row").removeClass("row-selected");
        roleGrid.addRowClassName(ev.rowKey, 'row-selected');

        console.log("✅ 선택된 ROLE:", selectedRole.roleCode);

        loadRoleMenuAuth(selectedRole.roleCode);
    });
}

// ============================================================
// ✅ 3) AUTH GRID
// ============================================================
function initAuthGrid() {

    authGrid = new tui.Grid({
        el: document.getElementById('authGrid'),
        scrollX: false,
        scrollY: true,
        bodyHeight: 'fitToParent',
        rowHeight: 34,
        columns: [
            { header: '메뉴코드', name: 'menuCode', width: 170 },
            { header: '메뉴명', name: 'menuName', width: 180 },
            { header: '조회',   name: 'readYn',   width: 70, align: 'center', formatter: checkboxFormatter },
            { header: '등록',   name: 'createYn', width: 70, align: 'center', formatter: checkboxFormatter },
            { header: '수정',   name: 'updateYn', width: 70, align: 'center', formatter: checkboxFormatter },
            { header: '삭제',   name: 'deleteYn', width: 70, align: 'center', formatter: checkboxFormatter }
        ]
    });

    authGrid.on('click', ev => {

        if (!ev.columnName) return;

        const field = ev.columnName;
        if (!['readYn', 'createYn', 'updateYn', 'deleteYn'].includes(field)) return;

        const rowKey = ev.rowKey;
        const row    = authGrid.getRow(rowKey);

        const newValue = row[field] === 'Y' ? 'N' : 'Y';
        authGrid.setValue(rowKey, field, newValue, false);
    });
}

// ============================================================
// ✅ 4) 체크박스 렌더러
// ============================================================
function checkboxFormatter({ value }) {
    const checked = value === 'Y' ? 'checked' : '';
    return `<input type="checkbox" ${checked} onclick="return false;">`;
}

// ============================================================
// ✅ ✅ ✅ 탭 동기화
// ============================================================
function syncCurrentTabToFullData() {

    const currentRows = authGrid.getData();

    currentRows.forEach(gridRow => {

        const target = fullAuthData.find(full =>
            full.menuCode === gridRow.menuCode &&
            full.systemType === gridRow.systemType
        );

        if (target) {
            target.readYn   = gridRow.readYn;
            target.createYn = gridRow.createYn;
            target.updateYn = gridRow.updateYn;
            target.deleteYn = gridRow.deleteYn;
        }
    });
}

// ============================================================
// ✅ 5) 버튼 이벤트
// ============================================================
function bindEvents() {

    console.log("✅ bindEvents 실행됨");

    $("#btnSearch").on("click", loadRoleList);

    $("#btnReset").on("click", function () {
        $("#searchRole").val("");
        roleGrid.resetData([]);
        authGrid.clear();
        selectedRole = null;
    });

    $("#btnAllCheck").on("click", function () {

        authGrid.getData().forEach(row => {
            authGrid.setValue(row.rowKey, "readYn",   "Y");
            authGrid.setValue(row.rowKey, "createYn", "Y");
            authGrid.setValue(row.rowKey, "updateYn", "Y");
            authGrid.setValue(row.rowKey, "deleteYn", "Y");
        });
    });

    $("#btnAllClear").on("click", function () {

        authGrid.getData().forEach(row => {
            authGrid.setValue(row.rowKey, "readYn",   "N");
            authGrid.setValue(row.rowKey, "createYn", "N");
            authGrid.setValue(row.rowKey, "updateYn", "N");
            authGrid.setValue(row.rowKey, "deleteYn", "N");
        });
    });

    $("#btnRevert").on("click", function () {

        if (!originalAuthSnapshot) {
            alert("되돌릴 데이터가 없습니다.");
            return;
        }

        fullAuthData = JSON.parse(JSON.stringify(originalAuthSnapshot));
        filterBySystemType();
    });

    $("#btnSaveAuth").on("click", function () {
        syncCurrentTabToFullData();
        saveRoleAuth();
    });

    $(".menu-tab-btn").on("click", function () {

        syncCurrentTabToFullData();

        $(".menu-tab-btn").removeClass("active");
        $(this).addClass("active");

        currentSystemType = $(this).data("system");
        filterBySystemType();
    });
}

// ============================================================
// ✅ 6) ROLE SelectBox
// ============================================================
function loadRoleSelectBox() {

    $.get("/auth/menu_permission/api/role", function (data) {

        const $select = $("#searchRole");
        $select.empty().append(`<option value="">전체</option>`);

        data.forEach(role => {
            $select.append(`<option value="${role.roleCode}">${role.roleName}</option>`);
        });
    });
}

// ============================================================
// ✅ 7) ROLE 조회
// ============================================================
function loadRoleList() {

    const roleCode = $("#searchRole").val();

    $.get("/auth/menu_permission/api/role", { roleCode }, function (data) {

        roleGrid.resetData(data);
        selectedRole = null;
        authGrid.clear();
    });
}

// ============================================================
// ✅ 8) ROLE → MENU 권한 조회
// ============================================================
function loadRoleMenuAuth(roleCode) {

    $.get("/auth/menu_permission/api/role-menu", { roleCode }, function (data) {

        if (roleCode === 'ADMIN') {
            data.forEach(row => {
                row.readYn   = 'Y';
                row.createYn = 'Y';
                row.updateYn = 'Y';
                row.deleteYn = 'Y';
            });
        }

        if (roleCode !== "ADMIN") {
            data = data.filter(item => item.menuCode !== "SYS-MENU-AUTH");
        }

        fullAuthData = data;
        originalAuthSnapshot = JSON.parse(JSON.stringify(data));
        filterBySystemType();
    });
}

// ============================================================
// ✅ 9) SYSTEM 필터
// ============================================================
function filterBySystemType() {

    const filtered = fullAuthData.filter(item =>
        item.systemType === currentSystemType
    );

    authGrid.resetData(filtered);
}

// ============================================================
// ✅ 10) 저장
// ============================================================
function saveRoleAuth() {

    if (!selectedRole) {
        alert("ROLE을 먼저 선택하세요.");
        return;
    }

    const saveData = fullAuthData.map(row => ({
        companyCode: COMPANY_CODE,
        roleCode: selectedRole.roleCode,
        menuCode: row.menuCode,
        readYn:   row.readYn   === "Y" ? "Y" : "N",
        createYn: row.createYn === "Y" ? "Y" : "N",
        updateYn: row.updateYn === "Y" ? "Y" : "N",
        deleteYn: row.deleteYn === "Y" ? "Y" : "N"
    }));

    $.ajax({
        url: "/auth/menu_permission/api/role-menu/save",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(saveData),

        success: function () {
            alert("✅ 권한 저장 완료");
            loadRoleMenuAuth(selectedRole.roleCode);
        },

        error: function () {
            alert("❌ 저장 실패");
        }
    });
}