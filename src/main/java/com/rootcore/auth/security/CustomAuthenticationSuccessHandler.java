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

        // DB에서 사용자 정보 조회
        LoginUserVO loginUser = loginMapper.selectLoginUser(userId);
        if (loginUser == null) {
            response.sendRedirect("/auth/login?error");
            return;
        }

        String companyCode = loginUser.getCompanyCode();
        String roleCode = loginUser.getRoleCode(); // 예: ROLE_ADMIN
        String finalRoleCode = (roleCode != null)
                ? roleCode.replace("ROLE_", "").toUpperCase()   // 예: ADMIN
                : null;

        // ============================
        //  메뉴 권한 조회 (ROLE 기반)
        // ============================
        List<RoleMenuAuthVO> sideMenuList =
                menuPermissionService.getLoginMenuList(companyCode, finalRoleCode);

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
        // ⭐⭐ 세션 저장 ⭐⭐
        // ============================
        session.setAttribute("LOGIN_USER_ID", userId);
        session.setAttribute("LOGIN_USER_NAME", loginUser.getUserName());
        session.setAttribute("LOGIN_COMPANY_CODE", companyCode);

        // ✅ 부서/직급 세션 저장 추가
        session.setAttribute("LOGIN_DEPT", loginUser.getDept());
        session.setAttribute("LOGIN_POSITION", loginUser.getPosition());

        // 필터/사이드바에서 쓰는 ROLE_CODE (ADMIN / MANAGER / USER)
        session.setAttribute("LOGIN_ROLE_CODE", finalRoleCode);

        // 사이드바 메뉴용
        session.setAttribute("LOGIN_MENU_AUTH", sideMenuList);

        // URL 권한 체크용
        session.setAttribute("LOGIN_USER_AUTH_LIST", userAuthList);

        // (필요하다면 원본 ROLE_ 값도 별도 키로 보관 가능)
        session.setAttribute("LOGIN_ROLE_CODE_RAW", roleCode);

        // 출근 로직 호출(인사)
        attendanceService.checkinTodayIfNeeded(companyCode, userId);

        // 실패횟수 초기화 & 계정 잠금 해제
        loginMapper.resetFailCountAndLastLogin(userId);
        loginMapper.unlockUserAccount(userId);

        System.out.println("===== LOGIN SUCCESS =====");
        System.out.println("USER   : " + userId);
        System.out.println("ROLE   : " + finalRoleCode + " (raw=" + roleCode + ")");
        System.out.println("DEPT   : " + loginUser.getDept());
        System.out.println("POS    : " + loginUser.getPosition());
        System.out.println("SIDE   : " + sideMenuList.size());
        System.out.println("AUTH   : " + userAuthList.size());
        System.out.println("=========================");

        // 메인으로 이동
        response.sendRedirect("/");
    }
}
