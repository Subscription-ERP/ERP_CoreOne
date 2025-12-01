package com.rootcore.auth.security;

import com.rootcore.auth.vo.LoginVO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class SecurityUser implements UserDetails {

    private final LoginVO loginVO;

    public SecurityUser(LoginVO loginVO) {
        this.loginVO = loginVO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 메뉴 권한/ROLE은 추후 추가
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
