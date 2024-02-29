package com.amir.security.jwt;

import com.amir.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${amir.app.jwtSecretKey}")
    private String jwtSecretKey;

    @Value("${amir.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration * 1000L))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }


    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJwt(authToken);
            return true;
        }catch (SignatureException exception){
            System.out.println("Invalid JWT signature: " + exception.getMessage());
        }catch (ExpiredJwtException exception){
            System.out.println("Expired JWT token: " + exception.getMessage());
        }catch (UnsupportedJwtException exception){
            System.out.println("Unsupported JWT token: " + exception.getMessage());
        }catch (IllegalArgumentException exception){
            System.out.println("JWT claims string is empty: " + exception.getMessage());
        }
        return false;
    }
}
