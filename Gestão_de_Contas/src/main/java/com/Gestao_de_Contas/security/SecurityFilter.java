package com.Gestao_de_Contas.security;

import com.Gestao_de_Contas.modules.user.entity.User;
import com.Gestao_de_Contas.modules.user.repository.UserRepository;
import com.Gestao_de_Contas.providers.JWTProviders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProviders jwtProvider;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");
            var subjectToken = this.jwtProvider.validateToken(token);

            if (subjectToken.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // ✅ carrega o usuário do banco
            UUID userId = UUID.fromString(subjectToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
