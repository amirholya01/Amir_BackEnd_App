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

/**
 * Implementation of Spring Security UserDetails interface to represent authenticated users.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;



    // Fields representing user details
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Integer active;
    private Collection<? extends GrantedAuthority> authorities;



    /**
     * Constructor to create a UserDetailsImpl object.
     * @param id The user ID.
     * @param username The username.
     * @param email The email address.
     * @param password The password.
     * @param authorities The authorities (roles) granted to the user.
     * @param active The active status of the user.
     */
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


    /**
     * Static method to build UserDetailsImpl object from User object.
     * @param user The user object.
     * @return UserDetailsImpl object.
     */
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

    // Getters for user details
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

    // Methods representing account status
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
