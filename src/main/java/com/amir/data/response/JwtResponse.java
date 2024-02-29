package com.amir.data.response;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private String result;
    private String id;

    public JwtResponse(String token, String username, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
    }

    public JwtResponse(String token, String username, Collection<? extends GrantedAuthority> authorities, String result, String id) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
        this.result = result;
        this.id = id;
    }

    public JwtResponse(String token, String type, String username, Collection<? extends GrantedAuthority> authorities, String result, String id) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.authorities = authorities;
        this.result = result;
        this.id = id;
    }
}
