package com.amir.security.service;

import com.amir.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Objects;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;



    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Integer active;
    private Collection<? extends GrantedAuthority> authorities;



    public UserDetailsImpl(String id,
                           String username,
                           String email,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           Integer active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getActive()
        );
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) object;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(active, that.active) && Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, active, authorities);
    }
}
