package com.Gestao_de_Contas.modules.user.useCase;

import com.Gestao_de_Contas.modules.user.dto.AuthUserDTO;
import com.Gestao_de_Contas.modules.user.dto.TokenDTO;
import com.Gestao_de_Contas.modules.user.entity.RefreshToken;
import com.Gestao_de_Contas.modules.user.repository.RefreshTokenRepository;
import com.Gestao_de_Contas.modules.user.repository.UserRepository;
import com.Gestao_de_Contas.providers.JWTProviders;
import com.Gestao_de_Contas.security.TokenHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    public TokenDTO execute(AuthUserDTO authUserDTO){
        var user = this.userRepository.findByName(authUserDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        var passwordMatches = this.passwordEncoder.matches(authUserDTO.getPassword(), user.getPassword());
        if(!passwordMatches){
            throw new AuthenticationServiceException("Invalid password");
        }
        var generateTokenUser = this.jwtProviders.generateToken(user.getUsername());
        var refreshUUID = UUID.randomUUID().toString();
        var refreshHash = TokenHashUtil.hashUtil(refreshUUID);
        var refreshSave = RefreshToken.builder()
                .token(refreshHash)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        this.refreshTokenRepository.save(refreshSave);
        return new TokenDTO(generateTokenUser, refreshUUID);
    }
}
