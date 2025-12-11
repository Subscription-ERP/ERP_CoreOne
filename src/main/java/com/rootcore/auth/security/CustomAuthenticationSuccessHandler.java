package com.rootcore.auth.security;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.LoginUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserMenuAuthVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MenuPermissionService menuPermissionService;
    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인한 사용자 ID
        String userId = authentication.getName();

        // DB에서 사용자 정보 조회
        LoginUserVO loginUser = loginMapper.selectLoginUser(userId);
        if (loginUser == null) {
            response.sendRedirect("/auth/login?error");
            return;
        }

        String companyCode = loginUser.getCompanyCode();
        String roleCode = loginUser.getRoleCode();
        String finalRoleCode = (roleCode != null)
                ? roleCode.replace("ROLE_", "").toUpperCase()
                : null;

        // 메뉴 리스트
        List<RoleMenuAuthVO> sideMenuList =
                menuPermissionService.getLoginMenuList(companyCode, finalRoleCode);

        // URL 접근용 변환 리스트
        List<UserMenuAuthVO> userAuthList =
                sideMenuList.stream().map(vo -> {
                    UserMenuAuthVO u = new UserMenuAuthVO();
                    u.setCompanyCode(vo.getCompanyCode());
                    u.setRoleCode(finalRoleCode);
                    u.setMenuCode(vo.getMenuCode());
                    u.setMenuName(vo.getMenuName());
                    u.setMenuUrl(vo.getMenuUrl());
                    u.setSystemType(vo.getSystemType());
                    u.setReadYn(vo.getReadYn());
                    u.setCreateYn(vo.getCreateYn());
                    u.setUpdateYn(vo.getUpdateYn());
                    u.setDeleteYn(vo.getDeleteYn());
                    return u;
                }).toList();

        // ============================
        // ⭐⭐ 세션 저장 ⭐⭐ (로그아웃 메뉴 표시 핵심)
        // ============================
        session.setAttribute("LOGIN_USER_ID", userId);

        // 이 값이 없으면 logout 메뉴 전체가 안 보임
        session.setAttribute("LOGIN_USER_NAME", loginUser.getUserName());

        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);
        session.setAttribute("LOGIN_ROLE_CODE", finalRoleCode);

        session.setAttribute("LOGIN_MENU_AUTH", sideMenuList);
        session.setAttribute("LOGIN_USER_AUTH_LIST", userAuthList);

        // 실패횟수 초기화 & 계정 잠금 해제
        loginMapper.resetFailCountAndLastLogin(userId);
        loginMapper.unlockUserAccount(userId);

        System.out.println("===== LOGIN SUCCESS =====");
        System.out.println("USER   : " + userId);
        System.out.println("ROLE   : " + finalRoleCode);
        System.out.println("SIDE   : " + sideMenuList.size());
        System.out.println("AUTH   : " + userAuthList.size());
        System.out.println("=========================");

        response.sendRedirect("/");
    }
}
