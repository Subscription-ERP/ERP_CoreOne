/* ============================================================
   menuAuth.js (탭 유지 + 저장/버튼 이벤트 + rowKey 꼬임 방지 최종본)
   - 원본: fullAuthData (menuCode 기준)
   - 화면: authGrid (탭 필터뷰)
   - 체크: menuCode로만 원본 수정
   - 탭 이동해도 변경 유지
   - 전체선택/전체해제/되돌리기/저장 정상 동작
============================================================ */

let COMPANY_CODE = "";
let userGrid, roleGrid, authGrid;

let selectedRoleCode = null;
let currentSystemType = "SYSTEM";

let fullAuthData = [];          // ★ 원본(ROLE 전체)
let originalAuthSnapshot = [];  // ★ 되돌리기 원본 스냅샷

$(document).ready(function () {
  COMPANY_CODE = $("#sessionCompanyCode").val() || "0000";

  initUserGrid();
  initRoleGrid();
  initAuthGrid();

  loadRoleList();   // ROLE은 화면 진입 시 미리 로딩

  bindEvents();     // 이벤트는 “위임 방식”으로 붙임(안 먹는 문제 방지)
});

/* =========================
   유틸
========================= */
const deepCopy = (obj) => JSON.parse(JSON.stringify(obj || []));

function normalizeYN(v) {
  return (v === "Y") ? "Y" : "N";
}

function isAdminRole() {
  return selectedRoleCode === "ADMIN";
}

function getFilteredMenuCodes() {
  return fullAuthData
    .filter(r => (r.systemType || "") === currentSystemType)
    .map(r => r.menuCode);
}

function applyAuthFilter() {
  if (!authGrid) return;

  // ✅ 화면에 보여줄 데이터는 “복사본”으로 넣는다 (TUI가 내부적으로 건드려도 원본 보호)
  const viewData = deepCopy(
    fullAuthData.filter(r => (r.systemType || "") === currentSystemType)
  );

  authGrid.resetData(viewData);
}

/* ============================================================
   LEFT: USER GRID
============================================================ */
function initUserGrid() {
  userGrid = new tui.Grid({
    el: document.getElementById("userGrid"),
    scrollX: true,
    scrollY: true,
    rowHeaders: ["checkbox"],
    bodyHeight: "fitToParent",
    rowHeight: 34,
    columns: [
      { header: "사번",   name: "userId",   width: 110, align: "center" },
      { header: "사원명", name: "userName", width: 90,  align: "center" },
      { header: "부서",   name: "dept",     width: 110, align: "center" },
      { header: "직급",   name: "position", width: 90,  align: "center" },
      { header: "ROLE",   name: "roleCode", width: 90,  align: "center" },
      { header: "ROLE명", name: "roleName", minWidth: 120 }
    ]
  });

  // USER 클릭 → 해당 USER의 ROLE로 권한 조회(선택 UX)
  userGrid.on("click", (ev) => {
    const row = userGrid.getRow(ev.rowKey);
    if (!row || !row.roleCode) return;

    selectedRoleCode = row.roleCode;
    highlightRoleRow(row.roleCode);
    loadRoleMenuAuth(row.roleCode);
  });
}

function loadUserList() {
  $.ajax({
    url: "/auth/menu_permission/api/user",
    type: "GET",
    data: {
      userName: $("#searchUserName").val(),
      dept: $("#searchDept").val(),
      position: $("#searchPosition").val()
    },
    success: (res) => userGrid.resetData(res || []),
    error: (xhr) => {
      console.error(xhr);
      alert("사용자 조회 중 오류가 발생했습니다.");
    }
  });
}

/* ============================================================
   CENTER: ROLE GRID
============================================================ */
function initRoleGrid() {
  roleGrid = new tui.Grid({
    el: document.getElementById("roleGrid"),
    scrollX: false,
    scrollY: true,
    bodyHeight: "fitToParent",
    rowHeight: 34,
    columns: [
      { header: "ROLE",   name: "roleCode", width: 90, align: "center" },
      { header: "권한(ROLE)명", name: "roleName", minWidth: 140 }
    ]
  });

  roleGrid.on("click", (ev) => {
    const row = roleGrid.getRow(ev.rowKey);
    if (!row) return;

    selectedRoleCode = row.roleCode;
    loadRoleMenuAuth(row.roleCode);
  });
}

