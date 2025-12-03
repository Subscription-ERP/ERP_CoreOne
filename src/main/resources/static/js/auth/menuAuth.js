/* ============================================================
   메뉴권한 관리 JS (액션 기반 저장 방식)
   상준 ERP 프로젝트 최종 버전
============================================================ */

let currentUserId = null;
let currentCompanyCode = "ROOT";  // 세션에 있으면 세션값 사용하도록 변경 가능

/* ============================================================
   1) 유저 목록 조회
============================================================ */
function loadUserList() {

    const userName = $("#searchUserName").val();
    const dept = $("#searchDept").val();
    const position = $("#searchPosition").val();

    $.ajax({
        url: "/auth/menu_permission/user_list",
        type: "GET",
        data: {
            companyCode: currentCompanyCode,
            userName: userName,
            dept: dept,
            position: position
        },
        success: function (res) {
            userGrid.resetData(res);
        },
        error: function () {
            alert("사용자 조회 중 오류가 발생했습니다.");
        }
    });
}

/* ============================================================
   2) 유저 선택 시 메뉴트리 로드
============================================================ */
function loadMenuTree(menuGroup) {

    if (!currentUserId) return;

    $.ajax({
        url: "/auth/menu_permission/menu_tree",
        type: "GET",
        data: {
            companyCode: currentCompanyCode,
            userId: currentUserId,
            menuGroup: menuGroup
        },
        success: function (tree) {
            renderMenuTree(tree, menuGroup);
        },
        error: function () {
            alert("메뉴트리 조회 중 오류가 발생했습니다.");
        }
    });
}

/* ============================================================
   3) 메뉴트리 출력
============================================================ */
function renderMenuTree(tree, menuGroup) {

    const containerId =
        menuGroup === "HR" ? "#menuTreeHr" :
        menuGroup === "SD" ? "#menuTreeSales" :
        menuGroup === "FI" ? "#menuTreeFi" :
        "#menuTreeSub";

    const container = $(containerId);
    container.empty();

    tree.forEach(item => {
        container.append(buildMenuNode(item));
    });
}

/* ============================================================
   4) 메뉴 노드 HTML 생성
============================================================ */
function buildMenuNode(node) {

    let html = `
        <div class="menu-item">
            <div class="title">${node.menuName}</div>
            <div class="auth-checks">
    `;

    node.actions.forEach(action => {
        html += `
            <label>
                <input type="checkbox"
                    class="auth-check"
                    data-menu="${node.menuCode}"
                    data-action="${action.actionCode}"
                    ${action.checked ? "checked" : ""}>
                ${action.actionCode}
            </label>
        `;
    });

    html += `</div>`;

    if (node.children && node.children.length > 0) {
        html += `<div class="menu-children">`;

        node.children.forEach(child => {
            html += buildMenuNode(child);
        });

        html += `</div>`;
    }

    html += `</div>`;

    return html;
}

/* ============================================================
   5) 저장 버튼 클릭 시 액션 기반 JSON 구성
============================================================ */
$("#btnSaveAuth").on("click", function () {

    if (!currentUserId) {
        alert("사원을 먼저 선택하세요.");
        return;
    }

    let saveList = [];

    $(".auth-check").each(function () {
        const menuCode = $(this).data("menu");
        const actionCode = $(this).data("action");
        const checked = $(this).is(":checked") ? "Y" : "N";

        saveList.push({
            menuCode: menuCode,
            actionCode: actionCode,
            authYn: checked,
            companyCode: currentCompanyCode,
            userId: currentUserId
        });
    });

    $.ajax({
        url: "/auth/menu_permission/save",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(saveList),
        success: function () {
            alert("메뉴권한이 저장되었습니다.");
        },
        error: function () {
            alert("저장 중 오류가 발생했습니다.");
        }
    });
});

/* ============================================================
   6) 전체선택 / 전체해제
============================================================ */
$("#btnAllCheck").on("click", function () {
    $(".auth-check").prop("checked", true);
});

$("#btnAllClear").on("click", function () {
    $(".auth-check").prop("checked", false);
});

/* ============================================================
   7) 유저 그리드 클릭 이벤트
============================================================ */
const userGrid = new tui.Grid({
    el: document.getElementById("userGrid"),
    scrollX: false,
    scrollY: true,
    columns: [
        { header: "사원ID", name: "userId", width: 100 },
        { header: "이름", name: "userName", width: 120 },
        { header: "부서", name: "deptName", width: 120 },
        { header: "직급", name: "positionName", width: 100 }
    ],
    bodyHeight: "fitToParent"
});

userGrid.on("click", function (ev) {
    if (!ev.rowKey) return;

    const row = userGrid.getRow(ev.rowKey);
    currentUserId = row.userId;

    loadMenuTree("HR");
});

/* ============================================================
   8) 탭 클릭 이벤트 처리
============================================================ */
$(".menu-tab-btn").on("click", function () {
    const tab = $(this).data("tab");

    $(".menu-tab-btn").removeClass("active");
    $(this).addClass("active");

    if (tab === "tab-hr") loadMenuTree("HR");
    if (tab === "tab-sales") loadMenuTree("SD");
    if (tab === "tab-fi") loadMenuTree("FI");
    if (tab === "tab-sub") loadMenuTree("SUB");
});

/* ============================================================
   9) 초기 데이터 로딩
============================================================ */
$(document).ready(function () {
    loadUserList();
});
