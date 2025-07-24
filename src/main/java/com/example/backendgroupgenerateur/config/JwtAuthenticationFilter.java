package com.example.backendgroupgenerateur.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backendgroupgenerateur.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;

        // 🔍 Extraction du token depuis le header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("✅ Token trouvé dans le header: " + token);
        } else {
            System.out.println("⛔ Aucun token trouvé dans le header Authorization");
        }

        // 🔒 Validation et traitement du token
        if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            System.out.println("✅ Utilisateur extrait du token : " + username);

            // Vérifie que l'utilisateur n'est pas déjà authentifié
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Récupérer le rôle directement du token JWT
                String role = jwtUtils.getRoleFromToken(token);
                System.out.println("✅ Rôle extrait du token : " + role);

                // Créer une liste d'autorités à partir du rôle
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                // Créer un UserDetails minimal avec le nom d'utilisateur et les autorités du token
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", authorities);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("🔐 Utilisateur authentifié : " + username);
            }
        } else if (token != null) {
            System.out.println("⛔ Token invalide");
        }

        // 🔁 Poursuite de la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