function loadRoleList() {
  $.ajax({
    url: "/auth/menu_permission/api/role",
    type: "GET",
    success: (roles) => roleGrid.resetData(roles || []),
    error: (xhr) => {
      console.error(xhr);
      alert("ROLE 조회 중 오류가 발생했습니다.");
    }
  });
}

function highlightRoleRow(roleCode) {
  const data = roleGrid.getData();
  const idx = data.findIndex(r => r.roleCode === roleCode);
  if (idx >= 0) roleGrid.focusAt(idx, 0);
}

/* ============================================================
   RIGHT: AUTH GRID (menuCode 기반 checkbox)
============================================================ */
function checkboxFormatter(colName) {
  // formatter 인자로 row까지 들어올 수 있게 처리 (TUI 버전에 따라 다름)
  return ({ value, row }) => {
    const checked = (value === "Y") ? "checked" : "";
    const disabled = isAdminRole() ? "disabled" : "";
    const menuCode = row?.menuCode || "";

    // ★ rowKey 절대 사용하지 말 것 (탭 리셋때 바뀜)
    return `
      <input type="checkbox"
             class="auth-chk"
             data-menucode="${menuCode}"
             data-col="${colName}"
             ${checked} ${disabled}/>
    `;
  };
}

function initAuthGrid() {
  authGrid = new tui.Grid({
    el: document.getElementById("authGrid"),
    scrollX: true,
    scrollY: true,
    bodyHeight: "fitToParent",
    rowHeight: 34,
    columns: [
      { header: "관리영역",   name: "systemType", width: 90,  align: "center" },
      { header: "메뉴코드", name: "menuCode",   width: 120, align: "center" },
      { header: "메뉴명",   name: "menuName",   minWidth: 260 },

      { header: "조회", name: "readYn",   width: 70, align: "center", formatter: checkboxFormatter("readYn") },
      { header: "등록", name: "createYn", width: 70, align: "center", formatter: checkboxFormatter("createYn") },
      { header: "수정", name: "updateYn", width: 70, align: "center", formatter: checkboxFormatter("updateYn") },
      { header: "삭제", name: "deleteYn", width: 70, align: "center", formatter: checkboxFormatter("deleteYn") }
    ]
  });
}

function loadRoleMenuAuth(roleCode) {
  $.ajax({
    url: "/auth/menu_permission/api/role-menu",
    type: "GET",
    data: { roleCode },
    success: (list) => {

      fullAuthData = (list || []).map(r => ({
        ...r,
        // 혹시 null/undefined 대비
        readYn: normalizeYN(r.readYn),
        createYn: normalizeYN(r.createYn),
        updateYn: normalizeYN(r.updateYn),
        deleteYn: normalizeYN(r.deleteYn)
      }));

      // ✅ ADMIN은 화면/저장 모두 보호 (항상 Y)
      if (roleCode === "ADMIN") {
        fullAuthData.forEach(r => {
          r.readYn = "Y";
          r.createYn = "Y";
          r.updateYn = "Y";
          r.deleteYn = "Y";
        });
      }

      originalAuthSnapshot = deepCopy(fullAuthData);

      applyAuthFilter();
    },
    error: (xhr) => {
      console.error(xhr);
      alert("메뉴권한 조회 중 오류가 발생했습니다.");
    }
  });
}

