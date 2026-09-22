package com.scholarsphere.usage.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.Collections;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;


    public JwtAuthenticationFilter(
            JwtService jwtService) {

        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        // =====================================================
        // READ AUTHORIZATION HEADER
        // =====================================================

        String authorizationHeader =
                request.getHeader(
                        HttpHeaders.AUTHORIZATION
                );


        // =====================================================
        // NO JWT
        // =====================================================

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // =====================================================
        // EXTRACT TOKEN
        // =====================================================

        String token =
                authorizationHeader.substring(7);


        try {

            // =================================================
            // VALIDATE TOKEN
            // =================================================

            if (jwtService.isTokenValid(token)) {

                String email =
                        jwtService.extractEmail(token);

                String role =
                        jwtService.extractRole(token);

                Long userId =
                        jwtService.extractUserId(token);


                // =============================================
                // CREATE AUTHENTICATION
                // =============================================

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role
                                        )
                                )
                        );


                // =============================================
                // STORE USER ID
                // =============================================

                authentication.setDetails(userId);


                // =============================================
                // STORE AUTHENTICATION
                // =============================================

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }

        } catch (Exception e) {

            SecurityContextHolder
                    .clearContext();
        }


        // =====================================================
        // CONTINUE
        // =====================================================

        filterChain.doFilter(
                request,
                response
        );
    }
}