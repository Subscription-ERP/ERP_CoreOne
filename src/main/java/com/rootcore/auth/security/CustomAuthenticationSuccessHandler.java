package com.rootcore.auth.security;

import com.rootcore.auth.mapper.LoginMapper;
import com.rootcore.auth.service.MenuPermissionService;
import com.rootcore.auth.vo.LoginUserVO;
import com.rootcore.auth.vo.RoleMenuAuthVO;
import com.rootcore.auth.vo.UserMenuAuthVO;
import com.rootcore.hr.service.AttendanceService;

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
    private final AttendanceService attendanceService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 로그인한 사용자 ID
        String userId = authentication.getName();

        // ✅ 로그인 폼에서 넘어온 회사코드
        String companyCode = request.getParameter("companyCode");

        // (방어) 회사코드가 비어있으면 로그인페이지로
        if (companyCode == null || companyCode.trim().isEmpty()) {
            response.sendRedirect("/auth/login?error=noCompany");
            return;
        }

        // ✅ 회사코드 + userId로 사용자 정보 조회
        LoginUserVO loginUser = loginMapper.selectLoginUser(companyCode, userId);

        // 회사코드 불일치 or 사용자 없음
        if (loginUser == null) {
            response.sendRedirect("/auth/login?error=noCompany");
            return;
        }

        // DB 기준 회사코드 (정상)
        String dbCompanyCode = loginUser.getCompanyCode();

        // ROLE 처리
        String roleCode = loginUser.getRoleCode(); // 예: ROLE_ADMIN 또는 ADMIN
        String finalRoleCode = (roleCode != null)
                ? roleCode.replace("ROLE_", "").toUpperCase()
                : null;

        // ============================
        // 메뉴 권한 조회 (ROLE 기반)
        // ============================
        List<RoleMenuAuthVO> sideMenuList =
                menuPermissionService.getLoginMenuList(dbCompanyCode, finalRoleCode);

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
        // 세션 저장
        // ============================
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_USER_NAME", loginUser.getUserName());
        session.setAttribute("LOGIN_COMPANY_CODE", dbCompanyCode);
        session.setAttribute("LOGIN_DEPT", loginUser.getDept());
        session.setAttribute("LOGIN_POSITION", loginUser.getPosition());
        session.setAttribute("LOGIN_ROLE_CODE", finalRoleCode);
        session.setAttribute("LOGIN_MENU_AUTH", sideMenuList);
        session.setAttribute("LOGIN_USER_AUTH_LIST", userAuthList);
        session.setAttribute("LOGIN_ROLE_CODE_RAW", roleCode);

        // 출근 로직
        attendanceService.checkinTodayIfNeeded(dbCompanyCode, userId);

        // 실패횟수 초기화 & 잠금해제
        loginMapper.resetFailCountAndLastLogin(userId);
        loginMapper.unlockUserAccount(userId);

        response.sendRedirect("/");
    }
}
