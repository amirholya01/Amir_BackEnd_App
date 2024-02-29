package com.amir.security.jwt;

import com.amir.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;



    /**
     * This method filters incoming requests to extract JWT token,
     * validate it, and set authentication in the security context.
     *
     * @param request     HTTP request
     * @param response    HTTP response
     * @param filterChain Filter chain
     * @throws ServletException Servlet exception
     * @throws IOException      I/O exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            // Retrieve JWT token from the request
            String jwt = getJwt(request);

            // Validate JWT token and set authentication if valid
            if (jwt != null && tokenProvider.validateJwtToken(jwt)){
                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            // Handle any exception that occurs during filtering
            System.out.println(e);
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }


    /**
     * Extracts JWT token from the request header.
     *
     * @param request HTTP request
     * @return JWT token if found in the Authorization header, otherwise null
     */
    private String getJwt(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer")){
            return authHeader.replace("Bearer", "");
        }
        return null;
    }
}
