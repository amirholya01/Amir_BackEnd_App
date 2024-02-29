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


    /**
     * Generate a JWT token for the authenticated user.
     *
     * @param authentication The authentication object representing the authenticated user.
     * @return The JWT token.
     */
    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration * 1000L))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }



    /**
     * Validate the JWT token.
     *
     * @param authToken The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
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


    /**
     * Retrieve the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }
}
