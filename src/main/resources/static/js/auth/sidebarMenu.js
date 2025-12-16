document.addEventListener("DOMContentLoaded", function () {

    // ✅ 서버에서 세션으로 내려준 로그인 사용자 메뉴 목록
    const menuData = window.LOGIN_MENU_AUTH || [];

    console.log("✅ 로그인 사용자 메뉴 권한:", menuData);

    const sideMenu = document.getElementById("sideMenu");

    if (!sideMenu) {
        console.error("❌ sideMenu DOM을 찾을 수 없습니다.");
        return;
    }

    // ✅ 기존 메뉴 전부 제거 (정적 HTML 완전 차단)
    sideMenu.innerHTML = "";

    // ✅ 1) systemType(탭) 코드 -> 한글명 매핑
    // - 너가 말한 최종 탭명 기준
    const SYSTEM_LABEL = {
        CM: "공통기능",
        FI: "회계관리",
        HR: "인사관리",
        SALES: "영업관리",
        SD: "영업관리",      // (혹시 SD로 내려오면 SALES로 취급)
        SUB: "구독관리",
        SYSTEM: "시스템관리"
    };

    // ✅ 2) 출력 순서 고정
    const ORDER = ["CM", "FI", "HR", "SALES", "SUB", "SYSTEM"];

    // ✅ 시스템 타입별 그룹화
    const groupMap = {};

    menuData.forEach(menu => {

        // ✅ ✅ ✅ 핵심: READ 권한 없는 메뉴는 무조건 제외
        // (혹시 대문자/다른 필드명 대비)
        const readYn = menu.readYn ?? menu.READ_YN;
        if (readYn !== "Y") return;

        let systemType = (menu.systemType ?? menu.SYSTEM_TYPE ?? "").toString().toUpperCase();

        // ✅ SD로 내려오면 SALES로 합치기
        if (systemType === "SD") systemType = "SALES";

        if (!groupMap[systemType]) {
            groupMap[systemType] = [];
        }

        groupMap[systemType].push(menu);
    });

    // ✅ ✅ ✅ 실제 화면에 출력 (ORDER 순서대로)
    ORDER.forEach(systemType => {

        const list = groupMap[systemType];
        if (!list || list.length === 0) return;

        const groupLabel = SYSTEM_LABEL[systemType] || systemType;

        // ✅ 3) 하위 메뉴 정리(중복 제거)
        // - 탭명과 동일한 메뉴명 제거
        // - (옵션) menuUrl 없는 폴더용 제거
        const children = list.filter(menu => {
            const menuName = (menu.menuName ?? menu.MENU_NAME ?? "").toString().trim();
            const menuUrl  = (menu.menuUrl ?? menu.MENU_URL ?? "").toString().trim();

            // 3-1) 탭명과 같은 하위메뉴(중복) 제거
            if (menuName === groupLabel) return false;

            // 3-2) 폴더용(상위용) 메뉴 제거 (원하면 아래 줄 주석처리)
            if (!menuUrl || menuUrl === "#") return false;

            return true;
        });

        // 하위가 하나도 없으면 탭 자체도 출력 안 함
        if (children.length === 0) return;

        // ✅ 4) 정렬 (sortOrder/menuOrder 있으면 우선, 없으면 이름 기준)
        children.sort((a, b) => {
            const ao = Number(a.sortOrder ?? a.SORT_ORDER ?? a.menuOrder ?? a.MENU_ORDER ?? 9999);
            const bo = Number(b.sortOrder ?? b.SORT_ORDER ?? b.menuOrder ?? b.MENU_ORDER ?? 9999);
            if (ao !== bo) return ao - bo;

            const an = (a.menuName ?? a.MENU_NAME ?? "").toString();
            const bn = (b.menuName ?? b.MENU_NAME ?? "").toString();
            return an.localeCompare(bn);
        });

        // 1️⃣ 상위 그룹(li)
        const groupLi = document.createElement("li");
        groupLi.className = "nav-item"; // ✅ 기본은 닫힌 상태 (원하면 menu-open 추가)

        groupLi.innerHTML = `
            <a href="#" class="nav-link">
                <i class="nav-icon bi bi-folder"></i>
                <p>${groupLabel}
                    <i class="nav-arrow bi bi-chevron-right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview"></ul>
        `;

        const subUl = groupLi.querySelector("ul");

        // 2️⃣ 하위 메뉴들
        children.forEach(menu => {

            const menuName = (menu.menuName ?? menu.MENU_NAME ?? "").toString().trim();
            let menuUrl = (menu.menuUrl ?? menu.MENU_URL ?? "").toString().trim();

            // ✅ url 보정: "/hr/userManage" 처럼 슬래시가 없으면 붙여줌 (원치 않으면 제거 가능)
            if (menuUrl && menuUrl !== "#" && !menuUrl.startsWith("/")) {
                menuUrl = "/" + menuUrl;
            }

            const itemLi = document.createElement("li");
            itemLi.className = "nav-item";

            itemLi.innerHTML = `
                <a href="${menuUrl}" class="nav-link">
                    <i class="nav-icon bi bi-dash"></i>
                    <p>${menuName}</p>
                </a>
            `;

            subUl.appendChild(itemLi);
        });

        // ✅ 최종적으로 사이드바에 추가
        sideMenu.appendChild(groupLi);
    });

});
