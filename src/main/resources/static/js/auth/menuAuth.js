/* ============================================================
   menuAuth.js  (2025-12 최신 안정본)
============================================================ */

let COMPANY_CODE = "";
let userGrid, roleGrid;
let selectedRole = null;

/* ============================================================
   초기 실행
============================================================ */
$(document).ready(function () {
    COMPANY_CODE = $("#sessionCompanyCode").val();
    if (!COMPANY_CODE) COMPANY_CODE = "0000";

    initUserGrid();
    initRoleGrid();

    loadUserList();
    loadRoleList();

    bindEvents();
});

/* ============================================================
   1) USER GRID
============================================================ */
function initUserGrid() {
    userGrid = new tui.Grid({
        el: document.getElementById("userGrid"),
        scrollX: true,
        scrollY: true,
        rowHeaders: ["checkbox"],
        bodyHeight: 600,
        columns: [
            { header: "사번", name: "userId", width: 120, align: "center" },
            { header: "사원명", name: "userName", width: 100, align: "center" },
            { header: "부서", name: "dept", width: 100, align: "center" },
            { header: "직급", name: "position", width: 80, align: "center" },
            { header: "ROLE", name: "roleCode", width: 80, align: "center" },
            { header: "ROLE명", name: "roleName", width: 120, align: "center" }
        ]
    });
}

function loadUserList() {
    $.ajax({
        url: "/auth/menu_permission/api/user",
        type: "GET",
        data: {
            companyCode: COMPANY_CODE,
            userName: $("#srchUserName").val()
        },
        success: function (res) {
            userGrid.resetData(res);
        }
    });
}

/* ============================================================
   2) ROLE GRID
============================================================ */
function initRoleGrid() {
    roleGrid = new tui.Grid({
        el: document.getElementById("roleGrid"),
        scrollX: false,
        scrollY: true,
        bodyHeight: 600,
        columns: [
            { header: "ROLE CODE", name: "roleCode", width: 100, align: "center" },
            { header: "ROLE NAME", name: "roleName", width: 140 }
        ]
    });

    roleGrid.on("click", (ev) => {
        const row = roleGrid.getRow(ev.rowKey);
        if (!row) return;

        selectedRole = row.roleCode;
        loadMenuAuth(selectedRole);
    });
}

function loadRoleList() {
    $.ajax({
        url: "/auth/menu_permission/role",
        type: "GET",
        data: { companyCode: COMPANY_CODE },
        success: function (roles) {
            roleGrid.resetData(roles);
        }
    });
}

/* ============================================================
   3) 메뉴권한 로드
============================================================ */
function loadMenuAuth(roleCode) {
    $.ajax({
        url: "/auth/menu_permission/menu",
        type: "GET",
        data: {
            companyCode: COMPANY_CODE,
            roleCode: roleCode
        },
        success: function (list) {
            renderMenuTree(list);
        }
    });
}

function renderMenuTree(list) {
    $("#menuTreeArea").empty();

    list.forEach(menu => {
        $("#menuTreeArea").append(`
            <div style="margin-bottom:6px;">
                <b>[${menu.systemType}]</b> ${menu.menuName} (${menu.menuCode})
                <label><input type="checkbox" class="auth-read" data-code="${menu.menuCode}" ${menu.readYn === 'Y' ? 'checked' : ''}> 조회</label>
                <label><input type="checkbox" class="auth-create" data-code="${menu.menuCode}" ${menu.createYn === 'Y' ? 'checked' : ''}> 등록</label>
                <label><input type="checkbox" class="auth-update" data-code="${menu.menuCode}" ${menu.updateYn === 'Y' ? 'checked' : ''}> 수정</label>
                <label><input type="checkbox" class="auth-delete" data-code="${menu.menuCode}" ${menu.deleteYn === 'Y' ? 'checked' : ''}> 삭제</label>
            </div>
        `);
    });
}

/* ============================================================
   4) ROLE 저장
============================================================ */
$("#btnSaveMenuAuth").on("click", function () {
    if (!selectedRole) return alert("ROLE을 선택해주세요.");

    const list = [];

    $(".auth-read").each(function () {
        const menuCode = $(this).data("code");
        list.push({
            companyCode: COMPANY_CODE,
            roleCode: selectedRole,
            menuCode: menuCode,
            readYn: $(this).is(":checked") ? "Y" : "N",
            createYn: $(`.auth-create[data-code='${menuCode}']`).is(":checked") ? "Y" : "N",
            updateYn: $(`.auth-update[data-code='${menuCode}']`).is(":checked") ? "Y" : "N",
            deleteYn: $(`.auth-delete[data-code='${menuCode}']`).is(":checked") ? "Y" : "N"
        });
    });

    $.ajax({
        url: "/auth/menu_permission/menu/save",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(list),
        success: function () {
            alert("저장 완료");
            loadMenuAuth(selectedRole);
        }
    });
});

/* ============================================================
   5) 선택 사용자에게 ROLE 부여
============================================================ */
$("#btnAssignRole").on("click", function () {
    const checked = userGrid.getCheckedRowKeys();
    if (checked.length === 0) return alert("사용자를 선택해주세요.");

    if (!selectedRole) return alert("ROLE을 선택해주세요.");

    const userList = checked.map(r => userGrid.getRow(r).userId);

    $.ajax({
        url: "/auth/menu_permission/assignRole",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            companyCode: COMPANY_CODE,
            roleCode: selectedRole,
            userIdList: userList
        }),
        success: function () {
            alert("역할 부여 완료");
            loadUserList();
        }
    });
});

/* ============================================================
   6) 이벤트 바인딩
============================================================ */
function bindEvents() {
    $("#btnUserSearch").on("click", loadUserList);
}
