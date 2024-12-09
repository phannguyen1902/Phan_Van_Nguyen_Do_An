package com.example.befall23datnsd05.security.customer;

import com.example.befall23datnsd05.entity.KhachHang;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomKhachHangDetail implements UserDetails {

    private final KhachHang khachHang;

    public CustomKhachHangDetail(KhachHang khachHang){
        this.khachHang = khachHang;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Long getKhachHangId() {
        return khachHang.getId();
    }
    @Override
    public String getPassword() {
        return khachHang.getMatKhau();
    }

    @Override
    public String getUsername() {
        return khachHang.getEmail();
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
}
