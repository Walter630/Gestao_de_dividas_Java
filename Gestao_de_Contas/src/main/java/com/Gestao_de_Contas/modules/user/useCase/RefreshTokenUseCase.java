package com.Gestao_de_Contas.modules.user.useCase;

import com.Gestao_de_Contas.modules.user.dto.TokenDTO;
import com.Gestao_de_Contas.modules.user.entity.RefreshToken;
import com.Gestao_de_Contas.modules.user.repository.RefreshTokenRepository;
import com.Gestao_de_Contas.providers.JWTProviders;
import com.Gestao_de_Contas.security.TokenHashUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenUseCase {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JWTProviders jwtProviders;

    @Transactional
    public TokenDTO execute(String token) throws AuthenticationServiceException {
            var tokenHash = TokenHashUtil.hashUtil(token);
            // 2. Busca o token no banco
            var storedToken = refreshTokenRepository.findByToken(tokenHash)
                    .orElseThrow(() -> new AuthenticationServiceException("Invalid credentials"));
            if(storedToken.getExpiryDate().isBefore(Instant.now())) {
                this.refreshTokenRepository.delete(storedToken );
                throw new AuthenticationServiceException("Expired token, please login again");
            }
            // 4. ✅ ROTAÇÃO: apaga o token antigo — chave velha destruída!
            refreshTokenRepository.delete(storedToken );

            var user = storedToken.getUser();
            var newAccessToken = this.jwtProviders.generateToken(
                    user.getEmail()
            );
            var newRawRefresh = generateSecureToken();
            var newHash = TokenHashUtil.hashUtil(newRawRefresh);
            System.out.print("teste" + newHash);
            System.out.println("novo token" + newAccessToken);
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .token(newHash)
                            .user(user)
                            .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                            .build()
            );

            return new TokenDTO(newAccessToken, newRawRefresh); // ← par completo
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
