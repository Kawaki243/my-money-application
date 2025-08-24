package com.project.moneymanagerbackend.security;

import com.project.moneymanagerbackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter that runs once per request.
 * <p>
 * - Extracts the JWT from the {@code Authorization} header (Bearer token).
 * - Validates the token using {@link JwtUtil}.
 * - If valid, loads the user via {@link UserDetailsService} and sets the authentication
 *   in {@link SecurityContextHolder} so Spring Security can authorize the request.
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Filters each incoming request to check for a valid JWT in the Authorization header.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if filter processing fails
     * @throws IOException if an input or output exception occurs
     */
    @Override
    protected void doFilterInternal( HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // Extract JWT from "Authorization: Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // ⚠️ Token parsing error (expired, malformed, etc.)
                throw new RuntimeException("JWT validation failed: {}"+e.getMessage());
            }

            // Authenticate user if token is valid and not already authenticated
            // Check if we extracted an email from the JWT and no user is currently authenticated in the context
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load the user details (username, password, roles) from the database using the email
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                // Validate the JWT against the loaded user details (check signature, expiration, subject match, etc.)
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // Create an authentication token for Spring Security with the user's details and authorities (roles/permissions)
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,        // Principal (the user details object)
                            null,               // No credentials since we already have a valid JWT
                            userDetails.getAuthorities() // Granted authorities (roles/permissions)
                    );

                    // Attach request-specific details (e.g., remote IP, session ID) to the authentication object
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Store the authentication object in the SecurityContext so Spring Security knows the user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}

