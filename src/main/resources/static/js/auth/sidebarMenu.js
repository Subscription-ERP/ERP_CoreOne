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

    // ✅ 시스템 타입별 그룹화 (CM / FI / HR / SD / SYSTEM ...)
    const groupMap = {};

    menuData.forEach(menu => {

        // ✅ ✅ ✅ 핵심: READ 권한 없는 메뉴는 무조건 제외
        if (menu.readYn !== "Y") return;

        if (!groupMap[menu.systemType]) {
            groupMap[menu.systemType] = [];
        }

        groupMap[menu.systemType].push(menu);
    });

    // ✅ ✅ ✅ 실제 화면에 출력
    Object.keys(groupMap).forEach(systemType => {

        // 1️⃣ 상위 그룹(li)
        const groupLi = document.createElement("li");
        groupLi.className = "nav-item menu-open";

        groupLi.innerHTML = `
            <a href="#" class="nav-link">
                <i class="nav-icon bi bi-folder"></i>
                <p>${systemType}
                    <i class="nav-arrow bi bi-chevron-right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview"></ul>
        `;

        const subUl = groupLi.querySelector("ul");

        // 2️⃣ 하위 메뉴들
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

        // ✅ 최종적으로 사이드바에 추가
        sideMenu.appendChild(groupLi);
    });

});
