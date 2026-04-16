package com.Gestao_de_Contas.modules.user.useCase;

import com.Gestao_de_Contas.modules.user.dto.AuthUserDTO;
import com.Gestao_de_Contas.modules.user.dto.TokenDTO;
import com.Gestao_de_Contas.modules.user.entity.RefreshToken;
import com.Gestao_de_Contas.modules.user.entity.Role;
import com.Gestao_de_Contas.modules.user.repository.RefreshTokenRepository;
import com.Gestao_de_Contas.modules.user.repository.UserRepository;
import com.Gestao_de_Contas.providers.JWTProviders;
import com.Gestao_de_Contas.security.TokenHashUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthUserUseCase {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTProviders  jwtProviders;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    //Tudo ou nada
    @Transactional
    public TokenDTO execute(AuthUserDTO authUserDTO){
        var user = this.userRepository.findByEmail(authUserDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        var passwordMatches = this.passwordEncoder.matches(authUserDTO.getPassword(), user.getPassword());
        if(!passwordMatches){
            throw new AuthenticationServiceException("Invalid credentials");
        }

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        refreshTokenRepository.deleteByUser(user);
        var generateTokenUser = this.jwtProviders.generateToken(user.getUsername());
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        var rawRefresh = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(TokenHashUtil.hashUtil(rawRefresh))
                        .user(user)
                        .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                        .build()
        );

        return new TokenDTO(generateTokenUser, rawRefresh, user.getId().toString(), user.getRole().name());
    }
}
