package com.amir.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * This class serves as the entry point for authentication failures in the application.
 * When an unauthenticated user tries to access a protected resource, this entry point is invoked.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Invoked when an unauthenticated user tries to access a protected resource.
     *
     * @param request       HTTP request
     * @param response      HTTP response
     * @param authException Authentication exception indicating the reason for the failure
     * @throws IOException      I/O exception
     * @throws ServletException Servlet exception
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // Send an HTTP 401 Unauthorized error response to the client
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error -> Unauthorized");
    }
}
