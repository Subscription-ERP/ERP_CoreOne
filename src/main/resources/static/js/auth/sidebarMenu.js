document.addEventListener("DOMContentLoaded", function () {

    // ✅ 1. 세션에서 로그인 메뉴 데이터 가져오기
    const menuData = window.LOGIN_MENU_AUTH || [];

    console.log("✅ 사이드바 메뉴 세션 데이터:", menuData);

    const sideMenu = document.getElementById("navigation");
    if (!sideMenu) {
        console.error("❌ navigation 태그를 찾을 수 없음");
        return;
    }

    // 기존 정적 메뉴 제거
    sideMenu.innerHTML = "";

    // ✅ 2. SYSTEM_TYPE 기준으로 그룹핑
    const groupMap = {};

    menuData.forEach(menu => {

        // ✅ READ 권한 없는 메뉴는 완전 차단
        if (menu.readYn !== 'Y') return;

        if (!groupMap[menu.systemType]) {
            groupMap[menu.systemType] = [];
        }

        groupMap[menu.systemType].push(menu);
    });

    // ✅ 3. 그룹별로 메뉴 생성
    for (const systemType in groupMap) {

        const groupLi = document.createElement("li");
        groupLi.className = "nav-item menu-open";

        // ✅ 시스템명 한글 변환 (선택)
        let systemName = systemType;
        if (systemType === "CM") systemName = "공통";
        if (systemType === "FI") systemName = "회계";
        if (systemType === "HR") systemName = "인사";
        if (systemType === "SALES") systemName = "영업";
        if (systemType === "SUB") systemName = "구독";
        if (systemType === "SYSTEM") systemName = "시스템";

        groupLi.innerHTML = `
            <a href="#" class="nav-link">
                <i class="nav-icon bi bi-folder"></i>
                <p>
                    ${systemName}
                    <i class="nav-arrow bi bi-chevron-right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview"></ul>
        `;

        const subUl = groupLi.querySelector("ul");

        // ✅ 4. 하위 메뉴 생성
        groupMap[systemType].forEach(menu => {

            const itemLi = document.createElement("li");
            itemLi.className = "nav-item";

            itemLi.innerHTML = `
                <a href="${menu.menuUrl}" class="nav-link">
                    <i class="nav-icon bi bi-dash"></i>
                    <p>${menu.menuName}</p>
                </a>
            `;

            subUl.appendChild(itemLi);
        });

        // ✅ 5. 최종 sidebar에 추가
        sideMenu.appendChild(groupLi);
    }
});