/* ============================================================
   이벤트 바인딩 (전부 위임 방식으로 강제 안정화)
============================================================ */
function bindEvents() {

  // LEFT 조회/초기화
  $(document).on("click", "#btnUserSearch", loadUserList);

  $(document).on("click", "#btnReset", function () {
    $("#searchUserName").val("");
    $("#searchDept").val("");
    $("#searchPosition").val("");
    userGrid.resetData([]);
  });

  // ✅ 탭 클릭 (클릭이 안 먹는 문제 방지: 위임)
  $(document).on("click", ".menu-tab-btn", function () {
    $(".menu-tab-btn").removeClass("active");
    $(this).addClass("active");

    currentSystemType = $(this).data("system");
    applyAuthFilter();
  });

  // ✅ 체크박스 변경 (menuCode로 원본(fullAuthData)만 수정)
  $(document).on("change", ".auth-chk", function (e) {
    e.stopPropagation();

    if (isAdminRole()) {
      // ADMIN은 변경 불가
      this.checked = true;
      return;
    }

    const menuCode = this.dataset.menucode;
    const col = this.dataset.col;
    const yn = this.checked ? "Y" : "N";

    const target = fullAuthData.find(r => r.menuCode === menuCode);
    if (target) target[col] = yn;
  });

  // ✅ 전체선택 (현재 탭만)
  $(document).on("click", "#btnAllCheck", function () {
    if (!selectedRoleCode) return alert("가운데에서 ROLE을 선택하세요.");
    if (isAdminRole()) return alert("ADMIN 권한은 수정할 수 없습니다.");

    const menuCodes = getFilteredMenuCodes();
    fullAuthData.forEach(r => {
      if (menuCodes.includes(r.menuCode)) {
        r.readYn = "Y";
        r.createYn = "Y";
        r.updateYn = "Y";
        r.deleteYn = "Y";
      }
    });

    applyAuthFilter();
  });

  // ✅ 전체해제 (현재 탭만)
  $(document).on("click", "#btnAllClear", function () {
    if (!selectedRoleCode) return alert("가운데에서 ROLE을 선택하세요.");
    if (isAdminRole()) return alert("ADMIN 권한은 수정할 수 없습니다.");

    const menuCodes = getFilteredMenuCodes();
    fullAuthData.forEach(r => {
      if (menuCodes.includes(r.menuCode)) {
        r.readYn = "N";
        r.createYn = "N";
        r.updateYn = "N";
        r.deleteYn = "N";
      }
    });

    applyAuthFilter();
  });

  // ✅ 되돌리기 (ROLE 전체 원본으로 복구 후 탭 필터 적용)
  $(document).on("click", "#btnRevert", function () {
    if (!selectedRoleCode) return alert("가운데에서 ROLE을 선택하세요.");

    fullAuthData = deepCopy(originalAuthSnapshot);
    applyAuthFilter();
    alert("되돌리기 완료");
  });

  // ✅ 저장 (ROLE 전체 fullAuthData를 저장)
  $(document).on("click", "#btnSaveAuth", function () {
    if (!selectedRoleCode) return alert("가운데에서 ROLE을 선택하세요.");
    if (isAdminRole()) return alert("ADMIN 권한은 저장/수정할 수 없습니다.");

    // 서버는 RoleMenuAuthVO 리스트를 기대 → roleCode/menuCode/권한 4개는 반드시 포함
    const payload = fullAuthData.map(r => ({
      roleCode: selectedRoleCode,
      menuCode: r.menuCode,
      readYn: normalizeYN(r.readYn),
      createYn: normalizeYN(r.createYn),
      updateYn: normalizeYN(r.updateYn),
      deleteYn: normalizeYN(r.deleteYn)
    }));

    $.ajax({
      url: "/auth/menu_permission/api/role-menu/save",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(payload),
      success: function () {
        alert("저장 완료");
        // 저장 후 스냅샷 갱신
        originalAuthSnapshot = deepCopy(fullAuthData);
      },
      error: function (xhr) {
        console.error(xhr);
        alert("저장 중 오류가 발생했습니다.");
      }
    });
  });

  // (선택) 사용자 ROLE 일괄 부여 버튼도 위임으로 안전하게
  $(document).on("click", "#btnSetRole", function () {
    const checkedKeys = userGrid.getCheckedRowKeys();
    if (!checkedKeys || checkedKeys.length === 0) return alert("왼쪽에서 사용자를 체크로 선택하세요.");
    if (!selectedRoleCode) return alert("가운데에서 ROLE을 선택하세요.");

    const userIds = checkedKeys.map(k => userGrid.getRow(k).userId);

    $.ajax({
      url: "/auth/menu_permission/api/user/setRole",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ roleCode: selectedRoleCode, userIds }),
      success: function () {
        alert("ROLE 부여 완료");
        loadUserList();
      },
      error: function (xhr) {
        console.error(xhr);
        alert("ROLE 부여 중 오류가 발생했습니다.");
      }
    });
  });
}
