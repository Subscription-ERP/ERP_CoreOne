package com.rootcore.auth.util;
import java.util.List;

import org.springframework.stereotype.Component;

// 	session권한 검사용 util class
// 	MANAGER/USER가 메뉴 URL을 직접 치거나 즐겨찾기 타고 들어올 때
//	그 메뉴에 READ 권한이 없으면 → 권한 없음 페이지로 이동
import com.rootcore.auth.vo.UserMenuAuthVO;

import jakarta.servlet.http.HttpSession;

@Component
public class MenuAuthChecker {

    /**
     * 특정 메뉴코드에 대해 READ 권한이 있는지 확인
     */
    @SuppressWarnings("unchecked")
    public boolean hasReadAuth(HttpSession session, String menuCode) {

        Object obj = session.getAttribute("LOGIN_MENU_AUTH");
        if (obj == null || !(obj instanceof List<?>)) {
            return false;
        }

        List<UserMenuAuthVO> authList = (List<UserMenuAuthVO>) obj;

        return authList.stream()
                .anyMatch(a ->
                        menuCode.equals(a.getMenuCode())
                                && "Y".equalsIgnoreCase(a.getReadYn())
                );
    }
}
