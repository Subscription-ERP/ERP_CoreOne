package com.rootcore.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rootcore.auth.vo.UserMenuAuthVO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MenuAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // /erp/hr/userManage → /hr/userManage 형태로 정리
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }

        // 1) 필터 제외 URL (정적리소스, 로그인/에러, 내부 API 등)
        if (isExcluded(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            // 세션 없으면 권한 정보도 없으니 일단 통과 (인증 필터가 따로 막을 것)
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 로그인한 ROLE_CODE 가져오기 (로그인 성공 시 세션에 넣어둔 값: ADMIN / MANAGER / USER)
        String roleCode = (String) session.getAttribute("LOGIN_ROLE_CODE");

        // 2) ⭐ ADMIN 은 모든 URL 허용 (권한 체크 생략)
        if ("ADMIN".equals(roleCode)) {
            filterChain.doFilter(request, response);
            return;
        }
        if ("MANAGER".equals(roleCode)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) 그 외 ROLE 은 메뉴 권한 체크
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            filterChain.doFilter(request, response);
            return;
        }

        // ⭐ URL 권한 리스트 (로그인 시 세션에 저장한 리스트)
        @SuppressWarnings("unchecked")
        List<UserMenuAuthVO> userAuthList =
                (List<UserMenuAuthVO>) session.getAttribute("LOGIN_USER_AUTH_LIST");

        if (userAuthList == null || userAuthList.isEmpty()) {
            // 권한 정보 없으면 일단 통과 (필요하면 여기서 바로 noAuth 로 보내도록 변경 가능)
            filterChain.doFilter(request, response);
            return;
        }

        final String reqUri = uri;

        // 4) READ_YN = Y 이면서, URL이 앞부분 일치하는 메뉴가 하나라도 있으면 허용
        boolean allowed = userAuthList.stream()
                .filter(vo -> "Y".equalsIgnoreCase(vo.getReadYn()))
                .anyMatch(vo ->
                        vo.getMenuUrl() != null &&
                        !vo.getMenuUrl().isEmpty() &&
                        reqUri.startsWith(vo.getMenuUrl())
                );

        if (!allowed) {
            response.sendRedirect(contextPath + "/auth/noAuth");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /** 제외 URL */
    private boolean isExcluded(String uri) {

        // 정적 리소스
        if (uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/images/")
                || uri.startsWith("/assets/")
                || uri.startsWith("/favicon")) {
            return true;
        }

        // 인증 관련 화면 (로그인, 에러, noAuth 등)
        if (uri.startsWith("/auth/")
                || uri.equals("/")
                || uri.startsWith("/error")) {
            return true;
        }

        // 결제/계약 관련은 권한 미적용
        if (uri.startsWith("/company")
                || uri.startsWith("/plans")
                || uri.startsWith("/contract")
                || uri.startsWith("/payment")
                || uri.startsWith("/api/payments")
                || uri.startsWith("/api/success")
                || uri.startsWith("/api/fail")
                || uri.startsWith("/api/billing")) {
            return true;
        }

        // 내부 API 허용 (AJAX)
        if (uri.startsWith("/api/com/commonCodes")
                || uri.startsWith("/api/hr/getDeptName")
                || uri.startsWith("/api/hr/userAllList")
                || uri.startsWith("/api/hr/getPositionCodes")
                || uri.startsWith("/api/hr/getRankCodes")
                || uri.startsWith("/api/hr/user/detail")
                || uri.startsWith("/cm/deptCodeManage")   // 실제 화면
                || uri.startsWith("/hr/deptCodeManage")   // 모달
        ) {
            return true;
        }

        return false;
    }

}
