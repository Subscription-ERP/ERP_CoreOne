document.addEventListener("DOMContentLoaded", function () {

    const menuData = window.LOGIN_MENU_AUTH || [];
    console.log("✅ 로그인 사용자 메뉴 권한:", menuData);

    const sideMenu = document.getElementById("sideMenu");
    if (!sideMenu) {
        console.error("❌ sideMenu DOM을 찾을 수 없습니다.");
        return;
    }

    sideMenu.innerHTML = "";

    const SYSTEM_LABEL = {
        CM: "공통기능",
        FI: "회계관리",
        HR: "인사관리",
        SALES: "영업관리",
        SD: "영업관리",
        SUB: "구독관리",
        SYSTEM: "시스템관리"
    };

    const ORDER = ["CM", "FI", "HR", "SALES", "SUB", "SYSTEM"];

    const groupMap = {};

    menuData.forEach(menu => {
        const readYn = menu.readYn ?? menu.READ_YN;
        if (readYn !== "Y") return;

        let systemType = (menu.systemType ?? menu.SYSTEM_TYPE ?? "").toString().toUpperCase();
        if (systemType === "SD") systemType = "SALES";

        if (!groupMap[systemType]) groupMap[systemType] = [];
        groupMap[systemType].push(menu);
    });

    ORDER.forEach(systemType => {

        const list = groupMap[systemType];
        if (!list || list.length === 0) return;

        const groupLabel = SYSTEM_LABEL[systemType] || systemType;

        const children = list.filter(menu => {
            const menuName = (menu.menuName ?? menu.MENU_NAME ?? "").toString().trim();
            const menuUrl  = (menu.menuUrl  ?? menu.MENU_URL  ?? "").toString().trim();
            const menuCode = (menu.menuCode ?? menu.MENU_CODE ?? "").toString().toUpperCase();

            // ✅ (1) MODAL 메뉴 숨김
            const isModal =
                menuName.includes("모달") ||
                menuCode.includes("POP") ||
                menuUrl.includes("/modal") ||
                menuUrl.includes("_modal");
            if (isModal) return false;

            // ✅ (2) 출력물/인쇄 화면 숨김
            const isPrintView =
                menuName.includes("출력물") ||
                menuUrl.includes("/print") ||
                menuCode === "FI-LST-003";
            if (isPrintView) return false;

            // ✅ (3) SYSTEM 탭에서 "회사등록/회원가입" 숨김 (CM/SUB는 영향 없음)
            const isSystemJoinHidden =
                systemType === "SYSTEM" && (
                    menuCode === "SYS-JOIN" ||
                    menuUrl === "/auth/register" ||
                    menuName.includes("회원가입") ||
                    menuName.includes("회사등록/회원가입")
                );
            if (isSystemJoinHidden) return false;

            // ✅ (4) 운영성 안내 화면은 사이드바에서 숨김
            // - 5회 오류 안내 (/auth/password_lock)
            const isOpsNotice =
                menuCode === "CM-PASS-LOCK" ||
                menuUrl === "/auth/password_lock" ||
                menuName.includes("5회") ||
                menuName.includes("오류 안내");
            if (isOpsNotice) return false;

            // ✅ (5) 비밀번호 재설정은 "내 계정(마이페이지)" 성격 → 사이드바에서 숨김(추천)
            // - 우측 유저메뉴에 링크로 빼서 사용
            const isPasswordResetMyPage =
                menuCode === "CM-PASS-RESET" ||
                menuUrl === "/auth/password_reset" ||
                menuName.includes("비밀번호 재설정");
            if (isPasswordResetMyPage) return false;

            // ✅ (6) 탭명 중복 제거
            if (menuName === groupLabel) return false;

            // ✅ (7) URL 없는 폴더용 제거
            if (!menuUrl || menuUrl === "#") return false;

            return true;
        });

        if (children.length === 0) return;

        children.sort((a, b) => {
            const ao = Number(a.sortOrder ?? a.SORT_ORDER ?? a.menuOrder ?? a.MENU_ORDER ?? 9999);
            const bo = Number(b.sortOrder ?? b.SORT_ORDER ?? b.menuOrder ?? b.MENU_ORDER ?? 9999);
            if (ao !== bo) return ao - bo;

            const an = (a.menuName ?? a.MENU_NAME ?? "").toString();
            const bn = (b.menuName ?? b.MENU_NAME ?? "").toString();
            return an.localeCompare(bn);
        });

        const groupLi = document.createElement("li");
        groupLi.className = "nav-item";

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

        children.forEach(menu => {
            const menuName = (menu.menuName ?? menu.MENU_NAME ?? "").toString().trim();
            let menuUrl = (menu.menuUrl ?? menu.MENU_URL ?? "").toString().trim();

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

        sideMenu.appendChild(groupLi);
    });

});
