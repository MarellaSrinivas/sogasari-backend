
package com.sogasari.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                String authHeader = request.getHeader("Authorization");

                // No Bearer token: continue.
                // Spring Security will protect authenticated endpoints.
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        filterChain.doFilter(request, response);
                        return;
                }

                String token = authHeader.substring(7);

                try {
                        if (!jwtService.isValid(token)) {
                                SecurityContextHolder.clearContext();

                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write(
                                                "{\"message\":\"Access token is invalid or expired\"}");
                                return;
                        }

                        String phone = jwtService.extractPhone(token);
                        String role = jwtService.extractRole(token);

                        // Preserve compatibility with older customer tokens.
                        if (role == null || role.isBlank()) {
                                role = "CUSTOMER";
                        }

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        phone,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                        SecurityContextHolder.getContext()
                                        .setAuthentication(authentication);

                } catch (JwtException | IllegalArgumentException ex) {
                        SecurityContextHolder.clearContext();

                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write(
                                        "{\"message\":\"Access token is invalid or expired\"}");
                        return;
                }

                filterChain.doFilter(request, response);
        }
}
