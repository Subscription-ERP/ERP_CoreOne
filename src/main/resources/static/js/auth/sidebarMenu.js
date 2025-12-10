document.addEventListener("DOMContentLoaded", function () {

    const menuData = window.LOGIN_MENU_AUTH || [];
    console.log("✅ 사이드바 메뉴 세션 데이터:", menuData);

    const sideMenu = document.getElementById("navigation");
    if (!sideMenu) {
        console.error("❌ navigation 태그를 찾을 수 없음");
        return;
    }

    sideMenu.innerHTML = "";

    // ✅ SYSTEM_TYPE 별 그룹핑
    const groupMap = {};

    menuData.forEach(menu => {
        if (menu.readYn !== 'Y') return;

        if (!groupMap[menu.systemType]) {
            groupMap[menu.systemType] = [];
        }

        groupMap[menu.systemType].push(menu);
    });

    // ✅ 그룹 생성
    for (const systemType in groupMap) {

        let systemName = systemType;
        if (systemType === "CM") systemName = "공통";
        if (systemType === "FI") systemName = "회계";
        if (systemType === "HR") systemName = "인사";
        if (systemType === "SALES") systemName = "영업";
        if (systemType === "SUB") systemName = "구독";
        if (systemType === "SYSTEM") systemName = "시스템";

        const groupLi = document.createElement("li");
        groupLi.className = "nav-item";

        groupLi.innerHTML = `
            <a href="#" class="nav-link">
                <i class="nav-icon bi bi-folder"></i>
                <p>
                    ${systemName}
                    <i class="bi bi-chevron-right"></i>
                </p>
            </a>
            <ul class="nav nav-treeview"></ul>
        `;

        const subUl = groupLi.querySelector(".nav-treeview");

        groupMap[systemType].forEach(menu => {

            // ✅ ROOT 메뉴는 클릭 메뉴로 만들지 않음
            if (!menu.menuUrl || menu.menuUrl === "null") return;

            const itemLi = document.createElement("li");
            itemLi.className = "nav-item";

            itemLi.innerHTML = `
                <a href="${menu.menuUrl}" class="nav-link">
                    <i class="bi bi-dash"></i>
                    <p>${menu.menuName}</p>
                </a>
            `;

            subUl.appendChild(itemLi);
        });

        sideMenu.appendChild(groupLi);
    }
});
