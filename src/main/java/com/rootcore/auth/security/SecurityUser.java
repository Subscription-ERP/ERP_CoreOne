package com.rootcore.auth.security;

import com.rootcore.auth.vo.LoginVO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails {

    private final LoginVO loginVO;

    public SecurityUser(LoginVO loginVO) {
        this.loginVO = loginVO;
    }

    // ✅ ✅ ✅ 여기만 제대로 동작하면 403 끝난다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // DB에서 가져온 ROLE_CODE (ADMIN / MANAGER / USER)
        String roleCode = loginVO.getRoleCode();

        // 안전장치 (혹시라도 null이면 권한 없음 처리)
        if (roleCode == null || roleCode.isBlank()) {
            return List.of();
        }

        // Spring Security는 "ROLE_권한명" 형식만 인식함
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + roleCode.toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return loginVO.getDbPassword();
    }

    @Override
    public String getUsername() {
        return loginVO.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"LOCKED".equals(loginVO.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
