console.log("✅ menuAuth.js 로드됨");

/* ============================================================
   menuAuth.js (ROLE 기반 메뉴권한 관리 - 최종 안정판)
   ✅ 저장 유지
   ✅ 전체선택 / 전체해제 / 되돌리기 100% 정상
   ✅ Row 꼬임 없음
   ✅ 탭 이동 시 체크 유지 (fullAuthData 동기화)
   ✅ ADMIN 아닐 경우 SYSTEM 탭에서 SYS-MENU-AUTH 제거
============================================================ */

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
// ✅ 2) ROLE GRID
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

    roleGrid.on('click', ev => {

        const rowKey = ev.rowKey;
        selectedRole = roleGrid.getRow(rowKey);
        if (!selectedRole) return;

        $(".tui-grid-row").removeClass("row-selected");
        roleGrid.addRowClassName(rowKey, 'row-selected');

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

    // ✅ 체크박스는 GRID 클릭 이벤트로만 처리
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
// ✅ ✅ ✅ 핵심: 현재 탭 데이터 → 전체 데이터 동기화
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

    // ✅ 조회
    $(document).on("click", "#btnSearch", function () {
        loadRoleList();
    });

    // ✅ 초기화
    $(document).on("click", "#btnReset", function () {
        $("#searchRole").val("");
        roleGrid.resetData([]);
        authGrid.clear();
        selectedRole = null;
    });

    // ✅ 전체선택
    $(document).on("click", "#btnAllCheck", function () {

        const rows = authGrid.getData();

        rows.forEach(row => {
            const rowKey = row.rowKey;
            authGrid.setValue(rowKey, "readYn",   "Y");
            authGrid.setValue(rowKey, "createYn", "Y");
            authGrid.setValue(rowKey, "updateYn", "Y");
            authGrid.setValue(rowKey, "deleteYn", "Y");
        });
    });

    // ✅ 전체해제
    $(document).on("click", "#btnAllClear", function () {

        const rows = authGrid.getData();

        rows.forEach(row => {
            const rowKey = row.rowKey;
            authGrid.setValue(rowKey, "readYn",   "N");
            authGrid.setValue(rowKey, "createYn", "N");
            authGrid.setValue(rowKey, "updateYn", "N");
            authGrid.setValue(rowKey, "deleteYn", "N");
        });
    });

    // ✅ 되돌리기
    $(document).on("click", "#btnRevert", function () {

        if (!originalAuthSnapshot) {
            alert("되돌릴 데이터가 없습니다.");
            return;
        }

        fullAuthData = JSON.parse(JSON.stringify(originalAuthSnapshot));
        filterBySystemType();
    });

    // ✅ ✅ ✅ 저장 (저장 전에 전체 데이터 동기화)
    $(document).on("click", "#btnSaveAuth", function () {
        syncCurrentTabToFullData();
        saveRoleAuth();
    });

    // ✅ ✅ ✅ 탭 이동 (탭 이동 전에 전체 데이터 동기화)
    $(document).on("click", ".menu-tab-btn", function () {

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

        // ✅ ADMIN이면 전체 Y
        if (roleCode === 'ADMIN') {
            data.forEach(row => {
                row.readYn   = 'Y';
                row.createYn = 'Y';
                row.updateYn = 'Y';
                row.deleteYn = 'Y';
            });
        }

        // ✅ ADMIN 아닐 경우 SYS-MENU-AUTH 제거
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
