package com.example.vitabuddy.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserInfo implements UserDetails {

    private final MemberDTO dto;

    public UserInfo(MemberDTO dto) {
        this.dto = dto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> dto.getUserRole());
        return collection;
    }

    @Override
    public String getPassword() {
        return dto.getUserPwd();
    }

    @Override
    public String getUsername() {
        return dto.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + dto.getUserId() + '\'' +
                ", userRole='" + dto.getUserRole() + '\'' +
                '}';
    }
}
